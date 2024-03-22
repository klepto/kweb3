package dev.klepto.kweb3.core.contract.log;

import dev.klepto.kweb3.core.contract.ContractCall;
import dev.klepto.kweb3.core.type.EthAddress;
import dev.klepto.kweb3.core.type.EthBytes;
import dev.klepto.kweb3.core.type.EthUint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Produced by {@link LoggingContractExecutor} in order to log contract calls.
 *
 * @param call        the contract call
 * @param transaction the resulting transaction from contract call
 */
public record ContractCallLog(@NotNull ContractCall call,
                              @NotNull Transaction transaction) {

    /**
     * Represents a transaction that was produced by a contract call.
     *
     * @param to               the recipient address
     * @param from             the sender address
     * @param gasLimit         the gas limit
     * @param gasPrice         the gas price, or max fee per gas for EIP1559 transactions.
     * @param priorityGasPrice the max priority fee per gas for EIP1559 transactions
     * @param value            the  ether to send in the transaction
     * @param data             the data to send in the transaction
     */
    public record Transaction(@NotNull EthAddress to,
                              @NotNull EthAddress from,
                              @Nullable EthUint gasLimit,
                              @Nullable EthUint gasPrice,
                              @Nullable EthUint priorityGasPrice,
                              @Nullable EthUint value,
                              @NotNull EthBytes data) {
    }

}
