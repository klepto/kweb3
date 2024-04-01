package dev.klepto.kweb3.core.blockchain;

import dev.klepto.kweb3.core.type.EthAddress;
import dev.klepto.kweb3.core.type.EthBytes;
import dev.klepto.kweb3.core.type.EthUint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A representation of a parsed <code>ethereum block</code> from one of the RPC endpoint methods.
 *
 * @param baseFeePerGas    number of the base fee per gas in wei
 * @param difficulty       the difficulty for this block
 * @param extraData        the "extra data" field of this block
 * @param gasLimit         maximum gas allowed in this block
 * @param gasUsed          total used gas by all transactions in this block
 * @param hash             the hash of the block, or {@code null} when the returned block is the pending block
 * @param logsBloom        the bloom filter for the logs of the block
 * @param miner            the address of the beneficiary to whom the mining rewards were given, or {@code null} when
 *                         the returned block is the pending block
 * @param mixHash          a 256-bit hash which proves that a sufficient amount of computation has been carried out on
 *                         this block, or  {@code null} when the returned block is the pending block
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
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public record EthBlock(
        @NotNull EthUint baseFeePerGas,
        @NotNull EthUint difficulty,
        @NotNull EthBytes extraData,
        @NotNull EthUint gasLimit,
        @NotNull EthUint gasUsed,
        @Nullable String hash,
        @NotNull EthBytes logsBloom,
        @Nullable EthAddress miner,
        @NotNull String mixHash,
        @Nullable EthUint nonce,
        @NotNull EthUint number,
        @NotNull String parentHash,
        @NotNull String receiptsRoot,
        @NotNull String sha3Uncles,
        @NotNull EthUint size,
        @NotNull String stateRoot,
        @NotNull EthUint timestamp,
        @NotNull EthUint totalDifficulty,
        @NotNull List<String> transactions,
        @NotNull String transactionsRoot,
        @NotNull List<String> uncles
) {
}
