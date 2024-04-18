package dev.klepto.kweb3.core.ethereum.rpc.protocol.api;

import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.ethereum.rpc.protocol.RpcMethod;
import dev.klepto.kweb3.core.ethereum.rpc.protocol.RpcRequest;
import dev.klepto.kweb3.core.ethereum.rpc.protocol.RpcResponse;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of Ethereum RPC API <code>eth_estimateGas</code> method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthEstimateGas extends RpcMethod {

    /**
     * Returns an estimation of gas for a given transaction.
     *
     * @param from        the address from which the transaction is sent or null
     * @param to          the address to which the transaction is addressed
     * @param gas         the integer of gas provided for the transaction execution or null
     * @param gasPrice    the integer of gasPrice used for each paid gas encoded as hexadecimal or null
     * @param value       the integer of value sent with this transaction encoded as hexadecimal or null
     * @param data        the hash of the method signature and encoded parameters
     * @param blockNumber the block number in hexadecimal format or the string <code>latest</code>,
     *                    <code>earliest</code> or <code>pending</code>, if <code>null>, </code>defaults to
     *                    <code>latest</code>
     * @return the return value of the executed contract method
     */
    @NotNull
    default Web3Result<String> ethEstimateGas(@Nullable String from,
                                              @NotNull String to,
                                              @Nullable Integer gas,
                                              @Nullable String gasPrice,
                                              @Nullable String value,
                                              @NotNull String data,
                                              @Nullable String blockNumber) {
        blockNumber = blockNumber == null ? "latest" : blockNumber;
        val transaction = new TransactionParameter(from, to, gas, gasPrice, value, data);
        val request = new RpcRequest()
                .withMethod("eth_estimateGas")
                .withParams(transaction, blockNumber);

        return request(request)
                .map(RpcResponse::resultAsString);
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
    record TransactionParameter(@Nullable String from,
                                @NotNull String to,
                                @Nullable Integer gas,
                                @Nullable String gasPrice,
                                @Nullable String value,
                                @NotNull String data) {
    }

}
