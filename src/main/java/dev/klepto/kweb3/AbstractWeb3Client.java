package dev.klepto.kweb3;

import dev.klepto.kweb3.contract.Contract;
import dev.klepto.kweb3.contract.ContractProxy;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.sized.Uint256;
import lombok.Getter;
import lombok.val;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public abstract class AbstractWeb3Client implements Web3Client {

    @Getter private boolean logging;
    private final List<Web3Request> logs = new ArrayList<>();
    private final Map<Class<?>, Map<Address, Contract>> contractCache = new HashMap<>();

    @Override
    public <T extends Contract> T getContract(Class<T> type, Address address) {
        val contracts = contractCache.computeIfAbsent(type, key -> new HashMap<>());
        val contract = contracts.computeIfAbsent(address, key ->
                (Contract) Proxy.newProxyInstance(
                        AbstractWeb3Client.class.getClassLoader(),
                        new Class[]{type},
                        new ContractProxy(this, address)
                )
        );

        return type.cast(contract);
    }

    @Override
    public Web3Response send(Web3Request request) {
        if (isLogging()) {
            logs.add(request);
            return null;
        }

        return request(request);
    }

    public abstract Web3Response request(Web3Request request);

    @Override
    public List<String> abiEncode(Runnable runnable) {
        return getLogs(runnable).stream().map(this::abiEncode).toList();
    }

    @Override
    public List<Uint256> estimateGas(Runnable runnable) {
        return getLogs(runnable).stream().map(this::estimateGas).toList();
    }

    @Override
    public List<Web3Request> getLogs(Runnable runnable) {
        logs.clear();
        logging = true;
        runnable.run();
        logging = false;
        return new ArrayList<>(logs);
    }

}
