package dev.klepto.kweb3.core.rpc.protocol.api;

import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.rpc.protocol.RpcMethod;
import dev.klepto.kweb3.core.rpc.protocol.RpcRequest;
import dev.klepto.kweb3.core.rpc.protocol.RpcResponse;
import lombok.val;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of Ethereum RPC API <code>eth_blockNumber</code> method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthBlockNumber extends RpcMethod {

    /**
     * Returns the current latest block number.
     *
     * @return a hexadecimal of an integer representing the current block number the client is on
     */
    @NotNull
    default Web3Result<String> ethBlockNumber() {
        val request = new RpcRequest()
                .withMethod("eth_blockNumber");
        return request(request)
                .map(RpcResponse::resultAsString);
    }

}
