package dev.klepto.kweb3.kotlin.contracts

import dev.klepto.kweb3.core.contract.Web3Contract
import dev.klepto.kweb3.core.contract.annotation.ValueSize
import dev.klepto.kweb3.core.contract.annotation.View
import dev.klepto.kweb3.core.type.EthAddress
import dev.klepto.kweb3.core.type.EthString
import dev.klepto.kweb3.core.type.EthUint

/**
 * Suspending implementation of ERC-20 standard and commonly available
 * functions of ERC-20 tokens.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
interface Erc20 : Web3Contract {

    /**
     * Returns the name of the token.
     *
     * @return the name of this token
     */
    @View
    suspend fun name(): EthString

    /**
     * Returns the symbol of the token, usually a shorter version of the
     * [Erc20.name].
     *
     * @return the symbol of this token
     */
    @View
    suspend fun symbol(): EthString


    /**
     * Returns the value of tokens in existence represented as `uint256`.
     *
     * @return the value of tokens in existence
     */
    @View
    suspend fun totalSupply(): EthUint

    /**
     * Returns the number of decimals used for token amount user
     * representation. Most tokens use value of `18`, imitating the
     * relationship between Ether and Wei. This information is only used
     * for display purposes and does not influence arithmetic of the token
     * contract.
     *
     * @return the number of decimals used to represent token values
     */
    @View
    @ValueSize(8)
    suspend fun decimals(): EthUint

    /**
     * Returns the value of tokens owned by a given account
     * [address][EthAddress].
     *
     * @param account the address of the account
     * @return the value of tokens owned by given account address
     */
    @View
    suspend fun balanceOf(account: EthAddress): EthUint

    /**
     * Returns the remaining amount of tokens that `spender` will be allowed to
     * spend on behalf of `owner` through `transferFrom` function. This value
     * is zero by default.
     *
     * @param owner the address of the owner account
     * @param spender the address of the spender
     * @return the remaining amount of tokens that spender is allowed to spend
     *     on behalf of owner
     */
    @View
    suspend fun allowance(owner: EthAddress, spender: EthAddress): EthUint

}
