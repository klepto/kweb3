package dev.klepto.kweb3.contract.impl;

import dev.klepto.kweb3.Web3Response;
import dev.klepto.kweb3.abi.type.Address;
import dev.klepto.kweb3.abi.type.Uint;
import dev.klepto.kweb3.contract.Event;
import dev.klepto.kweb3.contract.Transaction;
import dev.klepto.kweb3.contract.Cost;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface WrappedToken extends Erc20 {

    @lombok.Value
    @Event("Deposit")
    class DepositEvent {
        @Event.Address Address eventAddress;
        @Event.Indexed Address address;
        Uint amount;
    }

    @lombok.Value
    @Event("Withdrawal")
    class WithdrawalEvent {
        @Event.Address Address eventAddress;
        @Event.Indexed Address address;
        Uint amount;
    }

    @Transaction
    Web3Response deposit(@Cost Uint amount);

    @Transaction
    Web3Response withdraw(Uint amount);

}
