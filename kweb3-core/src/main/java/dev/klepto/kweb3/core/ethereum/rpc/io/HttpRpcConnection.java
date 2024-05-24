package dev.klepto.kweb3.core.ethereum.rpc.io;

import dev.klepto.kweb3.core.chain.Web3Endpoint;
import dev.klepto.kweb3.core.ethereum.rpc.RpcMessage;
import kong.unirest.core.*;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Implementation of {@link AuthorizedRpcConnection} for HTTP connections.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class HttpRpcConnection extends AuthorizedRpcConnection {

    /**
     * Constructs a new {@link HttpRpcConnection} for the specified endpoint.
     *
     * @param endpoint        the endpoint
     * @param messageCallback the message callback
     * @param errorCallback   the error callback
     * @param closeCallback   the close callback
     */
    public HttpRpcConnection(@NotNull Web3Endpoint endpoint,
                             @Nullable Consumer<RpcMessage> messageCallback,
                             @Nullable Consumer<Throwable> errorCallback,
                             @Nullable Runnable closeCallback) {
        super(endpoint, messageCallback, errorCallback, closeCallback);
    }

    /**
     * Sends the message to the endpoint using asynchronous {@link kong.unirest.core.Unirest} client.
     *
     * @param message the message
     */
    @Override
    public void send(String message) {
        val endpoint = authorizedEndpoint();
        var request = Unirest.post(endpoint.url())
                .contentType(ContentType.APPLICATION_JSON)
                .body(message);

        val timeout = endpoint.settings().requestTimeout();
        if (timeout != null) {
            request = request.requestTimeout((int) timeout.toMillis());
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
