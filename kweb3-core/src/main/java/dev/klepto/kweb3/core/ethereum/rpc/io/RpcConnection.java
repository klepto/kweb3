package dev.klepto.kweb3.core.ethereum.rpc.io;

import dev.klepto.kweb3.core.chain.Web3Endpoint;
import dev.klepto.kweb3.core.ethereum.rpc.RpcMessage;

import java.io.Closeable;
import java.util.function.Consumer;

/**
 * Represents a connection to a remote RPC server.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcConnection extends Closeable {

    /**
     * Returns the endpoint of the connection.
     *
     * @return the endpoint
     */
    Web3Endpoint endpoint();

    /**
     * Sets the batch mode. When in batch mode, messages are not sent immediately, but are queued and sent in batches at
     * a later time.
     *
     * @param batch whether to enable batch mode
     */
    void batch(boolean batch);

    /**
     * Returns whether the connection is in batch mode.
     *
     * @return whether the connection is in batch mode
     */
    boolean isBatching();

    /**
     * Sends a message to the remote server.
     *
     * @param message the message
     */
    void send(RpcMessage message);

    /**
     * Sets the message callback. Invoked when a response is received from the remote server.
     *
     * @param messageCallback the message callback
     */
    void onMessage(Consumer<RpcMessage> messageCallback);

    /**
     * Sets the error callback. Invoked when error occurs during communication with the remote server.
     *
     * @param errorCallback the error callback
     */
    void onError(Consumer<Throwable> errorCallback);

    /**
     * Sets the close callback. Invoked when the connection is closed, only applicable to transport protocols that
     * maintain an open connection.
     *
     * @param closeCallback the close callback
     */
    void onClose(Runnable closeCallback);

    /**
     * Closes the connection.
     */
    void close();

}
