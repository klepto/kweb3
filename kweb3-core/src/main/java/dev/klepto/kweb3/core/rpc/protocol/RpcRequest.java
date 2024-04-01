package dev.klepto.kweb3.core.rpc.protocol;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.With;
import org.jetbrains.annotations.NotNull;

/**
 * Represents ethereum JSON RPC request body.
 *
 * @param jsonrpc the json rpc version
 * @param id      the unique identifier of this request
 * @param method  the json rpc method
 * @param params  the json rpc method parameters
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public record RpcRequest(@NotNull String jsonrpc,
                         long id,
                         @NotNull String method,
                         @NotNull JsonElement params) implements RpcMessage {

    /**
     * Creates a new rpc request using default values.
     */
    public RpcRequest() {
        this(RpcProtocol.JSON_VERSION, 1, "", new JsonArray());
    }

    /**
     * Parameters builder that uses var-args for less-verbose request building.
     *
     * @param params the request parameter objects
     * @return a new request containing given parameter objects
     */
    public RpcRequest withParams(Object... params) {
        return new RpcRequest(jsonrpc, id, method, RpcProtocol.GSON.toJsonTree(params));
    }

}
