package dev.klepto.kweb3.core.config;

import lombok.With;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

import static dev.klepto.kweb3.core.type.EthAddress.address;

/**
 * Contains information about a {@link Web3Network}.
 *
 * @param name      the name of the network
 * @param chainId   the chain id of the network
 * @param testnet   whether the network is a testnet
 * @param addresses the default addresses used within the network
 * @param currency  the default currency of the network
 * @param endpoints the endpoints of the network
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public record Web3Chain(@NotNull String name,
                        long chainId,
                        boolean testnet,
                        @Nullable Addresses addresses,
                        @Nullable Currency currency,
                        @NotNull Web3Endpoint... endpoints) implements Web3Network {

    public Web3Chain(String title,
                     long chainId,
                     boolean testnet,
                     Web3Endpoint... endpoints) {
        this(title, chainId, testnet, null, null, endpoints);
    }

    /**
     * Commonly used chains provided by <a href="https://publicnode.com">PublicNode</a>.
     */
    public interface PublicNode {

        /**
         * Ethereum mainnet chain.
         */
        Web3Chain ETHEREUM = new Web3Chain(
                "Ethereum",
                1,
                false,
                new Addresses(address("0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2"), null),
                new Currency("Ether", "ETH", 18),
                websocketEndpoint("ethereum")
        );

        /**
         * BNB smart chain.
         */
        Web3Chain BSC = new Web3Chain(
                "BNB Smart Chain",
                56,
                false,
                new Addresses(address("0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c"), null),
                new Currency("BNB", "BNB", 18),
                websocketEndpoint("bsc")
        );

        /**
         * Polygon chain.
         */
        Web3Chain POLYGON = new Web3Chain(
                "Polygon",
                137,
                false,
                new Addresses(address("0x0d500B1d8E8eF31E21C99d1Db9A6444d3ADf1270"), null),
                new Currency("Matic", "MATIC", 18),
                websocketEndpoint("polygon-bor")
        );

        /**
         * Avalanche C chain.
         */
        Web3Chain AVALANCHE = new Web3Chain(
                "Avalanche C",
                43114,
                false,
                new Addresses(address("0xB31f66AA3C1e785363F0875A1B74E27b85FD66c7"), null),
                new Currency("AVAX", "AVAX", 18),
                websocketEndpoint("avalanche-c-chain")
        );

        /**
         * Arbitrum One chain.
         */
        Web3Chain ARBITRUM = new Web3Chain(
                "Arbitrum One",
                42161,
                false,
                new Addresses(address("0x82aF49447D8a07e3bd95BD0d56f35241523fBab1"), null),
                new Currency("Ether", "ETH", 18),
                websocketEndpoint("arbitrum-one")
        );

        /**
         * Base chain.
         */
        Web3Chain BASE = new Web3Chain(
                "Base Mainnet",
                8453,
                false,
                new Addresses(address("0x4200000000000000000000000000000000000006"), null),
                new Currency("Ether", "ETH", 18),
                websocketEndpoint("base")
        );

        /**
         * Optimism chain.
         */
        Web3Chain OPTIMISM = new Web3Chain(
                "Optimism",
                10,
                false,
                new Addresses(address("0x4200000000000000000000000000000000000006"), null),
                new Currency("Optimism", "OP", 18),
                websocketEndpoint("optimism")
        );

        /**
         * Constructs <a href="https://publicnode.com">PublicNode</a> websocket endpoint for the specified name.
         *
         * @param name the name of the endpoint
         * @return the websocket endpoint configuration
         */
        static Web3Endpoint websocketEndpoint(String name) {
            return new Web3Endpoint(
                    Web3Transport.WEBSOCKET,
                    "wss://" + name + "-rpc.publicnode.com",
                    Duration.ofMillis(200),
                    null
            );
        }

    }

}
