package dev.klepto.kweb3.core.blockchain;

import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.rpc.RpcClient;
import dev.klepto.kweb3.core.rpc.protocol.RpcMessage;
import dev.klepto.kweb3.core.rpc.protocol.RpcRequest;
import dev.klepto.kweb3.core.rpc.protocol.api.EthGetBlock;
import dev.klepto.kweb3.core.type.EthUint;
import lombok.val;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static dev.klepto.kweb3.core.type.EthAddress.address;
import static dev.klepto.kweb3.core.type.EthBytes.bytes;
import static dev.klepto.kweb3.core.type.EthUint.uint256;

/**
 * A client for interacting with a blockchain metadata related RPC methods.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class BlockchainClient {

    private final RpcClient rpc;
    private final Map<String, Consumer> subscribers = new ConcurrentHashMap<>();

    /**
     * Creates a new BlockchainClient instance for a given RPC client.
     *
     * @param rpc the RPC client
     */
    public BlockchainClient(RpcClient rpc) {
        this.rpc = rpc;
        rpc.addCallback(this::onMessage);
    }

    /**
     * Listens to received messages from RPC client and processes them notifying the appropriate subscribers.
     *
     * @param message the RPC message
     */
    private void onMessage(RpcMessage message) {
        if (!(message instanceof RpcRequest request)) {
            return;
        }

        if (!"eth_subscription".equals(request.method())) {
            return;
        }

        if (!request.params().isJsonObject()) {
            return;
        }

        val params = request.params().getAsJsonObject();
        if (!params.has("subscription") || !params.has("result")) {
            return;
        }

        val subscription = params.get("subscription").getAsString();
        val subscriber = subscribers.get(subscription);
        if (subscriber == null) {
            return;
        }

        val result = params.get("result").getAsJsonObject();
        if (!result.has("number")) {
            return;
        }

        val blockNumber = uint256(result.get("number").getAsString());
        block(blockNumber).get(subscriber);
    }

    /**
     * Subscribes to new block headers.
     *
     * @param consumer the consumer to be called when a new block header is received
     * @return the subscription id
     */
    public Web3Result<String> blockSubscribe(Consumer<EthBlock> consumer) {
        val result = rpc.ethSubscribe("newHeads", null, null);
        result.get(id -> subscribers.put(id, consumer));
        return result;
    }

    /**
     * Unsubscribes from a subscription.
     *
     * @param subscriptionId the subscription id
     */
    public void unsubscribe(String subscriptionId) {
        if (!subscribers.containsKey(subscriptionId)) {
            return;
        }

        subscribers.remove(subscriptionId);
        rpc.ethUnsubscribe(subscriptionId);
    }

    /**
     * Gets the current gas price in wei.
     *
     * @return the current gas price
     */
    public Web3Result<EthUint> gasPrice() {
        return rpc.ethGasPrice().map(EthUint::uint256);
    }

    /**
     * Gets the current block number.
     *
     * @return the current block number
     */
    public Web3Result<EthUint> blockNumber() {
        return rpc.ethBlockNumber().map(EthUint::uint256);
    }

    /**
     * Gets the block with the specified number.
     *
     * @param blockNumber the block number
     * @return the block
     */
    public Web3Result<EthBlock> block(EthUint blockNumber) {
        return rpc.ethGetBlockByNumber(blockNumber.toHex())
                .map(BlockchainClient::parseBlockResponse);
    }

    /**
     * Gets the block with the specified hash.
     *
     * @param blockHash the block hash
     * @return the block
     */
    public Web3Result<EthBlock> block(String blockHash) {
        return rpc.ethGetBlockByHash(blockHash)
                .map(BlockchainClient::parseBlockResponse);
    }

    /**
     * Parses a `eth_getBlockByNumber` or `eth_getBlockByHash` response into a {@link EthBlock}.
     *
     * @param response the block response to parse
     * @return the parsed block
     */
    private static EthBlock parseBlockResponse(EthGetBlock.BlockResponse response) {
        return new EthBlock(
                uint256(response.baseFeePerGas()),
                uint256(response.difficulty()),
                bytes(response.extraData()),
                uint256(response.gasLimit()),
                uint256(response.gasUsed()),
                response.hash(),
                bytes(response.logsBloom()),
                response.miner() == null ? null : address(response.miner()),
                response.mixHash(),
                response.nonce() == null ? null : uint256(response.nonce()),
                uint256(response.number()),
                response.parentHash(),
                response.receiptsRoot(),
                response.sha3Uncles(),
                uint256(response.size()),
                response.stateRoot(),
                uint256(response.timestamp()),
                uint256(response.totalDifficulty()),
                Arrays.asList(response.transactions()),
                response.transactionsRoot(),
                Arrays.asList(response.uncles())
        );
    }

}
