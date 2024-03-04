package dev.klepto.kweb3.core.config;

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
public record Web3Chain(@NotNull String name,
                        long chainId,
                        boolean testnet,
                        @Nullable Addresses addresses,
                        @Nullable Currency currency,
                        @NotNull Web3Endpoint... endpoints) implements Web3Network {

    /**
     * Returns a new builder for {@link Web3Chain} with the same configuration as this instance.
     *
     * @return a new builder for {@link Web3Chain}
     */
    public Builder toBuilder() {
        return new Builder()
                .name(name)
                .chainId(chainId)
                .testnet(testnet)
                .addresses(addresses)
                .currency(currency)
                .endpoints(endpoints);
    }

    /**
     * Creates a new builder for {@link Web3Chain}.
     *
     * @return a new builder for {@link Web3Chain}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for {@link Web3Chain}.
     */
    public static class Builder {
        private String name;
        private long chainId = 1;
        private boolean testnet = false;
        private Addresses addresses = null;
        private Currency currency = null;
        private Web3Endpoint[] endpoints;

        private Builder() {
        }

        /**
         * Sets the name of the network.
         *
         * @param name the name of the network
         * @return the builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the chain id of the network.
         *
         * @param chainId the chain id of the network
         * @return the builder
         */
        public Builder chainId(long chainId) {
            this.chainId = chainId;
            return this;
        }

        /**
         * Sets whether the network is a testnet.
         *
         * @param testnet true if the network is a testnet
         * @return the builder
         */
        public Builder testnet(boolean testnet) {
            this.testnet = testnet;
            return this;
        }

        /**
         * Sets the default addresses used within the network.
         *
         * @param addresses the default addresses used within the network
         * @return the builder
         */
        public Builder addresses(Addresses addresses) {
            this.addresses = addresses;
            return this;
        }

        /**
         * Sets the default currency of the network.
         *
         * @param currency the default currency of the network
         * @return the builder
         */
        public Builder currency(Currency currency) {
            this.currency = currency;
            return this;
        }

        /**
         * Sets the endpoints of the network.
         *
         * @param endpoints an array containing endpoints of the network
         * @return the builder
         */
        public Builder endpoints(Web3Endpoint... endpoints) {
            this.endpoints = endpoints;
            return this;
        }

        /**
         * Builds a new instance of {@link Web3Chain} from the provided configuration.
         *
         * @return a new instance of {@link Web3Chain}
         */
        public Web3Chain build() {
            return new Web3Chain(name, chainId, testnet, addresses, currency, endpoints);
        }
    }

    /**
     * Commonly used chains provided by <a href="https://publicnode.com">PublicNode</a>.
     */
    public interface PublicNode {

        /**
         * Ethereum mainnet chain.
         */
        Web3Chain ETHEREUM = Web3Chain.builder()
                .name("Ethereum")
                .chainId(1)
                .addresses(new Addresses(address("0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2")))
                .currency(new Currency("Ether", "ETH", 18))
                .endpoints(websocketEndpoint("ethereum"))
                .build();

        /**
         * BNB smart chain.
         */
        Web3Chain BSC = Web3Chain.builder()
                .name("BNB Smart Chain")
                .chainId(56)
                .addresses(new Addresses(address("0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c")))
                .currency(new Currency("BNB", "BNB", 18))
                .endpoints(websocketEndpoint("bsc"))
                .build();

        /**
         * Polygon chain.
         */
        Web3Chain POLYGON = Web3Chain.builder()
                .name("Polygon")
                .chainId(137)
                .addresses(new Addresses(address("0x0d500B1d8E8eF31E21C99d1Db9A6444d3ADf1270")))
                .currency(new Currency("Matic", "MATIC", 18))
                .endpoints(websocketEndpoint("polygon-bor"))
                .build();

        /**
         * Avalanche C chain.
         */
        Web3Chain AVALANCHE = Web3Chain.builder()
                .name("Avalanche C")
                .chainId(43114)
                .addresses(new Addresses(address("0xB31f66AA3C1e785363F0875A1B74E27b85FD66c7")))
                .currency(new Currency("AVAX", "AVAX", 18))
                .endpoints(websocketEndpoint("avalanche-c-chain"))
                .build();

        /**
         * Arbitrum One chain.
         */
        Web3Chain ARBITRUM = Web3Chain.builder()
                .name("Arbitrum One")
                .chainId(42161)
                .addresses(new Addresses(address("0x82aF49447D8a07e3bd95BD0d56f35241523fBab1")))
                .currency(ETHEREUM.currency())
                .endpoints(websocketEndpoint("arbitrum-one"))
                .build();

        /**
         * Base chain.
         */
        Web3Chain BASE = Web3Chain.builder()
                .name("Base Mainnet")
                .chainId(8453)
                .addresses(new Addresses(address("0x4200000000000000000000000000000000000006")))
                .currency(ETHEREUM.currency())
                .endpoints(websocketEndpoint("base"))
                .build();

        /**
         * Optimism chain.
         */
        Web3Chain OPTIMISM = Web3Chain.builder()
                .name("Optimism")
                .chainId(10)
                .addresses(new Addresses(address("0x4200000000000000000000000000000000000006")))
                .currency(new Currency("Optimism", "OP", 18))
                .endpoints(websocketEndpoint("optimism"))
                .build();

        /**
         * Constructs <a href="https://publicnode.com">PublicNode</a> websocket endpoint for the specified name.
         *
         * @param name the name of the endpoint
         * @return the websocket endpoint configuration
         */
        static Web3Endpoint websocketEndpoint(String name) {
            return Web3Endpoint.builder()
                    .transport(Web3Transport.WEBSOCKET)
                    .url("wss://" + name + "-rpc.publicnode.com")
                    .requestTimeout(Duration.ofMillis(200))
                    .build();
        }

    }

}
