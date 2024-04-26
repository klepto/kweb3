package dev.klepto.kweb3.core.ethereum.rpc.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Implementation of Ethereum RPC API <code>eth_blockNumber</code> method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
@RequiredArgsConstructor
public class EthBlockNumberRequest extends RpcApiRequest {

    /**
     * Encodes the request object into an API message.
     *
     * @return the api message
     */
    @Override
    public RpcApiRequestMessage encode() {
        return new RpcApiRequestMessage()
                .withMethod("eth_blockNumber");
    }

}
