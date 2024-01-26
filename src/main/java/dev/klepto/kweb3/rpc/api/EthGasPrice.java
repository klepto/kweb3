package dev.klepto.kweb3.rpc.api;

import dev.klepto.kweb3.Web3Result;
import dev.klepto.kweb3.rpc.RpcMethod;
import dev.klepto.kweb3.rpc.RpcRequest;
import dev.klepto.kweb3.rpc.RpcResponse;
import lombok.val;

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
    default Web3Result<String> ethGasPrice() {
        val request = new RpcRequest()
                .withMethod("eth_gasPrice");

        return request(request)
                .map(RpcResponse::result);
    }

}
