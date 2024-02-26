package dev.klepto.kweb3.core.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

/**
 * Contains information about an endpoint of the network.
 *
 * @param transport       the transport protocol used to communicate with the endpoint
 * @param url             the URL of the endpoint
 * @param requestCooldown the minimum duration to wait between requests to the endpoint
 * @param requestTimeout  the maximum duration to wait for a response from the endpoint
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public record Web3Endpoint(@NotNull Web3Transport transport,
                           @NotNull String url,
                           @Nullable Duration requestCooldown,
                           @Nullable Duration requestTimeout) {

}