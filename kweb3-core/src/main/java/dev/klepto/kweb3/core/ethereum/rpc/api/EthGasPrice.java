package dev.klepto.kweb3.core.ethereum.rpc.api;

import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.ethereum.rpc.RpcMessage;
import lombok.val;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of Ethereum RPC API <code>eth_gasPrice</code> method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthGasPrice extends RpcApi {

    /**
     * Returns the current gas price in wei.
     *
     * @return the hexadecimal value of the current gas price in wei
     */
    @NotNull
    default Web3Result<String> ethGasPrice() {
        val request = new RpcMessage.RequestMessage()
                .withMethod("eth_gasPrice");
        return request(request)
                .map(RpcMessage.ResponseMessage::resultAsString);
    }

}
