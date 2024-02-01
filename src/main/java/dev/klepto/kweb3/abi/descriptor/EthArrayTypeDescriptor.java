package dev.klepto.kweb3.abi.descriptor;

import dev.klepto.unreflect.UnreflectType;
import lombok.With;
import lombok.val;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public record EthArrayTypeDescriptor(TypeDescriptor descriptor, int arraySize) implements TypeDescriptor {

    public EthArrayTypeDescriptor(TypeDescriptor descriptor) {
        this(descriptor, -1);
    }

    @Override
    public UnreflectType type() {
        return descriptor.type();
    }

    @Override
    public String toAbiDescriptor() {
        val arrayDescriptor = "[" + (arraySize > 0 ? arraySize : "") + "]";
        return descriptor.toAbiDescriptor() + arrayDescriptor;
    }

}