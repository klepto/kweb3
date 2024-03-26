package dev.klepto.kweb3.core.rpc.io;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.function.Consumer;

/**
 * Implementation of single-use {@link WebSocketClient}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Slf4j
public class WebsocketConnection extends WebSocketClient implements RpcConnection {

    private final String url;

    @Setter
    private Consumer<String> messageCallback;

    @Setter
    private Consumer<Throwable> errorCallback;

    @Setter
    private Runnable closeCallback;

    public WebsocketConnection(String url) {
        super(URI.create(url));
        this.url = url;
    }

    /**
     * Gets the URL of the remote server.
     *
     * @return the url
     */
    @Override
    public String url() {
        return url;
    }

    /**
     * Connects to the remote server.
     */
    @Override
    public void open() {
        try {
            connectBlocking();
        } catch (Throwable cause) {
            if (errorCallback != null) {
                errorCallback.accept(cause);
            }
        }
    }

    /**
     * Asynchronously sends a message to the remote server.
     *
     * @param message the message
     */
    @Override
    public void send(String message) {
        try {
            super.send(message);
        } catch (Exception cause) {
            if (errorCallback != null) {
                errorCallback.accept(cause);
            }
        }
    }

    /**
     * Called after establishing websocket connection.
     *
     * @param handshakedata The handshake of the websocket instance
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        log.debug("WebSocket RPC connected: {}", url);
    }

    /**
     * Called when websocket message is received.
     *
     * @param message the utf-8 decoded message that was received
     */
    @Override
    public void onMessage(String message) {
        log.debug("Websocket message: {}", message.trim());
        if (messageCallback != null) {
            messageCallback.accept(message);
        }
    }

    /**
     * Called when websocket is closed for any reason.
     *
     * @param code   the websocket closing code
     * @param reason the additional information about the close
     * @param remote true if closing was initiated by remote host
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.debug("WebSocket RPC disconnected: {} - {}", reason, url);
        if (closeCallback != null) {
            closeCallback.run();
        }
    }

    /**
     * Called when exception occurred during websocket I/O.
     *
     * @param cause the exception causing this error
     */
    @Override
    public void onError(Exception cause) {
        log.error("Websocket error", cause);
        if (errorCallback != null) {
            errorCallback.accept(cause);
        }
    }

}
