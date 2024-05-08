package dev.klepto.kweb3.core.chain;

import dev.klepto.kweb3.core.Web3Error;
import dev.klepto.kweb3.core.chain.auth.Authorization;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthUint;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

import static dev.klepto.kweb3.core.ethereum.type.primitive.EthUint.uint256;

/**
 * Describes a web3 RPC endpoint that allows for communication with a specific {@link Web3Chain}.
 *
 * @param chain         the chain that the endpoint is connecting to
 * @param url           the URL of the endpoint
 * @param transport     the transport type of the endpoint
 * @param authorization the authorization method of the endpoint
 * @param settings      the settings of the endpoint
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Builder
public record Web3Endpoint(@NotNull Web3Chain chain,
                           @NotNull String url,
                           @Nullable Web3Transport transport,
                           @Nullable Authorization authorization,
                           @NotNull Settings settings) {

    /**
     * Returns the transport type of the endpoint, if not specified, attempts to infer the transport type from the URL.
     *
     * @return the transport type of the endpoint
     */
    public Web3Transport transport() {
        if (transport != null) {
            return transport;
        } else if (url.startsWith("http://") || url.startsWith("https://")) {
            return Web3Transport.HTTP;
        } else if (url.startsWith("ws://") || url.startsWith("wss://")) {
            return Web3Transport.WEBSOCKET;
        }
        throw new Web3Error("Could not infer transport type from URL: {}", url);
    }

    /**
     * Authorizes the endpoint using the authorization method. If endpoint has no authorization method, returns the
     * endpoint as is.
     *
     * @return the authorized endpoint
     */
    public Web3Endpoint authorize() {
        if (authorization == null) {
            return this;
        }
        return authorization.authorize(this);
    }

    /**
     * Returns a new builder with the same properties as this endpoint.
     *
     * @return a new builder with the same properties as this endpoint
     */
    public Web3EndpointBuilder toBuilder() {
        return new Web3EndpointBuilder()
                .chain(chain)
                .url(url)
                .transport(transport)
                .authorization(authorization)
                .settings(settings);
    }

    /**
     * Describes the settings of the endpoint.
     *
     * @param gasLimit        the maximum amount of gas allowed by the endpoint
     * @param pollingInterval the interval between polling periodic requests to the endpoint
     * @param requestCooldown the minimum duration to wait between requests to the endpoint
     * @param requestTimeout  the maximum duration to wait for a response from the endpoint
     */
    public record Settings(
            @Nullable EthUint gasLimit,
            @Nullable Duration pollingInterval,
            @Nullable Duration requestCooldown,
            @Nullable Duration requestTimeout
    ) {

        /**
         * Creates default endpoint settings with no values specified.
         */
        public Settings() {
            this(null, null, null, null);
        }

        /**
         * Returns the gas limit of the endpoint or a default value of <code>50_000_000</code> if gas limit is not
         * specified.
         *
         * @return the gas limit of the endpoint
         */
        public EthUint gasLimit() {
            if (gasLimit == null) {
                return uint256(50_000_000);
            }
            return gasLimit;
        }


        /**
         * Returns the polling interval of the endpoint or a default value of <code>10 seconds</code> if polling
         * interval is not specified.
         *
         * @return the polling interval of the endpoint
         */
        public Duration pollingInterval() {
            if (pollingInterval == null) {
                return Duration.ofSeconds(10);
            }
            return pollingInterval;
        }

    }

}
