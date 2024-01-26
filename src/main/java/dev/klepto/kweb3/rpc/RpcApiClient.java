package dev.klepto.kweb3.rpc;

import com.google.gson.JsonObject;
import dev.klepto.kweb3.Web3Error;
import dev.klepto.kweb3.Web3Network;
import dev.klepto.kweb3.Web3Result;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Slf4j
public class RpcApiClient extends WebSocketClient implements RpcApi {

    private final Web3Network network;
    private final AtomicLong requestId = new AtomicLong(1);
    private final Map<Long, Web3Result<RpcResponse>> requests = new ConcurrentHashMap<>();

    public RpcApiClient(Web3Network network) {
        super(URI.create(network.rpcUrl()));
        this.network = network;
    }

    private void checkConnection() {
        try {
            if (!isOpen()) {
                connectBlocking();
            } else {
                sendPing();
            }
        } catch (Exception cause) {
            log.error("Couldn't connect to RPC", cause);
        }
    }

    public RpcRequest createRequest() {
        val requestId = this.requestId.incrementAndGet();
        return new RpcRequest().withId(requestId);
    }

    public Web3Result<RpcResponse> request(RpcRequest request) {
        checkConnection();

        try {
            val future = Web3Result.<RpcResponse>create();
            val json = GSON.toJson(request);
            requests.put(request.id(), future);
            send(json);
            return future;
        } catch (Exception cause) {
            throw new Web3Error(cause);
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        log.info("WebSocket RPC connected: {}", network);
    }

    @Override
    public void onMessage(String message) {
        log.debug(message.trim());
        val json = GSON.fromJson(message, JsonObject.class);
        if (json.has("id")) {
            val response = GSON.fromJson(json, RpcResponse.class);
            val future = requests.remove(response.id());
            if (future == null) {
                log.warn("Listener not found for response id: {}", response.id());
                return;
            }
            future.complete(response);
        } else {
            log.warn("Unknown RPC message: {}", message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("WebSocket RPC disconnected: {}", network);
        requests.values().forEach(Web3Result::cancel);
        requests.clear();
    }

    @Override
    public void onError(Exception cause) {
        log.error("WebSocket RPC error", cause);
    }

}
