package dev.klepto.kweb3.core.rpc.protocol.api;

import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.rpc.protocol.RpcMethod;
import dev.klepto.kweb3.core.rpc.protocol.RpcRequest;
import dev.klepto.kweb3.core.rpc.protocol.RpcResponse;
import lombok.val;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of Ethereum RPC API <code>eth_sendRawTransaction</code> method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthSendRawTransaction extends RpcMethod {

    /**
     * Submits a pre-signed transaction for broadcast to the Ethereum network.
     *
     * @param data the signed transaction data (typically signed with a library, using your private key)
     * @return the transaction hash, or the zero hash if the transaction is not yet available
     */
    @NotNull
    default Web3Result<String> ethSendRawTransaction(@NotNull String data) {
        val request = new RpcRequest()
                .withMethod("eth_sendRawTransaction")
                .withParams(new Request(data));

        return request(request)
                .map(RpcResponse::resultAsString);
    }

    /**
     * Represents <code>eth_sendRawTransaction</code> request object.
     *
     * @param data the signed transaction data (typically signed with a library, using your private key)
     */
    record Request(@NotNull String data) {
    }

}
