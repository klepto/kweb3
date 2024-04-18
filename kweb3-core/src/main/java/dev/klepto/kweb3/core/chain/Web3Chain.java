package dev.klepto.kweb3.core.chain;

import lombok.Builder;
import org.jetbrains.annotations.NotNull;

/**
 * Describes a web3 chain/network.
 *
 * @param name     the name of the chain
 * @param chainId  the chain ID of the chain
 * @param testnet  whether the chain is a testnet
 * @param currency the default currency of the chain
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Builder
public record Web3Chain(@NotNull String name,
                        long chainId,
                        boolean testnet,
                        @NotNull Currency currency) {

    /**
     * Returns a new builder with the same properties as this chain.
     *
     * @return a new builder with the same properties as this chain
     */
    public Web3ChainBuilder toBuilder() {
        return new Web3ChainBuilder()
                .name(name)
                .chainId(chainId)
                .testnet(testnet)
                .currency(currency);
    }

    /**
     * Describes the default currency of the network.
     *
     * @param name     the name of the currency
     * @param symbol   the symbol of the currency
     * @param decimals the number of decimals the currency uses
     */
    public record Currency(@NotNull String name,
                           @NotNull String symbol,
                           int decimals) {
    }

}
