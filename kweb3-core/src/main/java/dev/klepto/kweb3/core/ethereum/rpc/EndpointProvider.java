package dev.klepto.kweb3.core.ethereum.rpc;

import dev.klepto.kweb3.core.chain.Web3Endpoint;
import lombok.Synchronized;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * Endpoint provider with fallback mechanism allowing to cycle through multiple {@link Web3Endpoint}s upon failure.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class EndpointProvider {

    private final List<Web3Endpoint> endpoints;
    private final AtomicReference<Web3Endpoint> current = new AtomicReference<>();

    /**
     * Creates a new endpoint provider with the list of endpoints.
     *
     * @param endpoints the list of endpoints
     */
    public EndpointProvider(List<Web3Endpoint> endpoints) {
        require(!endpoints.isEmpty(), "At least one endpoint must be provided");
        this.endpoints = new ArrayList<>(endpoints);
    }

    /**
     * Gets the current {@link Web3Endpoint}. If no endpoint is currently selected, the first endpoint is selected.
     *
     * @return the current endpoint
     */
    public Web3Endpoint current() {
        if (current.get() == null) {
            return next();
        }
        return current.get();
    }

    /**
     * Selects the next endpoint in the list. If the current endpoint is the last one, the first endpoint is selected in
     * order to repeat the cycle.
     *
     * @return the next endpoint
     */
    @Synchronized
    public Web3Endpoint next() {
        if (current.get() == null) {
            current.set(endpoints.get(0));
            return current.get();
        }

        val index = endpoints.indexOf(current.get());
        val nextIndex = (index + 1) % endpoints.size();
        current.set(endpoints.get(nextIndex));
        return current.get();
    }


}
