package dev.klepto.kweb3.core.ethereum.rpc.io;

import dev.klepto.kweb3.core.chain.Web3Endpoint;
import dev.klepto.kweb3.core.chain.Web3Transport;
import dev.klepto.kweb3.core.ethereum.rpc.RpcMessage;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.util.function.Consumer;

import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * Represents a connection to a remote RPC server.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcConnection extends Closeable {

    /**
     * Creates a new connection from the given endpoint.
     *
     * @param endpoint the endpoint
     * @return the connection
     */
    static RpcConnection create(@NotNull Web3Endpoint endpoint,
                                @Nullable Consumer<RpcMessage> messageCallback,
                                @Nullable Consumer<Throwable> errorCallback,
                                @Nullable Runnable closeCallback) {
        val transport = endpoint.transport();
        require(transport == Web3Transport.WEBSOCKET
                        || transport == Web3Transport.HTTP,
                "Unsupported transport: {}", transport
        );

        return transport == Web3Transport.WEBSOCKET
                ? new WebsocketRpcConnection(endpoint, messageCallback, errorCallback, closeCallback)
                : new HttpRpcConnection(endpoint, messageCallback, errorCallback, closeCallback);
    }

    /**
     * Returns the endpoint of the connection.
     *
     * @return the endpoint
     */
    Web3Endpoint endpoint();

    /**
     * Sends a message to the remote server.
     *
     * @param message the message
     */
    void send(RpcMessage message);

    /**
     * Closes the connection.
     */
    void close();

}
