package dev.klepto.kweb3.core.config;

import dev.klepto.kweb3.core.type.EthUint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

import static dev.klepto.kweb3.core.type.EthUint.uint256;

/**
 * Contains information about an endpoint of the network.
 *
 * @param transport       the transport protocol used to communicate with the endpoint
 * @param url             the URL of the endpoint
 * @param gasCap          the maximum amount of gas defined by the endpoint
 * @param requestCooldown the minimum duration to wait between requests to the endpoint
 * @param requestTimeout  the maximum duration to wait for a response from the endpoint
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public record Web3Endpoint(@NotNull Web3Transport transport, @NotNull String url, @NotNull EthUint gasCap,
                           @Nullable Duration requestCooldown, @Nullable Duration requestTimeout) {

    /**
     * Returns a new builder for {@link Web3Endpoint} with the same configuration as this instance.
     *
     * @return a new builder for {@link Web3Endpoint}
     */
    public Builder toBuilder() {
        return new Builder()
                .transport(transport)
                .url(url)
                .gasCap(gasCap)
                .requestCooldown(requestCooldown)
                .requestTimeout(requestTimeout);
    }

    /**
     * Creates a new builder for {@link Web3Endpoint}.
     *
     * @return a new builder for {@link Web3Endpoint}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for {@link Web3Endpoint}.
     */
    public static class Builder {

        private Web3Transport transport = Web3Transport.HTTP;
        private String url = null;
        private EthUint gasCap = uint256(50_000_000);
        private Duration requestCooldown = null;
        private Duration requestTimeout = null;

        private Builder() {
        }

        /**
         * Sets the transport protocol used to communicate with the endpoint.
         *
         * @param transport the transport protocol used to communicate with the endpoint
         * @return the builder
         */
        public Builder transport(Web3Transport transport) {
            this.transport = transport;
            return this;
        }

        /**
         * Sets the URL of the endpoint.
         *
         * @param url the URL of the endpoint
         * @return the builder
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * Sets the maximum amount of gas defined by the endpoint.
         *
         * @param gasCap the maximum amount of gas defined by the endpoint
         * @return the builder
         */
        public Builder gasCap(EthUint gasCap) {
            this.gasCap = gasCap;
            return this;
        }

        /**
         * Sets the minimum duration to wait between requests to the endpoint.
         *
         * @param requestCooldown the minimum duration to wait between requests to the endpoint
         * @return the builder
         */
        public Builder requestCooldown(Duration requestCooldown) {
            this.requestCooldown = requestCooldown;
            return this;
        }

        /**
         * Sets the maximum duration to wait for a response from the endpoint.
         *
         * @param requestTimeout the maximum duration to wait for a response from the endpoint
         * @return the builder
         */
        public Builder requestTimeout(Duration requestTimeout) {
            this.requestTimeout = requestTimeout;
            return this;
        }

        /**
         * Builds a new instance of {@link Web3Endpoint} using configuration of this builder.
         *
         * @return a new instance of {@link Web3Endpoint}
         */
        public Web3Endpoint build() {
            return new Web3Endpoint(transport, url, gasCap, requestCooldown, requestTimeout);
        }
    }

}