package dev.klepto.kweb3.core.rpc.api;

import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.rpc.RpcMethod;
import dev.klepto.kweb3.core.rpc.RpcRequest;
import dev.klepto.kweb3.core.rpc.RpcResponse;
import lombok.val;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of Ethereum RPC API <code>eth_gasPrice</code> method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthGasPrice extends RpcMethod {

    /**
     * Returns the current gas price in wei.
     *
     * @return the hexadecimal value of the current gas price in wei
     */
    @NotNull
    default Web3Result<String> ethGasPrice() {
        val request = new RpcRequest()
                .withMethod("eth_gasPrice");

        return request(request)
                .map(RpcResponse::result);
    }

}
