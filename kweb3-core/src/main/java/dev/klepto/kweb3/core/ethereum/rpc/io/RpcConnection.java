package dev.klepto.kweb3.core.ethereum.rpc.io;

import com.google.common.util.concurrent.Uninterruptibles;
import dev.klepto.kweb3.core.chain.Web3Endpoint;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.io.Closeable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * Represents a connection to a remote RPC server.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public abstract class RpcConnection implements Closeable {

    private final @Getter Web3Endpoint endpoint;
    private final @Getter Web3Endpoint authorizedEndpoint;
    private final @Getter ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private @Setter Consumer<String> messageCallback;
    private @Setter Consumer<Throwable> errorCallback;
    private @Setter Runnable closeCallback;

    /**
     * Constructs a new {@link RpcConnection} for the specified endpoint.
     *
     * @param endpoint the endpoint
     */
    public RpcConnection(Web3Endpoint endpoint) {
        this.endpoint = endpoint;
        this.authorizedEndpoint = endpoint.authorization() != null
                ? endpoint.authorization().authorize(endpoint)
                : endpoint;
    }

    /**
     * Asynchronously sends a message to the remote server.
     *
     * @param message the message
     */
    public void send(String message) {
        getExecutor().submit(() -> {
            sendDirect(message);
            val cooldown = getEndpoint().settings().requestCooldown();
            if (cooldown != null) {
                Uninterruptibles.sleepUninterruptibly(cooldown);
            }
        });
    }

    /**
     * Sends a message directly to the remote server, bypassing the internal executor. This method will not respect
     * endpoint throttling constrains and may be blocking depending on the underlying implementation of the connection.
     *
     * @param message the message
     */
    public abstract void sendDirect(String message);

    /**
     * Closes the connection.
     */
    @Override
    public void close() {
        executor.shutdownNow();
        onClose();
    }

    /**
     * Invoked when the connection is closed.
     */
    public void onClose() {
        if (closeCallback != null) {
            closeCallback.run();
        }
    }

    /**
     * Invoked when a message is received.
     *
     * @param message the message
     */
    public void onMessage(String message) {
        if (messageCallback != null) {
            messageCallback.accept(message);
        }
    }

    /**
     * Invoked when an error occurs.
     *
     * @param error the error
     */
    public void onError(Throwable error) {
        if (errorCallback != null) {
            errorCallback.accept(error);
        }
    }


}
