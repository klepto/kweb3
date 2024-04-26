package dev.klepto.kweb3.core.ethereum.rpc;

/**
 * Marker interface for RPC protocol message.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcMessage {

    /**
     * Serializes the message into a request string.
     */
    String serialize();

}
