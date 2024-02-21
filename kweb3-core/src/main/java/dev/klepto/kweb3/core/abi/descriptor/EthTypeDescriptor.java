package dev.klepto.kweb3.core.abi.descriptor;

import dev.klepto.kweb3.core.type.EthType;
import dev.klepto.unreflect.UnreflectType;
import lombok.With;

/**
 * An ABI type descriptor for a simple ethereum data type.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 * @see dev.klepto.kweb3.core.type.EthAddress
 * @see dev.klepto.kweb3.core.type.EthString
 * @see dev.klepto.kweb3.core.type.EthBool
 */
@With
public record EthTypeDescriptor(UnreflectType type) implements TypeDescriptor {

    public EthTypeDescriptor(Class<? extends EthType> type) {
        this(UnreflectType.of(type));
    }

    @Override
    public String toString() {
        return toAbiDescriptor();
    }

}
