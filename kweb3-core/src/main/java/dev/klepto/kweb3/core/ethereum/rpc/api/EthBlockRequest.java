package dev.klepto.kweb3.core.ethereum.rpc.api;

import dev.klepto.kweb3.core.Web3Result;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of Ethereum RPC API <code>eth_getBlockByNumber</code> and <code>eth_getBlockByHash</code> methods.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthBlockRequest {

    /**
     * Returns the result of this request.
     *
     * @return the result of this request
     */
    Web3Result<RpcApiResponseMessage> result();

    /**
     * Decodes the result of this request into {@link BlockResponse} object.
     *
     * @return the result of this request as a {@link BlockResponse} object
     */
    default Web3Result<BlockResponse> decode() {
        return result().map(result -> result.resultAs(BlockResponse.class));
    }

    /**
     * Implementation of Ethereum RPC API <code>eth_getBlockByHash</code> method.
     */
    @Getter
    @RequiredArgsConstructor
    class ByHash extends RpcApiRequest implements EthBlockRequest {

        private final String blockHash;

        /**
         * Encodes the request object into an API message.
         *
         * @return the api message
         */
        @Override
        public RpcApiRequestMessage encode() {
            return new RpcApiRequestMessage()
                    .withMethod("eth_getBlockByHash")
                    .withParams(blockHash, false);
        }
    }

    /**
     * Implementation of Ethereum RPC API <code>eth_getBlockByNumber</code> method.
     */
    @Getter
    @RequiredArgsConstructor
    class ByNumber extends RpcApiRequest implements EthBlockRequest {

        private final String blockNumber;

        /**
         * Encodes the request object into an API message.
         *
         * @return the api message
         */
        @Override
        public RpcApiRequestMessage encode() {
            return new RpcApiRequestMessage()
                    .withMethod("eth_getBlockByNumber")
                    .withParams(blockNumber, false);
        }
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
