package dev.klepto.kweb3.core.contract;

import dev.klepto.kweb3.core.Web3Client;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Provides contract proxy creation and management.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@RequiredArgsConstructor
public class ContractProxyProvider {

    private final Web3Client client;
    private final Map<Class<?>, Map<EthAddress, Web3Contract>> contracts = new ConcurrentHashMap<>();
    private AtomicReference<ContractExecutor> executor = new AtomicReference<>(new ReflectionContractExecutor());
    private AtomicReference<ContractParser> parser = new AtomicReference<>(new ReflectionContractParser());

    /**
     * Sets the default contract executor to be used with all future contract calls.
     *
     * @param executor the contract executor implementation
     */
    public void setExecutor(@NotNull ContractExecutor executor) {
        this.executor.set(executor);
    }

    /**
     * Gets the current contract executor implementation used by all contract calls.
     *
     * @return the current executor implementation
     */
    @NotNull
    public ContractExecutor getExecutor() {
        return executor.get();
    }

    /**
     * Sets the default contract function parser to be used with all future contract calls.
     *
     * @param functions the contract function parser implementation
     */
    public void setParser(@NotNull ContractParser functions) {
        this.parser.set(functions);
    }

    /**
     * Gets the current contract function parser implementation used by all contract calls.
     *
     * @return the contract function parser implementation
     */
    @NotNull
    public ContractParser getParser() {
        return parser.get();
    }

    /**
     * Creates a blockchain contract proxy that binds java interface functions directly to blockchain transactions.
     *
     * @param type    the type of the contract interface
     * @param address the blockchain address of the contract
     * @return the contract proxy instance
     */
    @NotNull
    public <T extends Web3Contract> T createProxy(@NotNull Class<T> type, @NotNull EthAddress address) {
        val cache = contracts.computeIfAbsent(type, key -> new ConcurrentHashMap<>());
        val contract = cache.computeIfAbsent(address, key ->
                (Web3Contract) Proxy.newProxyInstance(
                        ContractProxyProvider.class.getClassLoader(),
                        new Class[]{type},
                        new ContractProxy(client, type, address)
                )
        );
        return type.cast(contract);
    }
}