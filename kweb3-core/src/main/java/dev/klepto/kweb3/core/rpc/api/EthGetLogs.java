package dev.klepto.kweb3.core.rpc.api;

import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.rpc.RpcMethod;
import dev.klepto.kweb3.core.rpc.RpcRequest;
import dev.klepto.kweb3.core.rpc.RpcResponse;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of Ethereum RPC API <code>eth_getLogs</code> method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthGetLogs extends RpcMethod {

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
    default Web3Result<String> ethGetLogs(@Nullable String[] addresses,
                                          @Nullable String fromBlock,
                                          @Nullable String toBlock,
                                          @Nullable String[] topics,
                                          @Nullable String blockHash) {
        val parameters = new Parameters(addresses, fromBlock, toBlock, topics, blockHash);
        val request = new RpcRequest()
                .withMethod("eth_getLogs")
                .withParams(parameters);
        return request(request)
                .map(RpcResponse::resultAsString);
    }

    /**
     * Represents <code>eth_getLogs</code> request parameters.
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

}
