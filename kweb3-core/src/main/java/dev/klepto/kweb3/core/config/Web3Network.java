package dev.klepto.kweb3.core.config;

import dev.klepto.kweb3.core.type.EthAddress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents information about a web3 network, such as chain id, testnet status, RPC endpoints etc. Default container
 * implementation can be seen at {@link Web3Chain}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Web3Network {

    /**
     * Returns the name of the network.
     *
     * @return name of the network
     */
    String name();

    /**
     * Returns the chain id of the network.
     *
     * @return chain id of the network
     */
    long chainId();

    /**
     * Returns whether the network is a testnet.
     *
     * @return true if the network is a testnet
     */
    boolean testnet();

    /**
     * Returns the default addresses used within the network.
     *
     * @return the default addresses used within the network
     */
    Addresses addresses();

    /**
     * Returns the default currency of the network.
     *
     * @return the default currency of the network
     */
    Currency currency();

    /**
     * Returns the endpoints of the network.
     *
     * @return an array containing endpoints of the network
     */
    Web3Endpoint[] endpoints();

    /**
     * Contains the default addresses used within the network.
     *
     * @param nativeToken the native token address
     * @param multicall3  the multicall3 address
     */
    record Addresses(@Nullable EthAddress nativeToken,
                     @Nullable EthAddress multicall3) {
    }

    /**
     * Contains information about the default currency of the network.
     *
     * @param name     the name of the currency
     * @param symbol   the symbol of the currency
     * @param decimals the number of decimals the currency uses
     */
    record Currency(@NotNull String name,
                    @NotNull String symbol,
                    int decimals) {
    }

}
