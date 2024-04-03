package dev.klepto.kweb3.core.contract;

import dev.klepto.kweb3.core.Web3Client;
import dev.klepto.kweb3.core.type.EthAddress;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Creates, caches and manages blockchain contract proxies.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@RequiredArgsConstructor
public class ContractProxies {

    private final Web3Client client;
    private final Map<Class<?>, Map<EthAddress, Web3Contract>> cache = new ConcurrentHashMap<>();

    private AtomicReference<ContractExecutor> executor = new AtomicReference<>(new DefaultContractExecutor());
    private AtomicReference<ContractParser> parser = new AtomicReference<>(new DefaultContractParser());

    /**
     * Creates a blockchain contract proxy that binds java interface functions directly to blockchain transactions.
     *
     * @param type    the type of the contract interface
     * @param address the blockchain address of the contract
     * @return the contract proxy instance
     */
    public <T extends Web3Contract> T createProxy(Class<T> type, EthAddress address) {
        val contracts = cache.computeIfAbsent(type, key -> new ConcurrentHashMap<>());
        val contract = contracts.computeIfAbsent(address, key ->
                (Web3Contract) Proxy.newProxyInstance(
                        ContractProxies.class.getClassLoader(),
                        new Class[]{type},
                        new ContractProxy(this, type, client, address)
                )
        );
        return type.cast(contract);
    }

    /**
     * Sets the default contract executor to be used with all future contract calls.
     *
     * @param executor the contract executor implementation
     */
    public void setExecutor(ContractExecutor executor) {
        this.executor.set(executor);
    }

    /**
     * Gets the current contract executor implementation used by all contract calls.
     *
     * @return the current executor implementation
     */
    public ContractExecutor getExecutor() {
        return executor.get();
    }

    /**
     * Sets the default contract function parser to be used with all future contract calls.
     *
     * @param functions the contract function parser implementation
     */
    public void setParser(ContractParser functions) {
        this.parser.set(functions);
    }

    /**
     * Gets the current contract function parser implementation used by all contract calls.
     *
     * @return the contract function parser implementation
     */
    public ContractParser getParser() {
        return parser.get();
    }

}
