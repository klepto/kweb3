package dev.klepto.kweb3.core.ethereum.rpc.api;

import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.ethereum.rpc.RpcClient;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthProtocol {

    /**
     * Returns the current client.
     *
     * @return the current client
     */
    @NotNull
    default RpcClient client() {
        require(this instanceof RpcClient, "EthProtocol must be RpcClient");
        return (RpcClient) this;
    }

    /**
     * Returns the current latest block number.
     *
     * @return a hexadecimal of an integer representing the current block number the client is on
     */
    @NotNull
    default Web3Result<String> ethBlockNumber() {
        val request = new EthBlockNumberRequest();
        client().request(request);
        return request.resultAsString();
    }

    /**
     * Executes a new message call immediately without creating a transaction on the blockchain.
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
    default Web3Result<String> ethCall(@Nullable String from,
                                       @NotNull String to,
                                       @Nullable Integer gas,
                                       @Nullable String gasPrice,
                                       @Nullable String value,
                                       @NotNull String data,
                                       @Nullable String blockNumber) {
        val request = new EthCallRequest(from, to, gas, gasPrice, value, data, blockNumber);
        client().request(request);
        return request.resultAsString();
    }

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
        val request = new EthEstimateGasRequest(from, to, gas, gasPrice, value, data, blockNumber);
        client().request(request);
        return request.resultAsString();
    }

    /**
     * Returns the current gas price in wei.
     *
     * @return the hexadecimal value of the current gas price in wei
     */
    @NotNull
    default Web3Result<String> ethGasPrice() {
        val request = new EthGasPriceRequest();
        client().request(request);
        return request.resultAsString();
    }

    /**
     * Returns information about a block by block number.
     *
     * @param blockNumber the block number in hexadecimal format
     * @return information about a block by block number
     */
    @NotNull
    default Web3Result<EthBlockRequest.BlockResponse> ethGetBlockByNumber(String blockNumber) {
        val request = new EthBlockRequest.ByNumber(blockNumber);
        client().request(request);
        return request.decode();
    }

    /**
     * Returns information about a block by block hash.
     *
     * @param blockHash the block hash
     * @return information about a block by block hash
     */
    @NotNull
    default Web3Result<EthBlockRequest.BlockResponse> ethGetBlockByHash(@NotNull String blockHash) {
        val request = new EthBlockRequest.ByHash(blockHash);
        client().request(request);
        return request.decode();
    }

    /**
     * Returns a list of all the logs matching the given filters. Note that available filters are endpoint dependent and
     * different node providers may include different restrictions on this method.
     *
     * @param addresses the array of smart contract address filters
     * @param fromBlock the block number in hexadecimal format or the string <code>latest</code>, <code>earliest</code>
     * @param toBlock   the block number in hexadecimal format or the string <code>latest</code>, <code>earliest</code>
     * @param topics    the array of topics filters
     * @param blockHash the {@code blockHash} filter
     * @return an array of all the logs matching the given filters
     */
    @NotNull
    default Web3Result<List<EthGetLogsRequest.LogResponse>> ethGetLogs(@Nullable String[] addresses,
                                                                       @Nullable String fromBlock,
                                                                       @Nullable String toBlock,
                                                                       @Nullable String[] topics,
                                                                       @Nullable String blockHash) {
        val request = new EthGetLogsRequest(addresses, fromBlock, toBlock, topics, blockHash);
        client().request(request);
        return request.decode();
    }

    /**
     * Submits a pre-signed transaction for broadcast to the Ethereum network.
     *
     * @param signedTransactionData the signed transaction data (typically signed with a library, using your private
     *                              key)
     * @return the transaction hash, or the zero hash if the transaction is not yet available
     */
    @NotNull
    default Web3Result<String> ethSendRawTransaction(@NotNull String signedTransactionData) {
        val request = new EthSendRawTransactionRequest(signedTransactionData);
        client().request(request);
        return request.resultAsString();
    }

    /**
     * Subscribes to specific events on the Ethereum network, such as new blocks, new pending transactions, or changes
     * in the state of an account. When an event occurs, a notification is sent to the client with the corresponding
     * data. To stop receiving notifications, the client can unsubscribe using <code>eth_unsubscribe</code>.
     *
     * @param method  the subscription method, <code>newHeads</code>, <code>logs</code>,
     *                <code>newPendingTransactions</code> or <code>syncing</code>
     * @param address the smart contract address filter or <code>null</code>, only applicable to <code>logs</code>
     *                method
     * @param topics  the array of topics filters or <code>null</code>, only applicable to <code>logs</code> method
     * @return a unique subscription id that can be used to unsubscribe or identify incoming notifications.
     */
    @NotNull
    default Web3Result<String> ethSubscribe(@NotNull String method,
                                            @Nullable String address,
                                            @Nullable String[] topics) {
        val request = new EthSubscribeRequest(method, address, topics);
        client().request(request);
        return request.resultAsString();
    }

    /**
     * Unsubscribes from specific events on the Ethereum network, to which the client has been previously subscribed
     * using <code>eth_subscribe</code>. The client must provide the <code>subscriptionId</code> obtained from
     * <code>eth_subscribe</code> to stop receiving notifications for the corresponding event.
     *
     * @param subscriptionId the unique subscription id obtained from <code>eth_subscribe</code>
     * @return <code>true</code> if the unsubscription was successful, <code>false</code> if subscription did not exist
     */
    @NotNull
    default Web3Result<Boolean> ethUnsubscribe(@NotNull String subscriptionId) {
        val request = new EthUnsubscribeRequest(subscriptionId);
        client().request(request);
        return request.result().map(response -> response.resultAs(boolean.class));
    }

}
