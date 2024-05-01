package dev.klepto.kweb3.core.ethereum.rpc.io;

import dev.klepto.kweb3.core.chain.Web3Endpoint;
import dev.klepto.kweb3.core.ethereum.rpc.RpcMessage;
import dev.klepto.kweb3.core.ethereum.rpc.api.RpcApiMessage;
import lombok.Synchronized;
import lombok.val;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Represents a connection to a remote RPC server.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public abstract class ScheduledRpcConnection implements RpcConnection {

    private static final Duration FLUSH_INTERVAL = Duration.ofMillis(50);
    private static final int BATCH_SIZE_LIMIT = 64;

    private final Queue<RpcMessage> messageQueue = new ConcurrentLinkedQueue<>();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final AtomicBoolean batch = new AtomicBoolean();
    private final AtomicReference<ScheduledFuture<?>> commitTask = new AtomicReference<>();
    private final AtomicLong commitTimestamp = new AtomicLong();

    private final Web3Endpoint endpoint;
    private final Web3Endpoint authorizedEndpoint;
    private Consumer<RpcMessage> messageCallback;
    private Consumer<Throwable> errorCallback;
    private Runnable closeCallback;

    /**
     * Constructs a new {@link ScheduledRpcConnection} for the specified endpoint.
     *
     * @param endpoint the endpoint
     */
    public ScheduledRpcConnection(Web3Endpoint endpoint) {
        this.endpoint = endpoint;
        this.authorizedEndpoint = endpoint.authorization() != null
                ? endpoint.authorization().authorize(endpoint) : endpoint;
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
     * Sets the batch mode.
     *
     * @param batch whether to enable batch mode
     */
    public void batch(boolean batch) {
        this.batch.set(batch);
        if (commitTask.get() != null) {
            commitTask.get().cancel(false);
        }
        if (batch) {
            commitTask.set(executor.scheduleAtFixedRate(
                    this::commit,
                    0,
                    FLUSH_INTERVAL.toMillis(),
                    TimeUnit.MILLISECONDS
            ));
        }
    }

    /**
     * Returns whether the connection is in batch mode.
     *
     * @return whether the connection is in batch mode
     */
    public boolean isBatching() {
        return batch.get();
    }

    /**
     * Returns the executor service used by this connection.
     *
     * @return the executor service
     */
    public ScheduledExecutorService executor() {
        return executor;
    }

    /**
     * Asynchronously sends a message to the remote server.
     *
     * @param message the message
     */
    @Override
    public void send(RpcMessage message) {
        messageQueue.add(message);

        if (!isBatching()) {
            commit();
        }
    }

    /**
     * Encodes the message queue and sends all the messages to the remote host.
     */
    @Synchronized
    public void commit() {
        if (messageQueue.isEmpty()) {
            return;
        }

        val messages = new ArrayList<RpcMessage>();
        while (!messageQueue.isEmpty() && messages.size() < BATCH_SIZE_LIMIT) {
            messages.add(messageQueue.poll());
        }

        val apiMessages = messages.stream()
                .filter(message -> message instanceof RpcApiMessage)
                .map(message -> (RpcApiMessage) message)
                .toList();

        if (apiMessages.isEmpty()) {
            return;
        }

        val request = RpcApiMessage.encode(apiMessages);
        val cooldown = endpoint().settings().requestCooldown();
        val elapsed = System.currentTimeMillis() - commitTimestamp.get();
        val target = cooldown != null ? cooldown.toMillis() - elapsed : 0;

        if (target <= 0) {
            send(request);
        } else {
            commitTimestamp.set(target);
            executor.schedule(() -> send(request), target, TimeUnit.MILLISECONDS);
        }
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
        RpcApiMessage.decode(response)
                .forEach(this::messageCallback);
    }

    /**
     * Closes the connection.
     */
    @Override
    public void close() {
        executor.shutdown();
        closeCallback();
    }

    /**
     * Sets the message callback. Invoked when a response is received from the remote server.
     *
     * @param messageCallback the message callback
     */
    @Override
    public void onMessage(Consumer<RpcMessage> messageCallback) {
        this.messageCallback = messageCallback;
    }

    /**
     * Sets the error callback. Invoked when error occurs during communication with the remote server.
     *
     * @param errorCallback the error callback
     */
    @Override
    public void onError(Consumer<Throwable> errorCallback) {
        this.errorCallback = errorCallback;
    }


    /**
     * Sets the close callback. Invoked when the connection is closed, only applicable to transport protocols that
     *
     * @param closeCallback the close callback
     */
    @Override
    public void onClose(Runnable closeCallback) {
        this.closeCallback = closeCallback;
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
