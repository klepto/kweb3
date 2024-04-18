package dev.klepto.kweb3.kotlin.contracts

import dev.klepto.kweb3.core.contract.annotation.ValueSize
import dev.klepto.kweb3.core.contract.annotation.View
import dev.klepto.kweb3.core.contract.type.EthTupleContainer
import dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress
import dev.klepto.kweb3.core.ethereum.type.primitive.EthUint

/**
 * Implementation of Uniswap V2 pair smart contract.
 *
 * Reference:
 * [UniswapV2Pair.sol](https://github.com/Uniswap/v2-core/blob/master/contracts/UniswapV2Pair.sol)
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
interface UniswapV2Pair : Erc20 {

    /**
     * Returns the minimum liquidity value. This value represents the amount of
     * liquidity tokens burned from the first liquidity provider in-order to
     * deter malicious pricing.
     *
     * @return the minimum liquidity value
     */
    @View
    suspend fun MINIMUM_LIQUIDITY(): EthUint

    /**
     * Returns the [UniswapV2Factory] smart contract address.
     *
     * @return the factory contract address
     */
    @View
    suspend fun factory(): EthAddress

    /**
     * Returns the first token address of the pair. Pair tokens are sorted by
     * numerical address value, which means `token0` address value is always
     * smaller than `token1` address value.
     *
     * @return the first token smart contract address
     */
    @View
    suspend fun token0(): EthAddress

    /**
     * Returns the second token address of the pair. Pair tokens are sorted by
     * numerical address value, which means `token1` address value is always
     * bigger than `token0` address value.
     *
     * @return the second token smart contract address
     */
    @View
    suspend fun token1(): EthAddress

    /**
     * Returns the current liquidity reserves of the pair.
     *
     * @return the pair reserves
     */
    @View
    suspend fun getReserves(): Reserves

    /**
     * Returns the latest cumulative price of the first token.
     *
     * @return the latest price of the first token
     */
    @View
    suspend fun price0CumulativeLast(): EthUint

    /**
     * Returns the latest cumulative price of the second token.
     *
     * @return the latest price of the second token
     */
    @View
    suspend fun price1CumulativeLast(): EthUint

    /**
     * Returns the latest product value of the pair reserves. This is equal to
     * `reserve0 * reserve1` and is set on every reserve update.
     *
     * @return the latest product value of the pair reserves
     */
    @View
    suspend fun kLast(): EthUint

    /**
     * Reserves container for [UniswapV2Pair.getReserves] response.
     *
     * @param reserve0 the reserves of the first token
     * @param reserve1 the reserves of the second token
     * @param blockTimestampLast the timestamp of last reserves update
     */
    data class Reserves(
        @field:ValueSize(112) val reserve0: EthUint,
        @field:ValueSize(112) val reserve1: EthUint,
        @field:ValueSize(32) val blockTimestampLast: EthUint
    ) : EthTupleContainer

}