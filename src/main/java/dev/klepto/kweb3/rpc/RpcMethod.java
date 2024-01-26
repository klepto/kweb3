package dev.klepto.kweb3.rpc;

import dev.klepto.kweb3.Web3Result;

/**
 * Indicates that class contains an ethereum RPC method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcMethod {

    /**
     * Sends a new RPC request using underlying RPC client.
     *
     * @param request the rpc request object
     * @return a {@link Web3Result} containing rpc response object that will be completed asynchronously
     */
    Web3Result<RpcResponse> request(RpcRequest request);

}
