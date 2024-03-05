package dev.klepto.kweb3.core.abi.descriptor;

import dev.klepto.kweb3.core.type.EthBytes;
import dev.klepto.kweb3.core.type.EthInt;
import dev.klepto.kweb3.core.type.EthSizedType;
import dev.klepto.kweb3.core.type.EthUint;
import dev.klepto.unreflect.UnreflectType;
import lombok.With;
import lombok.val;

import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * An ABI type descriptor for a sized ethereum data type.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 * @see dev.klepto.kweb3.core.type.EthBytes
 * @see dev.klepto.kweb3.core.type.EthInt
 * @see dev.klepto.kweb3.core.type.EthUint
 */
@With
public record EthSizedTypeDescriptor(UnreflectType type, int valueSize) implements TypeDescriptor {

    public EthSizedTypeDescriptor {
        val isInt = type.matchesExact(EthInt.class);
        val isUint = type.matchesExact(EthUint.class);
        val isBytes = type.matchesExact(EthBytes.class);
        require(isInt || isUint || isBytes, "Unsupported sized descriptor type: {}", type);
    }

    public EthSizedTypeDescriptor(Class<? extends EthSizedType> type, int valueSize) {
        this(UnreflectType.of(type), valueSize);
    }

    @Override
    public String toAbiDescriptor() {
        val valueDescriptor = valueSize > 0 ? valueSize + "" : "";
        return TypeDescriptor.super.toAbiDescriptor() + valueDescriptor;
    }

    @Override
    public String toString() {
        return toAbiDescriptor();
    }

}
