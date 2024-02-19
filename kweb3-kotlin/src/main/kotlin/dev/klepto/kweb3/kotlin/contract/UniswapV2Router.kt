package dev.klepto.kweb3.kotlin.contract

import dev.klepto.kweb3.core.contract.Web3Contract
import dev.klepto.kweb3.core.contract.annotation.View
import dev.klepto.kweb3.core.type.EthAddress
import dev.klepto.kweb3.core.type.EthArray
import dev.klepto.kweb3.core.type.EthUint

/**
 * Implementation of Uniswap V2 router smart contract.
 *
 * Reference:
 * [UniswapV2Router02.sol](https://github.com/Uniswap/v2-periphery/blob/master/contracts/UniswapV2Router02.sol)
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
interface UniswapV2Router : Web3Contract {

    /**
     * Returns the [UniswapV2Factory] contract address.
     *
     * @return the factory contract address
     */
    @View
    suspend fun factory(): EthAddress

    /**
     * Returns the native ERC-20 token address.
     *
     * @return the native token address
     */
    @View
    suspend fun WETH(): EthAddress

    /**
     * Calculates the amount of tokens that will be received for a given input
     * amount.
     *
     * @param path the token trade path
     * @return the token output amount at a current block
     */
    @View
    suspend fun getAmountsOut(amountIn: EthUint, path: EthArray<EthAddress>): EthArray<EthUint>

    /**
     * Calculates the amount of tokens to input, in order to receive the given
     * output amount.
     *
     * @param path the token trade path
     * @return the token input amount at a current block to receive the exact
     *     output amount
     */
    @View
    suspend fun getAmountsIn(amountOut: EthUint, path: EthArray<EthAddress>): EthArray<EthUint>

}