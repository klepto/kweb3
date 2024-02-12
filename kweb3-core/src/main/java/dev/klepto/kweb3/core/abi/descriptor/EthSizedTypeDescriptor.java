package dev.klepto.kweb3.core.abi.descriptor;

import dev.klepto.kweb3.core.type.EthSizedType;
import dev.klepto.unreflect.UnreflectType;
import lombok.With;
import lombok.val;

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

    public EthSizedTypeDescriptor(Class<? extends EthSizedType> type, int valueSize) {
        this(UnreflectType.of(type), valueSize);
    }

    @Override
    public String toAbiDescriptor() {
        val valueDescriptor = valueSize > 0 ? valueSize + "" : "";
        return TypeDescriptor.super.toAbiDescriptor() + valueDescriptor;
    }

}
