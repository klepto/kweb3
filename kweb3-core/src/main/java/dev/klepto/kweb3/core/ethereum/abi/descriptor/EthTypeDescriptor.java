package dev.klepto.kweb3.core.ethereum.abi.descriptor;

import dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthBool;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthString;
import dev.klepto.kweb3.core.ethereum.type.EthValue;
import dev.klepto.unreflect.UnreflectType;
import lombok.With;
import lombok.val;

import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * An ABI type descriptor for a simple ethereum data type.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 * @see EthAddress
 * @see EthString
 * @see EthBool
 */
@With
public record EthTypeDescriptor(UnreflectType type) implements TypeDescriptor {

    public EthTypeDescriptor {
        val isString = type.matches(EthString.class);
        val isBool = type.matches(EthBool.class);
        val isAddress = type.matches(EthAddress.class);
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
