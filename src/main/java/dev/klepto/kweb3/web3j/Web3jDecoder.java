package dev.klepto.kweb3.web3j;

import dev.klepto.kweb3.Web3Error;
import dev.klepto.kweb3.util.reflection.Reflection;
import lombok.val;
import org.web3j.abi.datatypes.*;

/**
 * Decodes Web3j types into solidity types.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Web3jDecoder {

    public Object decodeValue(Object value) {
        if (value instanceof DynamicArray) {
            return ((DynamicArray) value).getValue()
                    .stream()
                    .map(this::decodeValue)
                    .toArray();
        } else if (value instanceof Uint) {
            val className = "dev.klepto.kweb3.type.sized." + value.getClass().getSimpleName();
            return Reflection.create(className, ((Uint) value).getValue());
        } else if (value instanceof String) {
            return new dev.klepto.kweb3.type.Address((String) value);
        } else if (value instanceof Address) {
            return new dev.klepto.kweb3.type.Address(((Address) value).getValue());
        } else if (value instanceof DynamicBytes) {
            return new dev.klepto.kweb3.type.Bytes(((DynamicBytes) value).getValue());
        } else if (value instanceof Utf8String) {
            return ((Utf8String) value).getValue();
        }

        throw new Web3Error("Couldn't decode web3j value {} of type {}.", value, value.getClass());
    }

}
