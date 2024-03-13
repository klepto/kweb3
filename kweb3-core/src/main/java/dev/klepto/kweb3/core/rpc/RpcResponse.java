package dev.klepto.kweb3.core.rpc;

import com.google.gson.JsonElement;
import lombok.With;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents ethereum JSON RPC response body.
 *
 * @param jsonrpc the json rpc version
 * @param id      the unique identifier of the request this response is responding to
 * @param result  the response result or null if error occurred
 * @param error   the error of the request or null if no error occurred
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public record RpcResponse(String jsonrpc, long id, @Nullable JsonElement result, @Nullable Error error) {

    /**
     * Creates a new rpc response using default values.
     */
    public RpcResponse() {
        this(RpcApi.JSON_VERSION, 1, null, null);
    }

    /**
     * Returns the result as string.
     *
     * @return the result as string
     */
    @Nullable
    public String resultAsString() {
        if (result == null) {
            return null;
        }
        return result.toString();
    }

    /**
     * Represents RPC response error.
     *
     * @param code    the error code
     * @param message the error message
     */
    public record Error(int code, @NotNull String message) {
    }

}