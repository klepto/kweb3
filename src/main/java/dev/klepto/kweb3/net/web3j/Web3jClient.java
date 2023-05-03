package dev.klepto.kweb3.net.web3j;

import dev.klepto.kweb3.Web3Error;
import dev.klepto.kweb3.Web3Request;
import dev.klepto.kweb3.Web3Response;
import dev.klepto.kweb3.abi.type.Address;
import dev.klepto.kweb3.abi.type.Tuple;
import dev.klepto.kweb3.abi.type.Uint;
import dev.klepto.kweb3.chain.Chain;
import dev.klepto.kweb3.gas.GasFeeProvider;
import dev.klepto.kweb3.net.ContractClient;
import dev.klepto.kweb3.util.Addresses;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.val;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;

import java.math.BigInteger;
import java.util.List;

import static dev.klepto.kweb3.abi.type.util.Types.*;
import static org.web3j.tx.TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH;
import static org.web3j.tx.TransactionManager.DEFAULT_POLLING_FREQUENCY;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
@Setter
public class Web3jClient extends ContractClient {

    private static final DefaultBlockParameter LATEST_BLOCK = DefaultBlockParameter.valueOf("latest");

    private final Web3jSession session = new Web3jSession(getChain().getRpcUrl(), getChain().getChainId(), getPrivateKey());

    private GasFeeProvider gasFeeProvider;
    private GasFeeProvider defaultGasFeeProvider = new Web3jGasProvider(session.getWeb3j());
    private Uint gasPrice, gasLimit;

    public Web3jClient(Chain chain, String privateKey) {
        super(chain, privateKey);
    }

    @Override
    @SneakyThrows
    public Web3Response _send(Web3Request request) {
        val contractAddress = request.getContractAddress().toHex();
        val data = abiEncode(request);
        val response = request.getFunction().isView() ? sendView(contractAddress, data) : sendTransaction(request, contractAddress, data);

        val contractDomain = Addresses.domain(request.getContractAddress());
        val error = response.getError() != null
                ? new Web3Error(
                "RPC error: {} - {} - {}",
                contractDomain,
                contractAddress,
                response.getError().getMessage()
        ) : null;

        var transactionHash = (String) null;
        var blockNumber = (Uint) null;
        var logs = List.<Web3Response.Log>of();

        if (error == null && response instanceof EthSendTransaction) {
            transactionHash = ((EthSendTransaction) response).getTransactionHash();
            val processor = new PollingTransactionReceiptProcessor(session.getWeb3j(), DEFAULT_POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH);
            val receipt = processor.waitForTransactionReceipt(transactionHash);
            blockNumber = uint256(receipt.getBlockNumber());
            logs = receipt.getLogs().stream().map(log -> new Web3Response.Log(address(log.getAddress()), log.getTopics(), log.getData())).toList();
        }

        var result = (Tuple) null;
        val returnType = request.getFunction().getReturnType();
        if (returnType != null && response.getResult() != null) {
            result = abiDecode(response.getResult(), returnType.getAbiType());
        }

        return new Web3Response(this, request, error, transactionHash, blockNumber, result, logs, null);
    }

    @SneakyThrows
    public Response<String> sendView(String contractAddress, String data) {
        val transaction = Transaction.createEthCallTransaction(getAddress().toHex(), contractAddress, data);
        return session.getWeb3j().ethCall(transaction, LATEST_BLOCK).send();
    }

    @SneakyThrows
    public Response<String> sendTransaction(Web3Request request, String contractAddress, String data) {
        val providerGasPrice = getGasFeeProvider().getLegacyGasFee().getGasPrice();
        val providerMaxFeePerGas = getGasFeeProvider().getGasFee().getMaxFeePerGas();
        val useLegacy = this.gasPrice != null || providerGasPrice.lessThan(providerMaxFeePerGas);
        val gasLimit = this.gasLimit != null ? getGasLimit().getValue() : estimateGas(request).getValue();
        val value = request.getValue().getValue();

        if (useLegacy) {
            val gasPrice = this.gasPrice != null ? this.gasPrice.getValue() : providerGasPrice.getValue();
            return session.getTransactionManager().sendTransaction(gasPrice, gasLimit, contractAddress, data, value);
        } else {
            val maxPriorityFeePerGas = getGasFeeProvider().getGasFee().getMaxPriorityFeePerGas().getValue();
            val maxFeePerGas = getGasFeeProvider().getGasFee().getMaxFeePerGas().getValue();
            return session.getTransactionManager().sendEIP1559Transaction(
                    getChain().getChainId(),
                    maxPriorityFeePerGas,
                    maxFeePerGas,
                    gasLimit,
                    contractAddress,
                    data,
                    value
            );
        }
    }

    @Override
    public GasFeeProvider getGasFeeProvider() {
        return gasFeeProvider != null ? gasFeeProvider : defaultGasFeeProvider;
    }

    public Uint getGasPrice() {
        if (gasPrice != null) {
            return gasPrice;
        }

        return getGasFeeProvider().getLegacyGasFee().getGasPrice();
    }


    @Override
    public Address getAddress() {
        return session.getCredentials() == null ? address(0) : address(session.getCredentials().getAddress());
    }

    @Override
    @SneakyThrows
    public Uint balanceOf(Address address) {
        val balance = session.getWeb3j().ethGetBalance(address.toHex(), LATEST_BLOCK).send().getBalance();
        return uint256(balance);
    }

    @Override
    public Uint estimateGas(Web3Request request) {
        return estimateGas(request, abiEncode(request));
    }

    @SneakyThrows
    private Uint estimateGas(Web3Request request, String data) {
        val nonce = session.getWeb3j()
                .ethGetTransactionCount(getAddress().toHex(), DefaultBlockParameterName.PENDING)
                .send().getTransactionCount();
        val from = getAddress().toHex();
        val to = request.getContractAddress().toHex();
        val transaction = request.getFunction().isView()
                ? Transaction.createEthCallTransaction(from, to, data)
                : Transaction.createFunctionCallTransaction(from, nonce, BigInteger.ZERO, BigInteger.ZERO, to, data);
        val estimateGasResult = session.getWeb3j().ethEstimateGas(transaction).send();
        if (estimateGasResult.getResult() == null) {
            return uint256(0);
        }
        return uint256(estimateGasResult.getAmountUsed()).mul(2);
    }

}
