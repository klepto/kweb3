package dev.klepto.kweb3.rpc;

import dev.klepto.kweb3.Web3Error;
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
public class WebsocketClient extends WebSocketClient {

    private final String url;
    private final Consumer<String> messageCallback;
    private final Runnable closeCallback;

    /**
     * Creates a new client with given websocket url.
     *
     * @param url             the websocket url
     * @param messageCallback the callback that gets called when websocket receives a message
     * @param closeCallback   the callback that gets called after websocket is closed
     */
    public WebsocketClient(String url, Consumer<String> messageCallback, Runnable closeCallback) {
        super(URI.create(url));
        this.url = url;
        this.messageCallback = messageCallback;
        this.closeCallback = closeCallback;
    }

    /**
     * Automatically connects the client and transmits the given text message.
     *
     * @param message the string message which will be transmitted.
     */
    @Override
    public void send(String message) {
        try {
            if (!isOpen()) {
                connectBlocking();
            }
            super.send(message);
        } catch (Exception cause) {
            throw new Web3Error(cause);
        }
    }

    /**
     * Called after establishing websocket connection.
     *
     * @param handshakedata The handshake of the websocket instance
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        log.info("WebSocket RPC connected: {}", url);
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
        log.info("WebSocket RPC disconnected: {} - {}", reason, url);
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
    }

}
