package dev.klepto.kweb3.core.ethereum.rpc.io;

import dev.klepto.kweb3.core.chain.Web3Endpoint;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Implementation of {@link ScheduledRpcConnection} for WebSocket connections.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class WebsocketRpcConnection extends ScheduledRpcConnection {

    private final Client client = new Client();

    /**
     * Constructs new {@link WebsocketRpcConnection} for the specified endpoint.
     *
     * @param endpoint the endpoint
     */
    public WebsocketRpcConnection(Web3Endpoint endpoint) {
        super(endpoint);
    }

    /**
     * Ensures that the websocket connection is open.
     */
    private void ensureOpen() {
        if (client.isOpen()) {
            return;
        }
        try {
            client.connectBlocking();
        } catch (Throwable cause) {
            errorCallback(cause);
        }
    }

    /**
     * Sends the message to the endpoint using WebSocket client.
     *
     * @param message the message
     */
    @Override
    public void send(String message) {
        ensureOpen();

        try {
            client.send(message);
        } catch (Throwable cause) {
            errorCallback(cause);
        }
    }

    /**
     * Closes the connection.
     */
    @Override
    public void close() {
        super.close();
        client.close();
    }

    /**
     * WebSocket client implementation.
     */
    private class Client extends WebSocketClient {
        public Client() {
            super(URI.create(authorizedEndpoint().url()));
        }

        /**
         * Invoked when the connection is opened.
         *
         * @param handshakedata the handshake data
         */
        @Override
        public void onOpen(ServerHandshake handshakedata) {
        }

        /**
         * Invoked when a message is received.
         *
         * @param message the message
         */
        @Override
        public void onMessage(String message) {
            WebsocketRpcConnection.super.receive(message);
        }

        /**
         * Invoked when an error occurs.
         *
         * @param ex the exception
         */
        @Override
        public void onError(Exception ex) {
            WebsocketRpcConnection.super.errorCallback(ex);
        }

        /**
         * Invoked when the connection is closed.
         *
         * @param code   the code
         * @param reason the reason
         * @param remote whether the connection was closed remotely
         */
        @Override
        public void onClose(int code, String reason, boolean remote) {
            WebsocketRpcConnection.super.closeCallback();
        }
    }
}
