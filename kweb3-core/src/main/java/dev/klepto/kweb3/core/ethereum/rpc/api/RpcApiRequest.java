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
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@RequiredArgsConstructor
public abstract class RpcApiRequest implements RpcRequest {

    /**
     * Atomic counter for request IDs.
     */
    private static final AtomicLong REQUEST_ID = new AtomicLong(1);

    /**
     * Returns the next request ID.
     *
     * @return the next request ID
     */
    public static long nextRequestId() {
        return REQUEST_ID.get() == Long.MAX_VALUE
                ? REQUEST_ID.getAndSet(1)
                : REQUEST_ID.getAndIncrement();
    }

    private final long id = nextRequestId();
    private final Web3Result<RpcApiResponseMessage> result = new Web3Result<>();

    /**
     * Encodes this request into a {@link RpcApiMessage}.
     *
     * @return the encoded message
     */
    public abstract RpcApiRequestMessage encode();

    /**
     * Returns the ID of this request.
     *
     * @return the ID of this request
     */
    public long id() {
        return id;
    }

    /**
     * Returns the result of this request.
     *
     * @return the result of this request
     */
    public Web3Result<RpcApiResponseMessage> result() {
        return result;
    }

    /**
     * Returns the result of this request as a string.
     *
     * @return the result of this request as a string
     */
    public Web3Result<String> resultAsString() {
        return result.map(RpcApiResponseMessage::resultAsString);
    }

    /**
     * Sends this request to the specified {@link RpcClient}.
     *
     * @param client the client to send the request to
     * @return true if the request was successfully sent
     */
    @Override
    public boolean send(@NotNull RpcClient client) {
        val message = encode();
        return client.send(message.withId(id));
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
        if (!(message instanceof RpcApiMessage apiMessage)) {
            return false;
        }

        val id = apiMessage.id();
        if (id == null || id != this.id) {
            return false;
        }

        require(message instanceof RpcApiResponseMessage, "Received message is not a response.");
        val responseMessage = (RpcApiResponseMessage) message;
        if (responseMessage.error() != null) {
            result.completeExceptionally(new Web3Error(responseMessage.error().message()));
            return true;
        }

        result.complete(responseMessage);
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
