package dev.klepto.kweb3.core.rpc;

import dev.klepto.kweb3.core.Web3Error;
import dev.klepto.kweb3.core.config.Web3Endpoint;
import dev.klepto.kweb3.core.rpc.api.EthCall;
import dev.klepto.kweb3.core.rpc.api.EthEstimateGas;
import dev.klepto.kweb3.core.rpc.api.EthGasPrice;
import dev.klepto.kweb3.core.rpc.api.EthSendRawTransaction;

/**
 * Represents Ethereum RPC API implementing all the supported methods.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcApi extends EthCall, EthGasPrice, EthEstimateGas, EthSendRawTransaction {

    /**
     * Default JSON RPC version.
     */
    String JSON_VERSION = "2.0";

    /**
     * Creates a new RPC API instance for the specified transport and URL.
     *
     * @param endpoint the endpoint to create the RPC API for
     * @return a new RPC API instance
     */
    static RpcApi create(Web3Endpoint endpoint) {
        return switch (endpoint.transport()) {
            case WEBSOCKET -> new WebsocketApiClient(endpoint);
            default -> throw new Web3Error("Unsupported transport: {}", endpoint.transport());
        };
    }

}
