package dev.klepto.kweb3.core.ethereum.rpc.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of Ethereum RPC API <code>eth_unsubscribe</code> method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
@RequiredArgsConstructor
public class EthUnsubscribeRequest extends RpcApiRequest {

    private final @NotNull String subscriptionId;

    /**
     * Encodes the request object into an API message.
     *
     * @return the api message
     */
    @Override
    public RpcApiRequestMessage encode() {
        return new RpcApiRequestMessage()
                .withMethod("eth_unsubscribe")
                .withParams(subscriptionId);
    }

}
