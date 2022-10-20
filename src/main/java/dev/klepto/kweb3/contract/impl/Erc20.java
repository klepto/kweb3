package dev.klepto.kweb3.contract.impl;

import dev.klepto.kweb3.contract.*;
import dev.klepto.kweb3.contract.event.Event;
import dev.klepto.kweb3.contract.event.Indexed;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.sized.Uint256;
import dev.klepto.kweb3.type.sized.Uint8;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static dev.klepto.kweb3.contract.Cache.INDEFINITE;

/**
 * Simple implementation of Erc20 contract proxy.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Erc20 extends Contract {

    @lombok.Value
    @Event("Transfer")
    class TransferEvent {
        @Indexed Address sender;
        @Indexed Address receiver;
        Uint256 value;

        public static Stream<TransferEvent> find(ContractResponse response, Predicate<TransferEvent> predicate) {
            return response.getEvents(TransferEvent.class).stream().filter(predicate);
        }

        public static Stream<TransferEvent> findBySender(ContractResponse response) {
            return find(response, event -> event.getSender().equals(response.getRequest().getCallerAddress()));
        }

        public static Stream<TransferEvent> findByReceiver(ContractResponse response)  {
            return find(response, event -> event.getReceiver().equals(response.getRequest().getCallerAddress()));
        }

    }

    @lombok.Value
    @Event("Approval")
    class ApprovalEvent {
        @Indexed Address owner;
        @Indexed Address spender;
        Uint256 value;
    }

    @View
    @Cache(INDEFINITE)
    String name();

    @View
    @Cache(INDEFINITE)
    String symbol();

    @View
    @Cache(INDEFINITE)
    Uint8 decimals();

    @View
    Uint256 totalSupply();

    @View
    Uint256 balanceOf(Address account);

    @View
    Uint256 allowance(Address owner, Address spender);

    @Transaction
    ContractResponse transfer(Address to, Uint256 amount);

    @Transaction
    ContractResponse transferFrom(Address from, Address to, Uint256 amount);

    @Transaction
    ContractResponse approve(Address spender, Uint256 amount);

    @Transaction
    ContractResponse increaseAllowance(Address spender, Uint256 addedValue);

    @Transaction
    ContractResponse decreaseAllowance(Address spender, Uint256 subtractedValue);

}
