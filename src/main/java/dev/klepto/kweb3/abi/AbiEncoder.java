package dev.klepto.kweb3.abi;

import dev.klepto.kweb3.abi.type.AbiType;
import dev.klepto.kweb3.abi.type.AbiValue;
import dev.klepto.kweb3.abi.type.Tuple;
import dev.klepto.kweb3.abi.type.util.Types;

import static dev.klepto.kweb3.abi.type.util.Types.tuple;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface AbiEncoder {

    default <T extends AbiValue> String encode(T value) {
        return encode(Types.tuple(value), value.getType());
    }

    default String encode(Tuple values) {
        return encode(values, values.getType());
    }
    
    String encode(Tuple values, AbiType type);

}
