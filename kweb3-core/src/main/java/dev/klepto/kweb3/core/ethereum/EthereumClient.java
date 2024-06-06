package dev.klepto.kweb3.core.ethereum;

import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.chain.Web3Endpoint;
import dev.klepto.kweb3.core.ethereum.rpc.RpcClient;
import dev.klepto.kweb3.core.ethereum.rpc.RpcClientListener;
import dev.klepto.kweb3.core.ethereum.type.data.EthBlock;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthUint;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;

/**
 * A client for interacting with a blockchain metadata related RPC methods.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public class EthereumClient implements Closeable {

    private final RpcClient rpc;

    /**
     * Creates a new ethereum client that connects to given RPC endpoints.
     *
     * @param endpoint the RPC endpoints
     */
    public EthereumClient(Web3Endpoint endpoint, @Nullable RpcClientListener listener) {
        this.rpc = new RpcClient(endpoint, listener);
    }

    public EthereumClient(Web3Endpoint endpoint) {
        this(endpoint, null);
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
    public Web3Result<EthBlock> block() {
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
    }

}
