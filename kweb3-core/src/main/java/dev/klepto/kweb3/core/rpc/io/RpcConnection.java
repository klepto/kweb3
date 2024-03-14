package dev.klepto.kweb3.core.rpc.io;

import java.util.function.Consumer;

/**
 * Represents a connection to a remote RPC server.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcConnection {

    /**
     * Gets the URL of the remote server.
     *
     * @return the url
     */
    String url();

    /**
     * Asynchronously sends a message to the remote server.
     *
     * @param message the message
     */
    void send(String message);

    /**
     * Sets the callback that gets called when a message is received from the remote server.
     *
     * @param messageCallback the message callback
     */
    void setMessageCallback(Consumer<String> messageCallback);

    /**
     * Sets the callback that gets called when an error occurs.
     *
     * @param errorCallback the error callback
     */
    void setErrorCallback(Consumer<Throwable> errorCallback);

    /**
     * Sets the callback that gets called after the connection is closed.
     *
     * @param closeCallback the close callback
     */
    void setCloseCallback(Runnable closeCallback);

}
