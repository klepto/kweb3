package dev.klepto.kweb3.abi.descriptor;

import dev.klepto.kweb3.type.EthSizedType;
import dev.klepto.unreflect.UnreflectType;
import lombok.With;
import lombok.val;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
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
