package dev.klepto.kweb3;

import dev.klepto.kweb3.abi.type.Address;
import dev.klepto.kweb3.abi.type.Tuple;
import dev.klepto.kweb3.abi.type.Uint;
import dev.klepto.kweb3.contract.Function;
import dev.klepto.kweb3.contract.ValueType;
import lombok.Value;
import lombok.With;

import java.util.List;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
@With
public class Web3Request {

    Address contractAddress;
    Address callerAddress;
    Function function;
    Uint value;
    Tuple parameters;
    List<Event> events;

    public static class Event {
        String name;
        List<ValueType> valueTypes;
    }

}
