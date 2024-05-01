package dev.klepto.kweb3.core.ethereum.rpc.io;

import dev.klepto.kweb3.core.chain.Web3Endpoint;
import dev.klepto.kweb3.core.chain.Web3Transport;
import lombok.Synchronized;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * Provides connection from a list of endpoints.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class RpcConnectionProvider {

    private final List<Web3Endpoint> endpoints;
    private final AtomicReference<RpcConnection> connection = new AtomicReference<>();

    /**
     * Creates a new endpoint provider with the list of endpoints.
     *
     * @param endpoints the list of endpoints
     */
    public RpcConnectionProvider(List<Web3Endpoint> endpoints) {
        this.endpoints = new ArrayList<>(endpoints);
    }

    /**
     * Gets the current connection. If no connection is currently selected, creates connection for the first endpoint in
     * the list.
     *
     * @return the current connection
     */
    public RpcConnection connection() {
        if (connection.get() == null) {
            return next();
        }
        return connection.get();
    }

    /**
     * Selects the next endpoint in the list. If the current endpoint is the last one, the first endpoint is selected in
     * order to repeat the cycle.
     */
    @Synchronized
    public RpcConnection next() {
        val index = connection.get() == null ? -1 : endpoints.indexOf(connection.get().endpoint());
        val nextIndex = (index + 1) % endpoints.size();
        connection.set(createConnection(endpoints.get(nextIndex)));
        return connection.get();
    }

    /**
     * Creates a new connection from the given endpoint.
     *
     * @param endpoint the endpoint
     * @return the connection
     */
    private RpcConnection createConnection(Web3Endpoint endpoint) {
        val transport = endpoint.transport();
        require(transport == Web3Transport.WEBSOCKET
                        || transport == Web3Transport.HTTP,
                "Unsupported transport: {}", transport
        );
        return transport == Web3Transport.WEBSOCKET
                ? new WebsocketRpcConnection(endpoint)
                : new HttpRpcConnection(endpoint);
    }

}
