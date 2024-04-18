package dev.klepto.kweb3.core.ethereum.rpc.protocol;

import dev.klepto.kweb3.core.Web3Result;
import org.jetbrains.annotations.NotNull;

public interface RpcMethod {

    /**
     * Sends a new RPC request using underlying RPC client.
     *
     * @param request the rpc request object
     * @return a {@link Web3Result} containing rpc response object that will be completed asynchronously
     */
    @NotNull
    Web3Result<RpcResponse> request(@NotNull RpcRequest request);

}
