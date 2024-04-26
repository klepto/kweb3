package dev.klepto.kweb3.core.ethereum.rpc.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of Ethereum RPC API <code>eth_estimateGas</code> method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
@RequiredArgsConstructor
public class EthEstimateGasRequest extends RpcApiRequest {

    private final @Nullable String from;
    private final @NotNull String to;
    private final @Nullable Integer gas;
    private final @Nullable String gasPrice;
    private final @Nullable String value;
    private final @NotNull String data;
    private final @Nullable String blockNumber;

    /**
     * Encodes the request object into an API message.
     *
     * @return the api message
     */
    @Override
    public RpcApiRequestMessage encode() {
        val blockNumber = this.blockNumber == null ? "latest" : this.blockNumber;
        val transaction = new TransactionParameter(from, to, gas, gasPrice, value, data);
        return new RpcApiRequestMessage()
                .withMethod("eth_estimateGas")
                .withParams(transaction, blockNumber);
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
    private record TransactionParameter(@Nullable String from,
                                        @NotNull String to,
                                        @Nullable Integer gas,
                                        @Nullable String gasPrice,
                                        @Nullable String value,
                                        @NotNull String data) {
    }
}
