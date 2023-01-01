package dev.klepto.kweb3.contract.impl;

import dev.klepto.kweb3.Web3Response;
import dev.klepto.kweb3.abi.type.Address;
import dev.klepto.kweb3.abi.type.Uint;
import dev.klepto.kweb3.contract.*;
import lombok.Value;


import static dev.klepto.kweb3.contract.Cache.INDEFINITE;

/**
 * Simple implementation of Erc20 contract proxy.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Erc20 extends Contract {

    @Value
    @Event("Transfer")
    class TransferEvent {
        @Event.Address Address eventAddress;
        @Event.Indexed Address sender;
        @Event.Indexed Address receiver;
        Uint value;
    }

    @Value
    @Event("Approval")
    class ApprovalEvent {
        @Event.Address Address eventAddress;
        @Event.Indexed Address owner;
        @Event.Indexed Address spender;
        Uint value;
    }

    @View
    @Cache(INDEFINITE)
    String name();

    @View
    @Cache(INDEFINITE)
    String symbol();

    @View
    @Type(value = Uint.class, valueSize = 8)
    @Cache(INDEFINITE)
    Uint decimals();

    @View
    Uint totalSupply();

    @View
    Uint balanceOf(Address account);

    @View
    Uint allowance(Address owner, Address spender);

    @Transaction
    Web3Response transfer(Address to, Uint amount);

    @Transaction
    Web3Response transferFrom(Address from, Address to, Uint amount);

    @Transaction
    Web3Response approve(Address spender, Uint amount);

    @Transaction
    Web3Response increaseAllowance(Address spender, Uint addedValue);

    @Transaction
    Web3Response decreaseAllowance(Address spender, Uint subtractedValue);

}
