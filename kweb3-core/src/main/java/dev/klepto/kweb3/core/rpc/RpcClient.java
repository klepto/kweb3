package dev.klepto.kweb3.core.rpc;

import dev.klepto.kweb3.core.Web3Error;
import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.config.Web3Endpoint;
import dev.klepto.kweb3.core.rpc.protocol.RpcMessage;
import dev.klepto.kweb3.core.rpc.protocol.RpcProtocol;
import dev.klepto.kweb3.core.rpc.protocol.RpcRequest;
import dev.klepto.kweb3.core.rpc.protocol.RpcResponse;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * Represents Ethereum RPC API client, without exposing underlying protocol.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public abstract class RpcClient implements RpcProtocol {

    private final AtomicLong requestId = new AtomicLong(1);
    private final Map<Long, Web3Result<RpcResponse>> results = new ConcurrentHashMap<>();
    private final Set<Consumer<RpcMessage>> messageCallbacks = ConcurrentHashMap.newKeySet();

    /**
     * Gets called before sending out {@link RpcRequest}. Used to pre-process rpc request, for example attaching unique
     * request id.
     *
     * @param request the rpc request to process
     * @param result  the rpc response result that is yet to be completed
     * @return the processed rpc request
     */
    public RpcRequest beforeRequest(RpcRequest request, Web3Result<RpcResponse> result) {
        request = request.withId(requestId.incrementAndGet());
        results.put(request.id(), result);
        return request;
    }

    /**
     * Sends a new RPC request using implemented protocol.
     *
     * @param request the rpc request object
     * @return a {@link Web3Result} containing rpc response object that will be completed asynchronously
     */
    public abstract @NotNull Web3Result<RpcResponse> request(@NotNull RpcRequest request);

    /**
     * Called when new RPC message is received.
     *
     * @param message the rpc message
     */
    public void message(String message) {
        val response = RpcProtocol.decode(message);
        if (response instanceof RpcResponse) {
            response((RpcResponse) response);
        }
        messageCallbacks.forEach(callback -> callback.accept(response));
    }

    /**
     * Called when new RPC response is received.
     *
     * @param response the rpc response
     */
    public void response(RpcResponse response) {
        val result = results.remove(response.id());
        if (result == null) {
            return;
        }

        if (response.error() != null) {
            result.completeExceptionally(new Web3Error("RPC error occurred: {}", response.error().message()));
            return;
        }

        result.complete(response);
    }

    /**
     * Called to indicate that RPC requests need to be cancelled, usually because of IO error.
     */
    public void cancel() {
        results.values().forEach(Web3Result::cancel);
        results.clear();
    }

    /**
     * Adds a new message callback to this RPC client.
     *
     * @param callback the message callback to add
     */
    public void addMessageCallback(Consumer<RpcMessage> callback) {
        messageCallbacks.add(callback);
    }

    /**
     * Removes a message callback from this RPC client.
     *
     * @param callback the message callback to remove
     */
    public void removeMessageCallback(Consumer<RpcMessage> callback) {
        messageCallbacks.remove(callback);
    }

    /**
     * Creates a new RPC API instance for the specified transport and URL.
     *
     * @param endpoint the endpoint to create the RPC API for
     * @return a new RPC API instance
     */
    public static RpcClient create(Web3Endpoint endpoint) {
        return switch (endpoint.transport()) {
            case WEBSOCKET -> new WebscoketRpcClient(endpoint);
            default -> throw new Web3Error("Unsupported transport: {}", endpoint.transport());
        };
    }

}
