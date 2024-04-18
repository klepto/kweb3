package dev.klepto.kweb3.contracts;

import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.contract.Web3Contract;
import dev.klepto.kweb3.core.contract.annotation.ValueSize;
import dev.klepto.kweb3.core.contract.annotation.View;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthString;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthUint;

/**
 * Implementation of ERC-20 standard and commonly available functions of ERC-20 tokens.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Erc20 extends Web3Contract {

    /**
     * Returns the name of the token.
     *
     * @return the name of this token
     */
    @View
    Web3Result<EthString> name();

    /**
     * Returns the symbol of the token, usually a shorter version of the {@link Erc20#name()}.
     *
     * @return the symbol of this token
     */
    @View
    Web3Result<EthString> symbol();


    /**
     * Returns the value of tokens in existence represented as <code>uint256</code>.
     *
     * @return the value of tokens in existence
     */
    @View
    Web3Result<EthUint> totalSupply();

    /**
     * Returns the number of decimals used for token amount user representation. Most tokens use value of
     * <code>18</code>, imitating the relationship between Ether and Wei. This information is only used for display
     * purposes and does not influence arithmetic of the token contract.
     *
     * @return the number of decimals used to represent token values
     */
    @View
    @ValueSize(8)
    Web3Result<EthUint> decimals();

    /**
     * Returns the value of tokens owned by a given account {@link EthAddress address}.
     *
     * @param account the address of the account
     * @return the value of tokens owned by given account address
     */
    @View
    Web3Result<EthUint> balanceOf(EthAddress account);

    /**
     * Returns the remaining amount of tokens that `spender` will be allowed to spend on behalf of `owner` through
     * <code>transferFrom</code> function. This value is zero by default.
     *
     * @param owner   the address of the owner account
     * @param spender the address of the spender
     * @return the remaining amount of tokens that spender is allowed to spend on behalf of owner
     */
    @View
    Web3Result<EthUint> allowance(EthAddress owner, EthAddress spender);

}
