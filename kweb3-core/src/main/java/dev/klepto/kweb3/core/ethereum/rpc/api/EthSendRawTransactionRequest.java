package dev.klepto.kweb3.core.ethereum.rpc.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of Ethereum RPC API <code>eth_sendRawTransaction</code> method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
@RequiredArgsConstructor
public class EthSendRawTransactionRequest extends RpcApiRequest {

    private final @NotNull String signedTransactionData;

    /**
     * Encodes the request object into an API message.
     *
     * @return the api message
     */
    @Override
    public RpcApiRequestMessage encode() {
        return new RpcApiRequestMessage()
                .withMethod("eth_sendRawTransaction")
                .withParams(new TransactionParameter(signedTransactionData));
    }

    /**
     * Represents <code>eth_sendRawTransaction</code> request object.
     *
     * @param data the signed transaction data (typically signed with a library, using your private key)
     */
    private record TransactionParameter(@NotNull String data) {
    }

}
