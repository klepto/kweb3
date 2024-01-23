package dev.klepto.kweb3.rpc.api;

/**
 * Implementation of Ethereum RPC API <code>eth_sendRawTransaction</code> method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcEthSendRawTransaction {

    /**
     * Submits a pre-signed transaction for broadcast to the Ethereum network.
     *
     * @param request the request object
     * @return the response object
     */
    Response ethSendRawTransaction(Request request);

    /**
     * Represents <code>eth_sendRawTransaction</code> request object.
     *
     * @param data the signed transaction (typically signed with a library, using your private key)
     */
    record Request(String data) {
    }

    /**
     * Represents <code>eth_sendRawTransaction</code> response object.
     *
     * @param result the transaction hash, or the zero hash if the transaction is not yet available
     */
    record Response(String result) {
    }

}
