package dev.klepto.kweb3.core.abi.descriptor;

import dev.klepto.kweb3.core.type.EthArray;
import dev.klepto.unreflect.UnreflectType;
import lombok.With;
import lombok.val;

/**
 * An ABI type descriptor for {@link EthArray} ethereum data type.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 * @see dev.klepto.kweb3.core.type.EthArray
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

    @Override
    public String toString() {
        return toAbiDescriptor();
    }

}