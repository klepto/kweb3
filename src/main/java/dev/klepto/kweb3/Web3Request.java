package dev.klepto.kweb3;

import dev.klepto.kweb3.contract.Function;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.sized.Uint256;
import lombok.Value;

import java.util.List;

/**
 * Contains basic information about web3 contract request.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
public class Web3Request {

    Address address;
    Address callerAddress;
    Function function;
    List<Object> parameters;
    Uint256 value;
    List<Event> eventTypes;

    @Value
    public static class Event {
        String name;
        List<Class<?>> valueTypes;
        boolean[] indexedValues;
    }

}
