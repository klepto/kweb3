package dev.klepto.kweb3.core.rpc;

import com.google.common.util.concurrent.Uninterruptibles;
import dev.klepto.kweb3.core.Web3Error;
import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.config.Web3Endpoint;
import dev.klepto.kweb3.core.rpc.io.WebsocketConnection;
import dev.klepto.kweb3.core.rpc.protocol.RpcRequest;
import dev.klepto.kweb3.core.rpc.protocol.RpcResponse;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implementation of {@link RpcClient} using websocket protocol.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Slf4j
@RequiredArgsConstructor
public class WebscoketRpcClient extends RpcClient {

    /**
     * The endpoint that this client is connecting to.
     */
    @NotNull
    private final Web3Endpoint endpoint;

    /**
     * Thread-safe reference to current {@link WebsocketConnection}.
     */
    private final AtomicReference<WebsocketConnection> websocket = new AtomicReference<>();

    /**
     * The scheduler used to schedule reconnection attempts and request cooldown.
     */
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    /**
     * Creates or gets currently active {@link WebsocketConnection} in a thread-safe manner.
     *
     * @return the websocket client instance
     */
    @Synchronized
    public WebsocketConnection getWebsocket() {
        val current = websocket.get();
        if (current != null && !current.isClosed()) {
            return current;
        }

        val connection = new WebsocketConnection(endpoint.url());
        connection.setMessageCallback(this::message);
        connection.setCloseCallback(this::close);
        websocket.set(connection);
        return connection;
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
            val processedRequest = beforeRequest(request, result);
            scheduler.submit(() -> {
                try {
                    getWebsocket().send(processedRequest.serialize());
                } catch (Exception cause) {
                    result.completeExceptionally(new Web3Error(cause));
                }

                if (endpoint.requestCooldown() != null) {
                    Uninterruptibles.sleepUninterruptibly(endpoint.requestCooldown());
                }
            });
            return result;
        } catch (Exception cause) {
            throw new Web3Error(cause);
        }
    }

    /**
     * Closes this client and releases all resources.
     */
    private void close() {
        cancel();
    }

}
