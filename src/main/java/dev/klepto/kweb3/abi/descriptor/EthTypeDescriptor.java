package dev.klepto.kweb3.abi.descriptor;

import dev.klepto.kweb3.type.EthType;
import dev.klepto.unreflect.UnreflectType;
import lombok.With;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public record EthTypeDescriptor(UnreflectType type) implements TypeDescriptor {

    public EthTypeDescriptor(Class<? extends EthType> type) {
        this(UnreflectType.of(type));
    }

}
