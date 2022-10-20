package dev.klepto.kweb3.web3j;

import dev.klepto.kweb3.*;
import dev.klepto.kweb3.gas.GasFeeProvider;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.sized.Uint256;
import lombok.*;
import org.web3j.abi.*;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static dev.klepto.kweb3.type.sized.Uint256.uint256;
import static org.web3j.tx.TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH;
import static org.web3j.tx.TransactionManager.DEFAULT_POLLING_FREQUENCY;

/**
 * Implementation of {@link Web3Client} using Web3j.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@RequiredArgsConstructor
public class Web3jClient extends AbstractWeb3Client {

    private final String rpcUrl;
    private final long chainId;
    private final String privateKey;
    @Setter private GasFeeProvider gasFeeProvider;

    private Web3jSession createSession() {
        return new Web3jSession(rpcUrl, chainId, privateKey);
    }

    @SneakyThrows
    public Web3Response request(Web3Request request) {
        val session = createSession();
        val function = Web3jEncoder.encodeFunction(request);
        val data = FunctionEncoder.encode(function);
        val to = request.getAddress().toString();
        val defaultBlockParameter = DefaultBlockParameter.valueOf("latest");

        if (request.getFunction().isView()) {
            val response = session.getTransactionManager().sendCall(to, data, defaultBlockParameter);
            val result = Web3jDecoder.decodeResult(response, function.getOutputParameters());
            return new Web3Response(null, request, null, result, Collections.emptyList());
        } else {
            val gasLimit = estimateGas(request, session, data).toBigInteger();
            val gasProvider = getGasFeeProvider();
            val gasFee = gasProvider.getGasFee();
            val legacyGasFee = gasProvider.getLegacyGasFee();
            val useLegacy = gasFee.getMaxFeePerGas().getValue().equals(BigInteger.ZERO)
                    || gasFee.getMaxPriorityFeePerGas().getValue().equals(BigInteger.ZERO)
                    || legacyGasFee.getGasPrice().getValue().compareTo(gasFee.getMaxFeePerGas().getValue()) < 0;
            val value = request.getValue().getValue();

            var response = (EthSendTransaction) null;
            if (useLegacy) {
                val gasPrice = legacyGasFee.getGasPrice().getValue();
                response = session.getTransactionManager().sendTransaction(
                        gasPrice,
                        gasLimit,
                        to,
                        data,
                        value
                );
            } else {
                val maxPriorityFeePerGas = gasFee.getMaxPriorityFeePerGas().getValue();
                val maxFeePerGas = gasFee.getMaxFeePerGas().getValue();
                response = session.getTransactionManager().sendEIP1559Transaction(
                        chainId,
                        maxPriorityFeePerGas,
                        maxFeePerGas,
                        gasLimit,
                        to,
                        data,
                        value
                );
            }

            val processor = new PollingTransactionReceiptProcessor(
                    session.getWeb3j(), DEFAULT_POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH
            );
            val receipt = processor.waitForTransactionReceipt(response.getTransactionHash());
            val events = Web3jDecoder.decodeEvents(request.getEventTypes(), receipt.getLogs());
            var error = (Web3Error) null;
            var result = Collections.emptyList();

            if (response.getError() != null) {
                error = new Web3Error("RPC error: {}", response.getError().getMessage());
            } else {
                result = Web3jDecoder.decodeResult(response.getResult(), function.getOutputParameters());
            }

            return new Web3Response(response.getTransactionHash(), request, error, result, events);
        }
    }

    @Override
    @SneakyThrows
    public Uint256 balanceOf(Address address) {
        val session = createSession();
        val balance = session.getWeb3j()
                .ethGetBalance(address.toString(), DefaultBlockParameter.valueOf("latest"))
                .send();
        return new Uint256(balance.getBalance());
    }

    @Override
    public List<Object> abiDecode(String abi, List<Object> types) {
        val encodedTypes = Web3jEncoder.encodeTypes(types);
        return Web3jDecoder.decodeResult(abi, encodedTypes);
    }

    @Override
    public String abiEncode(Web3Request request) {
        val function = Web3jEncoder.encodeFunction(request);
        return FunctionEncoder.encode(function);
    }

    @Override
    @SneakyThrows
    public Uint256 estimateGas(Web3Request request) {
        val session = createSession();
        val data = abiEncode(request);
        return estimateGas(request, session, data);
    }

    @Override
    public Uint256 estimateGasPrice(Web3Request request) {
        val gasAmount = estimateGas(request);
        val gasPrice = getGasFeeProvider().getLegacyGasFee().getGasPrice();
        return gasAmount.mul(gasPrice);
    }

    @SneakyThrows
    private Uint256 estimateGas(Web3Request request, Web3jSession session, String data) {
        val nonce = session.getWeb3j().ethGetTransactionCount(
                getAddress().toString(), DefaultBlockParameterName.PENDING
        ).send().getTransactionCount();
        val from = getAddress().toString();
        val to = request.getAddress().toString();
        val transaction = request.getFunction().isView()
                ? Transaction.createEthCallTransaction(from, to, data)
                : Transaction.createFunctionCallTransaction(from, nonce, BigInteger.ZERO, BigInteger.ZERO, to, data);
        val estimateGasResult = session.getWeb3j().ethEstimateGas(transaction).send();
        if (estimateGasResult.getResult() == null) {
           return uint256(0);
        }
        return new Uint256(estimateGasResult. getAmountUsed()).mul(1.1);
    }

    public GasFeeProvider getGasFeeProvider() {
        return gasFeeProvider != null ? gasFeeProvider : new Web3jGasProvider(createSession().getWeb3j());
    }

    public Address getAddress() {
        return new Address(Credentials.create(privateKey).getAddress());
    }

}
