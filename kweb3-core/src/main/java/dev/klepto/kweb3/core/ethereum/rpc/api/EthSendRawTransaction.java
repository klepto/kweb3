package dev.klepto.kweb3.core.ethereum.rpc.api;

import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.ethereum.rpc.RpcMessage;
import lombok.val;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of Ethereum RPC API <code>eth_sendRawTransaction</code> method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthSendRawTransaction extends RpcApi {

    /**
     * Submits a pre-signed transaction for broadcast to the Ethereum network.
     *
     * @param data the signed transaction data (typically signed with a library, using your private key)
     * @return the transaction hash, or the zero hash if the transaction is not yet available
     */
    @NotNull
    default Web3Result<String> ethSendRawTransaction(@NotNull String data) {
        val request = new RpcMessage.RequestMessage()
                .withMethod("eth_sendRawTransaction")
                .withParams(new TransactionParameter(data));

        return request(request)
                .map(RpcMessage.ResponseMessage::resultAsString);
    }

    /**
     * Represents <code>eth_sendRawTransaction</code> request object.
     *
     * @param data the signed transaction data (typically signed with a library, using your private key)
     */
    record TransactionParameter(@NotNull String data) {
    }

}
