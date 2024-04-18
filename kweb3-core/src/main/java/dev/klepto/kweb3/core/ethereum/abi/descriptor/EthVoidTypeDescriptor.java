package dev.klepto.kweb3.core.ethereum.abi.descriptor;

import dev.klepto.kweb3.core.contract.type.EthVoid;
import dev.klepto.unreflect.UnreflectType;

/**
 * An ABI type descriptor for {@link EthVoid} ethereum data type.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public record EthVoidTypeDescriptor() implements TypeDescriptor {

    @Override
    public UnreflectType type() {
        return UnreflectType.of(EthVoid.class);
    }

    @Override
    public String toAbiDescriptor() {
        return "";
    }
}
