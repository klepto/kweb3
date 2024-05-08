package dev.klepto.kweb3.core.ethereum.rpc.io;

import dev.klepto.kweb3.core.chain.Web3Endpoint;
import dev.klepto.kweb3.core.ethereum.rpc.RpcMessage;
import dev.klepto.kweb3.core.ethereum.rpc.api.RpcApiMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Represents a connection to a remote RPC server.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public abstract class AuthorizedRpcConnection implements RpcConnection {

    private final Web3Endpoint endpoint;
    private final Web3Endpoint authorizedEndpoint;
    private final Consumer<RpcMessage> messageCallback;
    private final Consumer<Throwable> errorCallback;
    private final Runnable closeCallback;

    /**
     * Constructs a new {@link AuthorizedRpcConnection} for the specified endpoint.
     *
     * @param endpoint        the endpoint
     * @param messageCallback the message callback
     * @param errorCallback   the error callback
     * @param closeCallback   the close callback
     */
    public AuthorizedRpcConnection(@NotNull Web3Endpoint endpoint,
                                   @Nullable Consumer<RpcMessage> messageCallback,
                                   @Nullable Consumer<Throwable> errorCallback,
                                   @Nullable Runnable closeCallback) {
        this.endpoint = endpoint;
        this.authorizedEndpoint = endpoint.authorize();
        this.messageCallback = messageCallback;
        this.errorCallback = errorCallback;
        this.closeCallback = closeCallback;
    }


    /**
     * Returns the endpoint of the connection.
     *
     * @return the endpoint
     */
    @Override
    public Web3Endpoint endpoint() {
        return endpoint;
    }

    /**
     * Returns the authorized endpoint of the connection.
     *
     * @return the authorized endpoint
     */
    public Web3Endpoint authorizedEndpoint() {
        return authorizedEndpoint;
    }

    /**
     * Asynchronously sends a message to the remote server.
     *
     * @param message the message
     */
    @Override
    public void send(RpcMessage message) {
        send(message.serialize());
    }

    /**
     * Sends a message directly to the remote server, bypassing the internal executor. This method will not respect
     * endpoint throttling constrains and may be blocking depending on the underlying implementation of the connection.
     *
     * @param message the message
     */
    public abstract void send(String message);


    /**
     * Receives a response from the remote server.
     *
     * @param response the response
     */
    public void receive(String response) {
        if (messageCallback != null) {
            RpcApiMessage.decode(response)
                    .forEach(this::messageCallback);
        }
    }

    /**
     * Closes the connection.
     */
    @Override
    public void close() {
        closeCallback();
    }

    /**
     * Invoked when a message is received.
     *
     * @param message the message
     */
    public void messageCallback(RpcMessage message) {
        if (messageCallback != null) {
            messageCallback.accept(message);
        }
    }

    /**
     * Invoked when an error occurs.
     *
     * @param error the error
     */
    public void errorCallback(Throwable error) {
        if (errorCallback != null) {
            errorCallback.accept(error);
        }
    }

    /**
     * Invoked when the connection is closed.
     */
    public void closeCallback() {
        if (closeCallback != null) {
            closeCallback.run();
        }
    }

}
