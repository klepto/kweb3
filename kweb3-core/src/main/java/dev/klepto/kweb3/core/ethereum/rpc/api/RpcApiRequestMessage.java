package dev.klepto.kweb3.core.ethereum.rpc.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.With;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents ethereum JSON RPC request body.
 *
 * @param jsonrpc the json rpc version
 * @param id      the unique identifier of this request
 * @param method  the json rpc method
 * @param params  the json rpc method parameters
 */
@With
public record RpcApiRequestMessage(@NotNull String jsonrpc,
                                   @Nullable Long id,
                                   @NotNull String method,
                                   @NotNull JsonElement params) implements RpcApiMessage {
    /**
     * Creates a new rpc request using default values.
     */
    public RpcApiRequestMessage() {
        this(JSON_VERSION, -1L, "", new JsonArray());
    }

    /**
     * Parameters builder that uses var-args for less-verbose request building.
     *
     * @param params the request parameter objects
     * @return a new request containing given parameter objects
     */
    public RpcApiRequestMessage withParams(Object... params) {
        return new RpcApiRequestMessage(jsonrpc, id, method, GSON.toJsonTree(params));
    }
}