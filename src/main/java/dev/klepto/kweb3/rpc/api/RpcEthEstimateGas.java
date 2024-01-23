package dev.klepto.kweb3.rpc.api;

import org.jetbrains.annotations.Nullable;

/**
 * Implementation of Ethereum RPC API <code>eth_estimateGas</code> method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcEthEstimateGas {

    /**
     * Generates and returns an estimate of how much gas is necessary to allow the transaction to complete. The
     * transaction will not be added to the blockchain.
     *
     * @param request the request object
     * @return the response object
     */
    Response ethEstimateGas(Request request);

    /**
     * Represents <code>eth_estimateGas</code> request object.
     *
     * @param transaction the Transaction object
     * @param blockNumber the block number in hexadecimal format or the string latest, earliest or pending
     */
    record Request(Transaction transaction, String blockNumber) {
    }

    /**
     * Represents <code>eth_estimateGas</code> response object.
     *
     * @param quantity the estimated amount of gas used
     */
    record Response(String quantity) {
    }

    /**
     * Represents <code>eth_estimateGas</code> request transaction object.
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
