package dev.klepto.kweb3.core.ethereum.rpc.io;

import dev.klepto.kweb3.core.chain.Web3Endpoint;
import kong.unirest.core.Config;
import kong.unirest.core.ContentType;
import kong.unirest.core.FailedResponse;
import kong.unirest.core.UnirestInstance;
import lombok.val;

import java.io.IOException;

/**
 * Implementation of {@link ScheduledRpcConnection} for HTTP connections.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class HttpRpcConnection extends ScheduledRpcConnection {

    private static final int DEFAULT_TIMEOUT = 60_000_000;
    private final UnirestInstance unirest = new UnirestInstance(new Config());

    /**
     * Constructs new {@link HttpRpcConnection} for the specified endpoint.
     *
     * @param endpoint the endpoint
     */
    public HttpRpcConnection(Web3Endpoint endpoint) {
        super(endpoint);
    }

    /**
     * Sends the message to the endpoint using asynchronous {@link kong.unirest.core.Unirest} client.
     *
     * @param message the message
     */
    @Override
    public void send(String message) {
        val endpoint = authorizedEndpoint();
        var request = unirest.post(endpoint.url())
                .contentType(ContentType.APPLICATION_JSON)
                .body(message);

        val timeout = endpoint.settings().requestTimeout();
        if (timeout != null) {
            request = request.connectTimeout((int) timeout.toMillis());
        } else {
            request = request.connectTimeout(DEFAULT_TIMEOUT);
        }

        request.asStringAsync().whenComplete((response, throwable) -> {
            if (response instanceof FailedResponse<String> failure) {
                errorCallback(new IOException(failure.getStatusText()));
            } else if (throwable != null) {
                errorCallback(throwable);
            } else if (response != null) {
                receive(response.getBody());
            }
        });


    }

}
