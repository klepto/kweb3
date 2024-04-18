package dev.klepto.kweb3.kotlin.contracts

import dev.klepto.kweb3.core.contract.Web3Contract
import dev.klepto.kweb3.core.contract.annotation.View
import dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress
import dev.klepto.kweb3.core.ethereum.type.primitive.EthUint

/**
 * Implementation of Uniswap V2 factory smart contract.
 *
 * Reference:
 * [UniswapV2Factory.sol](https://github.com/Uniswap/v2-core/blob/master/contracts/UniswapV2Factory.sol)
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
interface UniswapV2Factory : Web3Contract {

    /**
     * Returns the address that receives protocol fees.
     *
     * @return the address receiving protocol fees
     */
    @View
    suspend fun feeTo(): EthAddress

    /**
     * Returns the address that is able to change protocol fee receiver.
     *
     * @return the contract owner/manager address that can change protocol fee
     *     receiver
     */
    @View
    suspend fun feeToSetter(): EthAddress

    /**
     * Returns the [UniswapV2Pair] contract address for a given token pair or
     * [EthAddress.ZERO] if pair does not exist. Token order does not matter.
     *
     * @param tokenA the first token address
     * @param tokenB the second token address
     * @return the pair smart contract address
     */
    @View
    suspend fun getPair(tokenA: EthAddress, tokenB: EthAddress): EthAddress

    /**
     * Returns the amount of token pairs created by this factory contract.
     *
     * @return the amount of token pairs created by this factory
     */
    @View
    suspend fun allPairsLength(): EthUint

    /**
     * Returns the address of the token [UniswapV2Pair] contract at a given
     * index.
     *
     * @param index the token pair index
     * @return the token pair contract address
     */
    @View
    suspend fun allPairs(index: EthUint): EthAddress

}