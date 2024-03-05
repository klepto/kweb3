package dev.klepto.kweb3.core.abi.descriptor;

import dev.klepto.kweb3.core.type.EthAddress;
import dev.klepto.kweb3.core.type.EthBool;
import dev.klepto.kweb3.core.type.EthString;
import dev.klepto.kweb3.core.type.EthValue;
import dev.klepto.unreflect.UnreflectType;
import lombok.With;
import lombok.val;

import static dev.klepto.kweb3.core.util.Conditions.require;

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

    public EthTypeDescriptor {
        val isString = type.matchesExact(EthString.class);
        val isBool = type.matchesExact(EthBool.class);
        val isAddress = type.matchesExact(EthAddress.class);
        require(isString || isBool || isAddress, "Unsupported simple descriptor type: " + type);
    }

    public EthTypeDescriptor(Class<? extends EthValue> type) {
        this(UnreflectType.of(type));
    }

    @Override
    public String toString() {
        return toAbiDescriptor();
    }

}
