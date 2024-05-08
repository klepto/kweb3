package dev.klepto.kweb3.core.contract;

import dev.klepto.kweb3.core.Web3Client;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Proxy;

/**
 * Provides contract proxy instances.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@RequiredArgsConstructor
public class ContractProxyProvider implements ContractProvider {

    /**
     * Creates a blockchain contract proxy that binds java interface functions directly to blockchain transactions.
     *
     * @param type    the type of the contract interface
     * @param address the blockchain address of the contract
     * @return the contract proxy instance
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public <T extends Web3Contract> T provide(@NotNull Web3Client client,
                                              @NotNull Class<T> type,
                                              @NotNull EthAddress address) {
        return (T) Proxy.newProxyInstance(
                ContractProxyProvider.class.getClassLoader(),
                new Class[]{type},
                new ContractProxy(client, type, address)
        );
    }
}
