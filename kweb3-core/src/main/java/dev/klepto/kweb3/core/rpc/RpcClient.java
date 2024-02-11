package dev.klepto.kweb3.core.rpc;

import dev.klepto.kweb3.core.Web3Error;
import dev.klepto.kweb3.core.Web3Result;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents Ethereum RPC API client, without exposing underlying protocol. Available protocols: HTTP, WS and IPC.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public abstract class RpcClient implements RpcApi {

    private final AtomicLong requestId = new AtomicLong(1);
    private final Map<Long, Web3Result<RpcResponse>> results = new ConcurrentHashMap<>();

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

}
