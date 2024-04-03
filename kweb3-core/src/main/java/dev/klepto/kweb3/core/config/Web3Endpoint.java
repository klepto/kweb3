package dev.klepto.kweb3.core.config;

import dev.klepto.kweb3.core.type.EthUint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

import static dev.klepto.kweb3.core.type.EthUint.uint256;

/**
 * Contains information about an endpoint of the network.
 *
 * @param url             the URL of the endpoint
 * @param transport       the transport protocol used to communicate with the endpoint, or {@code null} if not
 *                        specified
 * @param gasCap          the maximum amount of gas defined by the endpoint
 * @param requestCooldown the minimum duration to wait between requests to the endpoint
 * @param requestTimeout  the maximum duration to wait for a response from the endpoint
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public record Web3Endpoint(@NotNull String url, @Nullable Web3Transport transport, @Nullable EthUint gasCap,
                           @Nullable Duration requestCooldown, @Nullable Duration requestTimeout) {

    /**
     * Returns the transport protocol used to communicate with the endpoint. If the transport protocol is not specified,
     * the method will attempt to infer it from the URL prefix.
     *
     * @return the transport protocol used to communicate with the endpoint
     */
    public Web3Transport transport() {
        if (transport != null) {
            return transport;
        } else if (url.startsWith("http://") || url.startsWith("https://")) {
            return Web3Transport.HTTP;
        } else if (url.startsWith("ws://") || url.startsWith("wss://")) {
            return Web3Transport.WEBSOCKET;
        }
        return Web3Transport.IPC;
    }

    /**
     * Returns the maximum amount of gas defined by the endpoint. If the gas cap is not specified, the method will
     * return a default value of <code></code>50,000,000</code>.
     *
     * @return the maximum amount of gas defined by the endpoint
     */
    public EthUint gasCap() {
        if (gasCap == null) {
            return uint256(50_000_000);
        }
        return gasCap;
    }

    /**
     * Returns a new builder for {@link Web3Endpoint} with the same configuration as this instance.
     *
     * @return a new builder for {@link Web3Endpoint}
     */
    public Builder toBuilder() {
        return new Builder()
                .url(url)
                .transport(transport)
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

        private Web3Transport transport = null;
        private String url = null;
        private EthUint gasCap = null;
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
            return new Web3Endpoint(url, transport, gasCap, requestCooldown, requestTimeout);
        }
    }

}