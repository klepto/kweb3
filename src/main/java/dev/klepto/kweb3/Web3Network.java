package dev.klepto.kweb3;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a web3 network/chain.
 */
public interface Web3Network {

    /**
     * Returns the RPC address of HTTP or WebSocket protocol.
     *
     * @return a string containing RPC url
     */
    @NotNull
    String rpcUrl();

    /**
     * Returns the chain id of this network.
     *
     * @return a long that signifies chain id of the network
     */
    long chainId();

    /**
     * Basic {@link Web3Network} implementation that takes network variables as constructor parameters.
     */
    record Basic(@NotNull String rpcUrl, long chainId) implements Web3Network {
    }

}
