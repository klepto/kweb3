package dev.klepto.kweb3.core.rpc;

import lombok.With;
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
public record RpcResponse(String jsonrpc, long id, @Nullable String result, @Nullable Error error) {

    /**
     * Creates a new rpc response using default values.
     */
    public RpcResponse() {
        this(RpcApi.JSON_VERSION, 1, null, null);
    }

    /**
     * Represents RPC response error.
     *
     * @param code    the error code
     * @param message the error message
     */
    public record Error(int code, String message) {
    }

}