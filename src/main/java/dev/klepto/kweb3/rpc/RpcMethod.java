package dev.klepto.kweb3.rpc;

import com.google.gson.Gson;

/**
 * Indicates that class contains an ethereum RPC method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcMethod {

    /**
     * Global {@link Gson} instance for JSON parsing.
     */
    Gson GSON = new Gson();

    /**
     * Gets {@link RpcApiClient}, default behavior is that this interface is implemented in {@link RpcApiClient}.
     *
     * @return the instance of RpcApiClient
     */
    default RpcApiClient getClient() {
        return (RpcApiClient) this;
    }

}
