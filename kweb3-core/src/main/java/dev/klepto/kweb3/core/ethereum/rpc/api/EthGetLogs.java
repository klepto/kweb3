package dev.klepto.kweb3.core.ethereum.rpc.api;

import com.google.common.reflect.TypeToken;
import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.ethereum.rpc.RpcMessage;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of Ethereum RPC API <code>eth_getLogs</code> method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthGetLogs extends RpcApi {

    /**
     * Returns an array of all the logs matching the given filters. Note that available filters are endpoint dependent
     * and different node providers may include different restrictions on this method.
     *
     * @param addresses the array of smart contract address filters
     * @param fromBlock the block number in hexadecimal format or the string <code>latest</code>, <code>earliest</code>
     * @param toBlock   the block number in hexadecimal format or the string <code>latest</code>, <code>earliest</code>
     * @param topics    the array of topics filters
     * @param blockHash the {@code blockHash} filter
     * @return an array of all the logs matching the given filters
     */
    @NotNull
    default Web3Result<List<LogResponse>> ethGetLogs(@Nullable String[] addresses,
                                                     @Nullable String fromBlock,
                                                     @Nullable String toBlock,
                                                     @Nullable String[] topics,
                                                     @Nullable String blockHash) {
        val parameters = new Parameters(addresses, fromBlock, toBlock, topics, blockHash);
        val request = new RpcMessage.RequestMessage()
                .withMethod("eth_getLogs")
                .withParams(parameters);
        return request(request)
                .map(response -> response.resultAs(LogResponse.LIST_TYPE));
    }

    /**
     * Container for <code>eth_getLogs</code> request parameters.
     *
     * @param address   the  array of smart contract address filters
     * @param fromBlock the block number in hexadecimal format or the string <code>latest</code>, <code>earliest</code>
     *                  or <code>pending</code>
     * @param toBlock   the block number in hexadecimal format or the string <code>latest</code>, <code>earliest</code>,
     *                  or <code>pending</code>
     * @param topics    the array of topics filters
     * @param blockHash the {@code blockHash} filter
     */
    record Parameters(@Nullable String[] address,
                      @Nullable String fromBlock,
                      @Nullable String toBlock,
                      @Nullable String[] topics,
                      @Nullable String blockHash) {
    }

    /**
     * Represents <code>eth_getLogs</code> response log object.
     *
     * @param address          the address of the log
     * @param topics           the array of topics of the log
     * @param data             the data of the log
     * @param blockNumber      the block number in hexadecimal format
     * @param transactionHash  the transaction hash in hexadecimal format
     * @param transactionIndex the transaction index in hexadecimal format
     * @param blockHash        the block hash in hexadecimal format
     * @param logIndex         the log index in hexadecimal format
     * @param removed          whether the log was removed
     */
    record LogResponse(@NotNull String address,
                       @NotNull String[] topics,
                       @NotNull String data,
                       @NotNull String blockNumber,
                       @NotNull String transactionHash,
                       @NotNull String transactionIndex,
                       @NotNull String blockHash,
                       @NotNull String logIndex,
                       boolean removed) {
        static Type LIST_TYPE = new TypeToken<ArrayList<LogResponse>>() {
        }.getType();
    }

}
