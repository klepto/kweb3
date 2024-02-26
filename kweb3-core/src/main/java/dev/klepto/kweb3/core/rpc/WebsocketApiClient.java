package dev.klepto.kweb3.core.rpc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.klepto.kweb3.core.Web3Error;
import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.config.Web3Endpoint;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Implementation of {@link RpcClient} using websocket protocol.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Slf4j
@RequiredArgsConstructor
public class WebsocketApiClient extends RpcClient {

    /**
     * {@link Gson} instance for json encoding/decoding.
     */
    private static final Gson GSON = new Gson();

    /**
     * The endpoint that this client is connecting to.
     */
    @NotNull
    private final Web3Endpoint endpoint;

    /**
     * Thread-safe reference to current {@link WebsocketClient}.
     */
    private final AtomicReference<WebsocketClient> websocketClient = new AtomicReference<>();

    /**
     * Creates or gets currently active {@link WebsocketClient} in a thread-safe manner.
     *
     * @return the websocket client instance
     */
    @Synchronized
    public WebsocketClient getWebsocketClient() {
        val current = websocketClient.get();
        if (current != null && !current.isClosed()) {
            return current;
        }

        val client = new WebsocketClient(endpoint.url(), this::onMessage, this::onClose);
        websocketClient.set(client);
        return client;
    }

    /**
     * Sends a new RPC request using websocket protocol.
     *
     * @param request the rpc request object
     * @return a {@link Web3Result} containing rpc response object that will be completed asynchronously
     */
    @Override
    public @NotNull Web3Result<RpcResponse> request(@NotNull RpcRequest request) {
        try {
            val result = new Web3Result<RpcResponse>();
            request = beforeRequest(request, result);
            getWebsocketClient().send(GSON.toJson(request));
            return result;
        } catch (Exception cause) {
            throw new Web3Error(cause);
        }
    }

    /**
     * Callback function for {@link WebsocketClient#onMessage(String)}.
     *
     * @param message the utf-8 decoded message that was received
     */
    private void onMessage(String message) {
        val json = GSON.fromJson(message, JsonObject.class);
        if (!json.has("id")) {
            log.warn("Unknown RPC message: {}", message.trim());
            return;
        }

        val response = GSON.fromJson(json, RpcResponse.class);
        response(response);
    }

    /**
     * Callback function for {@link WebsocketClient#onClose(int, String, boolean)}.
     */
    private void onClose() {
        cancel();
    }

}
