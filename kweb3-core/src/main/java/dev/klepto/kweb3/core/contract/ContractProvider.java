package dev.klepto.kweb3.core.contract;

import dev.klepto.kweb3.core.Web3Client;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress;
import org.jetbrains.annotations.NotNull;


/**
 * Provides smart contract instances.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface ContractProvider {

    /**
     * Provides a smart contract instance for the given contract interface and address.
     *
     * @param client  the web3 client
     * @param type    the contract interface
     * @param address the contract address
     * @return the contract instance
     */
    <T extends Web3Contract> T provide(@NotNull Web3Client client,
                                       @NotNull Class<T> type,
                                       @NotNull EthAddress address);

}
