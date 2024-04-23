package dev.klepto.kweb3.core.ethereum.rpc;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a request to be sent to an <code>ethereum</code> node. Supports arbitrary implementation of requests and
 * their completion mechanism.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcRequest {

    /**
     * Sends this request to the specified {@link RpcClient}.
     *
     * @param client the client to send the request to
     * @return true if the request was successfully sent
     */
    boolean send(@NotNull RpcClient client);

    /**
     * Checks if the specified message is a response to this request and returns true if request should be marked as
     * completed.
     *
     * @param client  the client that received the message
     * @param message the message to check
     * @return true if this request should be marked as completed
     */
    boolean isComplete(@NotNull RpcClient client, @NotNull RpcMessage message);


}
