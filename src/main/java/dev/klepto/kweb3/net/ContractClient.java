package dev.klepto.kweb3.net;

import dev.klepto.kweb3.Web3Client;
import dev.klepto.kweb3.Web3Request;
import dev.klepto.kweb3.Web3Response;
import dev.klepto.kweb3.abi.AbiDecoder;
import dev.klepto.kweb3.abi.AbiEncoder;
import dev.klepto.kweb3.abi.HeadlongCodec;
import dev.klepto.kweb3.abi.type.AbiType;
import dev.klepto.kweb3.abi.type.Address;
import dev.klepto.kweb3.abi.type.Tuple;
import dev.klepto.kweb3.chain.Chain;
import dev.klepto.kweb3.contract.Contract;
import dev.klepto.kweb3.contract.ContractProxy;
import dev.klepto.kweb3.util.multicall.MulticallContract;
import dev.klepto.kweb3.util.multicall.MulticallBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import lombok.Setter;
import lombok.experimental.Delegate;
import lombok.val;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Setter
@Getter
@RequiredArgsConstructor
public abstract class ContractClient implements Web3Client {

    private final Chain chain;
    private final String privateKey;

    private volatile boolean logging;
    private MulticallContract multicallContract;

    private final Lock logsLock = new ReentrantLock();

    private final List<Web3Request> logs = new ArrayList<>();

    private final Map<Class<?>, Map<Address, Contract>> contractCache = new ConcurrentHashMap<>();

    @Delegate private final AbiEncoder encoder = new HeadlongCodec();
    @Delegate private final AbiDecoder decoder = new HeadlongCodec();

    @Override
    public <T extends Contract> T contract(Class<T> type, Address address) {
        val contracts = contractCache.computeIfAbsent(type, key -> new ConcurrentHashMap<>());
        val contract = contracts.computeIfAbsent(address, key ->
                (Contract) Proxy.newProxyInstance(
                        ContractClient.class.getClassLoader(),
                        new Class[]{type},
                        new ContractProxy(this, address)
                )
        );

        return type.cast(contract);
    }

    @Override
    public Web3Response send(Web3Request request) {
        if (isLogging()) {
            try {
                logsLock.lock();
                logs.add(request);
            } finally {
                logsLock.unlock();
            }
            return null;
        }

        return _send(request);
    }

    public abstract Web3Response _send(Web3Request request);

    @Override
    public List<Web3Request> getLogs(Runnable runnable) {
        logsLock.lock();
        logging = true;
        try {
            logs.clear();
            runnable.run();
            return new ArrayList<>(logs);
        } finally {
            logging = false;
            logsLock.unlock();
        }
    }

    public String abiEncode(Web3Request request) {
        val function = request.getFunction();
        val parameters = request.getParameters();
        val parametersType = function.getParametersType().getAbiType();
        return function.getHash() + encode(parameters, parametersType);
    }

    @Override
    public String abiEncode(Tuple value, AbiType type) {
        return encoder.encode(value, type);
    }

    @Override
    public Tuple abiDecode(String abi, AbiType type) {
        return decoder.decode(abi, type);
    }

    @Override
    public MulticallBuilder multicall() {
        return new MulticallBuilder(getMulticallContract());
    }
}
