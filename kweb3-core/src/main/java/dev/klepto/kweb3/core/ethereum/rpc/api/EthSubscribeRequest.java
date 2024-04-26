package dev.klepto.kweb3.core.ethereum.rpc.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Implementation of Ethereum RPC API <code>eth_subscribe</code> method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
@RequiredArgsConstructor
public class EthSubscribeRequest extends RpcApiRequest {

    private final @NotNull String method;
    private final @Nullable String address;
    private final @Nullable String[] topics;

    /**
     * Encodes the request object into an API message.
     *
     * @return the api message
     */
    @Override
    public RpcApiRequestMessage encode() {
        val hasParameters = address != null || topics != null;
        val parameters = hasParameters ? List.of(method, new SubscribeParameters(address, topics)) : method;
        return new RpcApiRequestMessage()
                .withMethod("eth_subscribe")
                .withParams(parameters);
    }

    /**
     * Container for <code>eth_subscribe</code> request parameters.
     *
     * @param address the smart contract address filter
     * @param topics  the array of topics filters
     */
    private record SubscribeParameters(@Nullable String address,
                                       @Nullable String[] topics) {
    }
}
