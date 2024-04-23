package dev.klepto.kweb3.core.ethereum;

import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.chain.Web3Endpoint;
import dev.klepto.kweb3.core.ethereum.rpc.RpcClient;
import dev.klepto.kweb3.core.ethereum.rpc.io.RpcConnectionProvider;
import dev.klepto.kweb3.core.ethereum.type.data.EthBlock;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthUint;
import lombok.Getter;

import java.io.Closeable;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * A client for interacting with a blockchain metadata related RPC methods.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class EthereumClient implements Closeable {

    private final @Getter RpcClient rpc;
    private final EthereumSubscriber subscriber;

    /**
     * Creates a new ethereum client that connects to given RPC endpoints.
     *
     * @param endpoints the RPC endpoints
     */
    public EthereumClient(Web3Endpoint... endpoints) {
        this.rpc = new RpcClient(new RpcConnectionProvider(Arrays.asList(endpoints)));
        this.subscriber = new EthereumSubscriber(rpc);
        subscriber.start();
    }

    /**
     * Subscribes to new block headers.
     *
     * @param consumer the consumer to be called when a new block header is received
     */
    public void blockSubscribe(Consumer<EthBlock> consumer) {
        subscriber.subscribe(EthereumSubscriber.SubscriberType.NEW_BLOCKS, consumer);
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
     * Gets the latest block.
     *
     * @return the latest block
     */
    public Web3Result<EthBlock> blockLatest() {
        return rpc.ethGetBlockByNumber("latest")
                .map(EthBlock::parse);
    }

    /**
     * Gets the block with the specified number.
     *
     * @param blockNumber the block number
     * @return the block
     */
    public Web3Result<EthBlock> blockByNumber(EthUint blockNumber) {
        return rpc.ethGetBlockByNumber(blockNumber.toHex())
                .map(EthBlock::parse);
    }

    /**
     * Gets the block with the specified hash.
     *
     * @param blockHash the block hash
     * @return the block
     */
    public Web3Result<EthBlock> blockByHash(String blockHash) {
        return rpc.ethGetBlockByHash(blockHash)
                .map(EthBlock::parse);
    }

    /**
     * Closes the ethereum client and releases all resources.
     */
    @Override
    public void close() {
        rpc.close();
        subscriber.close();
    }

}
