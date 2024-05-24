package dev.klepto.kweb3.core.ethereum.rpc;

import dev.klepto.kweb3.core.chain.Web3Endpoint;
import dev.klepto.kweb3.core.ethereum.rpc.api.EthProtocol;
import dev.klepto.kweb3.core.ethereum.rpc.io.RpcConnection;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Implementation of Ethereum RPC API client.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public class RpcClient implements Closeable, EthProtocol {

    private final RpcConnection connection;
    private final Queue<RpcRequest> requests = new ConcurrentLinkedQueue<>();

    /**
     * Constructs a new {@link RpcClient} for the specified endpoint.
     *
     * @param endpoint the endpoint
     */
    public RpcClient(Web3Endpoint endpoint) {
        this.connection = RpcConnection.create(endpoint, this::onMessage, this::onError, this::onClose);
    }

    /**
     * Returns the current endpoint.
     *
     * @return the current endpoint
     */
    public Web3Endpoint endpoint() {
        return connection.endpoint();
    }

    /**
     * Returns the current connection.
     *
     * @return the current connection
     */
    public RpcConnection connection() {
        return connection;
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
        connection.send(message);
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
     * Called upon IO error.
     *
     * @param throwable the error that occurred during the connection
     */
    private void onError(@NotNull Throwable throwable) {
        requests.removeIf(request -> request.onError(this, throwable));
    }

    /**
     * Called when connection is closed.
     */
    private void onClose() {

    }

    /**
     * Closes the connection.
     */
    @Override
    public void close() {
        connection.close();
    }
}
