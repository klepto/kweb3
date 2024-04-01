package dev.klepto.kweb3.core.rpc.protocol.api;

import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.rpc.protocol.RpcMethod;
import dev.klepto.kweb3.core.rpc.protocol.RpcRequest;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of Ethereum RPC API <code>eth_getBlockByNumber</code> and <code>eth_getBlockByHash</code> methods.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthGetBlock extends RpcMethod {

    /**
     * Returns information about a block by block number.
     *
     * @param blockNumber the block number in hexadecimal format
     * @return information about a block by block number
     */
    @NotNull
    default Web3Result<BlockResponse> ethGetBlockByNumber(String blockNumber) {
        val request = new RpcRequest()
                .withMethod("eth_getBlockByNumber")
                .withParams(blockNumber, false);
        return request(request)
                .map(result -> result.resultAs(BlockResponse.class));
    }

    /**
     * Returns information about a block by block hash.
     *
     * @param blockHash the block hash
     * @return information about a block by block hash
     */
    @NotNull
    default Web3Result<BlockResponse> ethGetBlockByHash(@NotNull String blockHash) {
        val request = new RpcRequest()
                .withMethod("eth_getBlockByHash")
                .withParams(blockHash, false);
        return request(request)
                .map(result -> result.resultAs(BlockResponse.class));
    }

    /**
     * Represents <code>eth_getBlockByNumber</code> and <code>eth_getBlockByHash</code> result object.
     *
     * @param baseFeePerGas    a hexadecimal number of the base fee per gas in wei
     * @param difficulty       a hexadecimal of the difficulty for this block
     * @param extraData        the "extra data" field of this block
     * @param gasLimit         maximum gas allowed in this block
     * @param gasUsed          total used gas by all transactions in this block
     * @param hash             the hash of the block, or {@code null} when the returned block is the pending block
     * @param logsBloom        the bloom filter for the logs of the block
     * @param miner            the address of the beneficiary to whom the mining rewards were given, or {@code null}
     *                         when the returned block is the pending block
     * @param mixHash          a 256-bit hash which proves that a sufficient amount of computation has been carried out
     *                         on this block, or  {@code null} when the returned block is the pending block
     * @param nonce            the hash of the generated proof-of-work, or {@code null} when the returned block is the
     *                         pending block
     * @param number           the block number, or {@code null} when the returned block is the pending block
     * @param parentHash       the hash of the parent block
     * @param receiptsRoot     the root of the receipts trie of the block
     * @param sha3Uncles       the SHA3 of the uncles data in the block
     * @param size             a hexadecimal of the size of this block in bytes
     * @param stateRoot        the root of the final state trie of the block
     * @param timestamp        unix timestamp for when the block was collated
     * @param totalDifficulty  a hexadecimal of the total difficulty of the chain until this block
     * @param transactions     an array of transaction hashes
     * @param transactionsRoot the root of the transaction trie of the block
     * @param uncles           an array of uncle hashes
     */
    record BlockResponse(
            @NotNull String baseFeePerGas,
            @NotNull String difficulty,
            @NotNull String extraData,
            @NotNull String gasLimit,
            @NotNull String gasUsed,
            @Nullable String hash,
            @NotNull String logsBloom,
            @Nullable String miner,
            @NotNull String mixHash,
            @Nullable String nonce,
            @NotNull String number,
            @NotNull String parentHash,
            @NotNull String receiptsRoot,
            @NotNull String sha3Uncles,
            @NotNull String size,
            @NotNull String stateRoot,
            @NotNull String timestamp,
            @NotNull String totalDifficulty,
            @NotNull String[] transactions,
            @NotNull String transactionsRoot,
            @NotNull String[] uncles
    ) {
    }


}
