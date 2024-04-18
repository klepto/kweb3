package dev.klepto.kweb3.core.ethereum.rpc.protocol;

/**
 * Marker interface for JSON protocol message, implemented by {@link RpcResponse} and {@link RpcRequest}.
 */
public interface RpcMessage {

    /**
     * Returns the JSON-RPC version.
     *
     * @return the JSON-RPC version
     */
    String jsonrpc();

    /**
     * Returns the message id, or <code>-1</code> if the message does not have an id.
     *
     * @return the message id or <code>-1</code>
     */
    long id();


    /**
     * Serializes the message into a JSON string.
     *
     * @return the JSON string representation of the message
     */
    default String serialize() {
        return RpcProtocol.GSON.toJson(this);
    }

}
