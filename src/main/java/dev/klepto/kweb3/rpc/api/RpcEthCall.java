package dev.klepto.kweb3.rpc.api;

import org.jetbrains.annotations.Nullable;

/**
 * Implementation of Ethereum RPC API <code>eth_call</code> method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcEthCall {

    /**
     * Executes a new message call immediately without creating a transaction on the blockchain.
     *
     * @param request the request object
     * @return the response object
     */
    Response ethCall(Request request);

    /**
     * Represents <code>eth_call</code> request object.
     *
     * @param transaction the Transaction object
     * @param blockNumber the block number in hexadecimal format or the string latest, earliest or pending
     */
    record Request(Transaction transaction, String blockNumber) {
    }

    /**
     * Represents <code>eth_call</code> response object.
     *
     * @param data the return value of the executed contract method
     */
    record Response(String data) {
    }

    /**
     * Represents <code>eth_call</code> request transaction object.
     *
     * @param from     the address from which the transaction is sent or null
     * @param to       the address to which the transaction is addressed
     * @param gas      the integer of gas provided for the transaction execution or null
     * @param gasPrice the integer of gasPrice used for each paid gas encoded as hexadecimal or null
     * @param value    the integer of value sent with this transaction encoded as hexadecimal or null
     * @param data     the hash of the method signature and encoded parameters
     */
    record Transaction(@Nullable String from,
                       String to,
                       @Nullable Integer gas,
                       @Nullable String gasPrice,
                       @Nullable String value,
                       String data) {
    }

}
