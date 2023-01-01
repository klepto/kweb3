package dev.klepto.kweb3.abi;

import dev.klepto.kweb3.abi.type.AbiType;
import dev.klepto.kweb3.abi.type.AbiValue;
import dev.klepto.kweb3.abi.type.Struct;

import static dev.klepto.kweb3.abi.type.util.Types.struct;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface AbiEncoder {

    default <T extends AbiValue> String encode(T value) {
        return encode(struct(value), value.getType());
    }

    default String encode(Struct values) {
        return encode(values, values.getType());
    }
    
    String encode(Struct values, AbiType type);

}
