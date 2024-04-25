package dev.klepto.kweb3.core.ethereum.rpc.api;

import dev.klepto.kweb3.core.Web3Error;
import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.ethereum.rpc.RpcClient;
import dev.klepto.kweb3.core.ethereum.rpc.RpcMessage;
import dev.klepto.kweb3.core.ethereum.rpc.RpcRequest;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * Marker interface for JSON RPC API.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcApi {

    /**
     * Atomic counter for request IDs.
     */
    AtomicLong REQUEST_ID = new AtomicLong(1);

    /**
     * Sends the specified request to the client and returns the asynchronous result.
     *
     * @param request the request to send
     * @return the result of the request
     */
    default Web3Result<RpcMessage.ResponseMessage> request(@NotNull RpcMessage.RequestMessage request) {
        require(this instanceof RpcClient, "API must be attached to a client.");
        val id = REQUEST_ID.get() == Long.MAX_VALUE
                ? REQUEST_ID.getAndSet(1)
                : REQUEST_ID.getAndIncrement();
        val client = (RpcClient) this;
        val result = new Web3Result<RpcMessage.ResponseMessage>();
        client.request(new ApiRequest(request.withId(id), result));
        return result;
    }

    /**
     * Implementation of standard JSON RPC API request.
     */
    @RequiredArgsConstructor
    class ApiRequest implements RpcRequest {

        private final RpcMessage.RequestMessage message;
        private final Web3Result<RpcMessage.ResponseMessage> result;

        /**
         * Sends this request to the specified {@link RpcClient}.
         *
         * @param client the client to send the request to
         * @return true if the request was successfully sent
         */
        @Override
        public boolean send(@NotNull RpcClient client) {
            return client.send(message);
        }

        /**
         * Checks if the specified message is a response to this request and returns true if request should be marked as
         * completed.
         *
         * @param client  the client that received the message
         * @param message the message to check
         * @return true if this request should be marked as completed
         */
        @Override
        public boolean isComplete(@NotNull RpcClient client, @NotNull RpcMessage message) {
            if (message.id() != this.message.id()) {
                return false;
            }

            require(message instanceof RpcMessage.ResponseMessage, "Received message is not a response.");
            val response = (RpcMessage.ResponseMessage) message;
            if (response.error() != null) {
                result.completeExceptionally(new Web3Error(response.error().message()));
                return true;
            }

            result.complete(response);
            return true;
        }

        /**
         * Called when an error occurs while processing this request. Returns true if the request should be marked as
         * completed.
         *
         * @param client the client that received the error
         * @param error  the error that occurred
         * @return true if error was a Web3Error, false otherwise
         */
        @Override
        public boolean onError(@NotNull RpcClient client, @NotNull Throwable error) {
            if (error instanceof Web3Error) {
                result.completeExceptionally(error);
                return true;
            }
            return false;
        }

    }

}
