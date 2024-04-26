package dev.klepto.kweb3.core.ethereum.rpc;

import dev.klepto.kweb3.core.chain.Web3Endpoint;
import dev.klepto.kweb3.core.ethereum.rpc.api.EthProtocol;
import dev.klepto.kweb3.core.ethereum.rpc.io.RpcConnection;
import dev.klepto.kweb3.core.ethereum.rpc.io.RpcConnectionProvider;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implementation of Ethereum RPC API client.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@RequiredArgsConstructor
public class RpcClient implements Closeable, EthProtocol {

    private final RpcConnectionProvider connectionProvider;
    private final AtomicReference<RpcConnection> connection = new AtomicReference<>();
    private final Queue<RpcRequest> requests = new ConcurrentLinkedQueue<>();

    /**
     * Returns the current endpoint.
     *
     * @return the current endpoint
     */
    public Web3Endpoint endpoint() {
        return connect().endpoint();
    }

    /**
     * Sets the batch mode. When in batch mode, messages are not sent immediately, but are queued and sent in batches at
     * a later time.
     *
     * @param batch true if batch mode should be enabled
     */
    public void batch(boolean batch) {
        connect().batch(batch);
    }

    /**
     * Sends the specified request to the connected node and adds it to the <code>requests</code> queue.
     *
     * @param request the request to send
     */
    public void request(@NotNull RpcRequest request) {
        requests.add(request);
        if (!request.send(this)) {
            requests.remove(request);
        }
    }

    /**
     * Adds the specified message to the message queue.
     *
     * @param message the message to send
     */
    public boolean send(@NotNull RpcMessage message) {
        connect().send(message);
        return true;
    }

    /**
     * Processes the response message from the connected node.
     *
     * @param message the message received from the connected node
     */
    private void onMessage(@NotNull RpcMessage message) {
        requests.removeIf(request -> request.isComplete(this, message));
    }

    /**
     * Called upon IO error. Selects next endpoint in the list.
     *
     * @param throwable the error that occurred during the connection
     */
    private void onError(@NotNull Throwable throwable) {
        requests.removeIf(request -> request.onError(this, throwable));
        connectionProvider.next();
    }

    /**
     * Called when connection is closed. Selects next endpoint in the list.
     */
    private void onClose() {
        connectionProvider.next();
    }

    /**
     * Selects next connection from {@link RpcConnectionProvider} and connects to it.
     *
     * @return the connection to the next endpoint
     */
    private RpcConnection connect() {
        val current = connection.get();
        val next = connectionProvider.connection();
        if (next != current) {
            next.onMessage(this::onMessage);
            next.onError(this::onError);
            next.onClose(this::onClose);
            connection.set(next);
            if (current != null) {
                next.batch(current.isBatching());
                replay();
            }
            return next;
        }
        return current;
    }

    /**
     * Replays the requests that were not completed.
     */
    private void replay() {
        requests.forEach(request -> request.send(this));
    }

    @Override
    public void close() {
        if (connection.get() != null) {
            connection.get().close();
        }
    }
}
