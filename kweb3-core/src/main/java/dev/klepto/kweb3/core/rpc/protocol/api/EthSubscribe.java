package dev.klepto.kweb3.core.rpc.protocol.api;

import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.rpc.protocol.RpcMethod;
import dev.klepto.kweb3.core.rpc.protocol.RpcRequest;
import dev.klepto.kweb3.core.rpc.protocol.RpcResponse;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Implementation of Ethereum RPC API <code>eth_subscribe</code> and <code>eth_unsubscribe</code> methods.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthSubscribe extends RpcMethod {

    /**
     * Subscribes to specific events on the Ethereum network, such as new blocks, new pending transactions, or changes
     * in the state of an account. When an event occurs, a notification is sent to the client with the corresponding
     * data. To stop receiving notifications, the client can unsubscribe using <code>eth_unsubscribe</code>.
     *
     * @param method  the subscription method, <code>newHeads</code>, <code>logs</code>,
     *                <code>newPendingTransactions</code> or <code>syncing</code>
     * @param address the smart contract address filter or <code>null</code>, only applicable to <code>logs</code>
     *                method
     * @param topics  the array of topics filters or <code>null</code>, only applicable to <code>logs</code> method
     * @return a unique subscription id that can be used to unsubscribe or identify incoming notifications.
     */
    @NotNull
    default Web3Result<String> ethSubscribe(@NotNull String method,
                                            @Nullable String address,
                                            @Nullable String[] topics) {
        val hasParameters = address != null || topics != null;
        val parameters = hasParameters ? List.of(method, new Parameters(address, topics)) : method;
        val request = new RpcRequest()
                .withMethod("eth_subscribe")
                .withMultipleParams(parameters);
        return request(request)
                .map(RpcResponse::resultAsString);
    }

    /**
     * Unsubscribes from specific events on the Ethereum network, to which the client has been previously subscribed
     * using <code>eth_subscribe</code>. The client must provide the <code>subscriptionId</code> obtained from
     * <code>eth_subscribe</code> to stop receiving notifications for the corresponding event.
     *
     * @param subscriptionId the unique subscription id obtained from <code>eth_subscribe</code>
     * @return <code>true</code> if the unsubscription was successful, <code>false</code> if subscription did not exist
     */
    @NotNull
    default Web3Result<Boolean> ethUnsubscribe(@NotNull String subscriptionId) {
        val request = new RpcRequest()
                .withMethod("eth_unsubscribe");
        return request(request)
                .map(response -> response.resultAs(boolean.class));
    }

    /**
     * Container for <code>eth_subscribe</code> request parameters.
     *
     * @param address the smart contract address filter
     * @param topics  the array of topics filters
     */
    record Parameters(@Nullable String address,
                      @Nullable String[] topics) {
    }


}
