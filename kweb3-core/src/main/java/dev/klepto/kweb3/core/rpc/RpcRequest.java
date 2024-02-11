package dev.klepto.kweb3.core.rpc;

import lombok.With;

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
public record RpcRequest(String jsonrpc, long id, String method, @With Object[] params) {

    /**
     * Creates a new rpc request using default values.
     */
    public RpcRequest() {
        this(RpcApi.JSON_VERSION, 1, null, null);
    }

    /**
     * Parameters builder that uses var-args for less-verbose request building.
     *
     * @param params the request parameter objects
     * @return a new request containing given parameter objects
     */
    public RpcRequest withParams(Object... params) {
        return new RpcRequest(jsonrpc, id, method, params);
    }

}