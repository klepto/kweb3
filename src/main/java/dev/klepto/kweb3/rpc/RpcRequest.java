package dev.klepto.kweb3.rpc;

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

    public RpcRequest() {
        this("2.0", 1, null, null);
    }
    
    public RpcRequest withParams(Object... params) {
        return new RpcRequest(jsonrpc, id, method, params);
    }

}