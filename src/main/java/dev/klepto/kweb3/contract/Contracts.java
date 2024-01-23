package dev.klepto.kweb3.contract;

import dev.klepto.kweb3.Web3Client;
import dev.klepto.kweb3.type.EthAddress;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Creates and manages blockchain contract proxies.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@RequiredArgsConstructor
public class Contracts {

    private final Web3Client client;
    private final Map<Class<?>, Map<EthAddress, Contract>> cache = new ConcurrentHashMap<>();

    /**
     * Creates a blockchain contract proxy that binds java interface functions directly to blockchain transactions.
     *
     * @param type    the type of the contract interface
     * @param address the blockchain address of the contract
     * @return the contract proxy instance
     */
    public <T extends Contract> T createProxy(Class<T> type, EthAddress address) {
        val contracts = cache.computeIfAbsent(type, key -> new HashMap<>());
        val contract = contracts.computeIfAbsent(address, key ->
                (Contract) Proxy.newProxyInstance(
                        Contracts.class.getClassLoader(),
                        new Class[]{type},
                        new ContractProxy(type, client, address)
                )
        );
        return type.cast(contract);
    }

}
