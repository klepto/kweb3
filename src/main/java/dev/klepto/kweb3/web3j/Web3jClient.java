package dev.klepto.kweb3.web3j;

import dev.klepto.kweb3.Web3Client;
import dev.klepto.kweb3.Web3Error;
import dev.klepto.kweb3.Web3Request;
import dev.klepto.kweb3.Web3Response;
import dev.klepto.kweb3.gas.GasFeeProvider;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.sized.Uint256;
import lombok.*;
import org.web3j.abi.*;
import org.web3j.abi.datatypes.*;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.tx.Contract;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.web3j.tx.TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH;
import static org.web3j.tx.TransactionManager.DEFAULT_POLLING_FREQUENCY;

/**
 * Implementation of {@link Web3Client} using Web3j.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@RequiredArgsConstructor
public class Web3jClient implements Web3Client {

    private final String rpcUrl;
    private final long chainId;
    private final String privateKey;

    private final Web3jEncoder encoder = new Web3jEncoder();
    private final Web3jDecoder decoder = new Web3jDecoder();

    @Setter
    private GasFeeProvider gasFeeProvider;
    private LogMode logMode;
    private List<Object> logs = new ArrayList<>();

    private Web3jSession createSession() {
        return new Web3jSession(rpcUrl, chainId, privateKey);
    }

    @SneakyThrows
    public Web3Response send(Web3Request request) {
        val session = createSession();
        val function = encodeFunction(request);
        val data = FunctionEncoder.encode(function);
        val to = request.getAddress().toString();
        val defaultBlockParameter = DefaultBlockParameter.valueOf("latest");

        if (logMode == LogMode.ABI) {
            appendLog(data);
            return null;
        } else if (logMode == LogMode.GAS) {
            appendLog(estimateGas(request));
            return null;
        }

        if (request.getFunction().isView()) {
            val response = session.getTransactionManager().sendCall(to, data, defaultBlockParameter);
            val result = FunctionReturnDecoder.decode(response, function.getOutputParameters())
                    .stream().map(decoder::decodeValue).collect(Collectors.toList());
            return new Web3Response(null, request, null, result, Collections.emptyList());
        } else {
            val gasLimit = estimateGas(request, session, data).toBigInteger();
            val gasProvider = gasFeeProvider != null ? gasFeeProvider : new Web3jGasProvider(session.getWeb3j());
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
            val events = decodeEvents(request.getEventTypes(), receipt.getLogs());
            var error = (Web3Error) null;
            var result = Collections.emptyList();

            if (response.getError() != null) {
                error = new Web3Error("RPC error: {}", response.getError().getMessage());
            } else {

                result = FunctionReturnDecoder.decode(response.getResult(), function.getOutputParameters())
                        .stream().map(decoder::decodeValue).collect(Collectors.toList());
            }

            return new Web3Response(response.getTransactionHash(), request, error, result, events);
        }
    }

    public List<Web3Response.Event> decodeEvents(List<Web3Request.Event> eventTypes, List<Log> logs) {
        val result = new ArrayList<Web3Response.Event>();
        val encodedEvents = eventTypes.stream().map(this::encodeEvent).collect(Collectors.toList());
        for (var i = 0; i < encodedEvents.size(); i++) {
            val eventType = eventTypes.get(i);
            val encodedEvent = encodedEvents.get(i);
            for (val log : logs) {
                val parameters = Contract.staticExtractEventParameters(encodedEvent, log);
                if (parameters == null) {
                    continue;
                }
                result.add(decodeEvent(eventType, parameters));
            }
        }
        return result;
    }

    private Web3Response.Event decodeEvent(Web3Request.Event eventType, EventValues parameters) {
        val indexQueue = new LinkedList<>(parameters.getIndexedValues());
        val nonIndexQueue = new LinkedList<>(parameters.getNonIndexedValues());
        val values = new ArrayList<>();
        for (var i = 0; i < eventType.getValueTypes().size(); i++) {
            val value = eventType.getIndexedValues()[i] ? indexQueue.poll() : nonIndexQueue.poll();
            values.add(decoder.decodeValue(value));
        }
        return new Web3Response.Event(eventType.getName(), values);
    }

    private Event encodeEvent(Web3Request.Event event) {
        val name = event.getName();
        val parameters = new ArrayList<TypeReference<?>>();
        for (var i = 0; i < event.getValueTypes().size(); i++) {
            val parameter = event.getValueTypes().get(i);
            val indexed = event.getIndexedValues()[i];
            val reference = encoder.encodeType(parameter, indexed);
            parameters.add(reference);
        }
        return new Event(name, parameters);
    }

    private Function encodeFunction(Web3Request request) {
        val parameters = request.getParameters().stream()
                .map(encoder::encodeValue)
                .collect(Collectors.toList());

        val functionReturnTypes = request.getFunction().getReturnTypes();
        val returnTypes = new ArrayList<TypeReference<?>>();
        functionReturnTypes.forEach(type -> {
            returnTypes.add(encoder.encodeType(type, false));
        });

        return new Function(request.getFunction().getName(), parameters, returnTypes);
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
    public String abiEncode(Web3Request request) {
        return FunctionEncoder.encode(encodeFunction(request));
    }

    @Override
    public List<String> abiEncode(Runnable runnable) {
        return captureLogs(LogMode.ABI, runnable, String.class);
    }

    @Override
    @SneakyThrows
    public Uint256 estimateGas(Web3Request request) {
        val session = createSession();
        val data = abiEncode(request);
        return estimateGas(request, session, data);
    }

    @Override
    public List<Uint256> estimateGas(Runnable runnable) {
        return captureLogs(LogMode.GAS, runnable, Uint256.class);
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
        return new Uint256(estimateGasResult.getAmountUsed()).mul(1.1);
    }

    public Address getAddress() {
        return new Address(Credentials.create(privateKey).getAddress());
    }

    private <T> List<T> captureLogs(LogMode logMode, Runnable runnable, Class<T> type) {
        this.logMode = logMode;
        logs.clear();
        runnable.run();
        this.logMode = null;
        return logs.stream().map(type::cast).collect(Collectors.toList());
    }

    private void appendLog(Object object) {
        logs.add(object);
    }

    private enum LogMode {
        ABI, GAS
    }

}
