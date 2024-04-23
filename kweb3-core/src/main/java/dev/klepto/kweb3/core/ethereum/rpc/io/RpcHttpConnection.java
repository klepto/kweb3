package dev.klepto.kweb3.core.ethereum.rpc.io;

import dev.klepto.kweb3.core.chain.Web3Endpoint;
import kong.unirest.core.Config;
import kong.unirest.core.ContentType;
import kong.unirest.core.UnirestInstance;
import lombok.val;

/**
 * Implementation of {@link RpcConnection} for HTTP connections.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class RpcHttpConnection extends RpcConnection {

    private final UnirestInstance unirest = new UnirestInstance(new Config());

    /**
     * Constructs new {@link RpcHttpConnection} for the specified endpoint.
     *
     * @param endpoint the endpoint
     */
    public RpcHttpConnection(Web3Endpoint endpoint) {
        super(endpoint);
    }

    /**
     * Sends the message to the endpoint using asynchronous {@link kong.unirest.core.Unirest} client.
     *
     * @param message the message
     */
    @Override
    public void sendDirect(String message) {
        val endpoint = getAuthorizedEndpoint();
        var request = unirest.post(endpoint.url())
                .contentType(ContentType.APPLICATION_JSON)
                .body(message);

        val timeout = endpoint.settings().requestTimeout();
        if (timeout != null) {
            request = request.connectTimeout((int) timeout.toMillis());
        }

        request.asStringAsync().whenComplete((response, throwable) -> {
            if (response != null) {
                onMessage(response.getBody());
            }
            if (throwable != null) {
                onError(throwable);
            }
        });
    }

}
