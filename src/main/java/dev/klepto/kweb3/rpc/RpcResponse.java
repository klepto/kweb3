package dev.klepto.kweb3.rpc;

import lombok.With;

/**
 * Represents ethereum JSON RPC response body.
 *
 * @param jsonrpc the json rpc version
 * @param id      the unique identifier of the request this response is responding to
 * @param result  the response result
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public record RpcResponse(String jsonrpc, long id, String result) {

    /**
     * Creates a new rpc response using default values.
     */
    public RpcResponse() {
        this(RpcApi.JSON_VERSION, 1, null);
    }

}