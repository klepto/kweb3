package dev.klepto.kweb3.core.ethereum.abi.descriptor;

import dev.klepto.kweb3.core.ethereum.type.EthNumeric;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthBytes;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthInt;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthUint;
import dev.klepto.unreflect.UnreflectType;
import lombok.With;
import lombok.val;

import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * An ABI type descriptor for a sized ethereum data type.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 * @see EthBytes
 * @see EthInt
 * @see EthUint
 */
@With
public record EthSizedTypeDescriptor(UnreflectType type, int valueSize) implements TypeDescriptor {

    public EthSizedTypeDescriptor {
        val isInt = type.matches(EthInt.class);
        val isUint = type.matches(EthUint.class);
        val isBytes = type.matches(EthBytes.class);
        require(isInt || isUint || isBytes, "Unsupported sized descriptor type: {}", type);
    }

    public EthSizedTypeDescriptor(Class<? extends EthNumeric<?>> type, int valueSize) {
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
