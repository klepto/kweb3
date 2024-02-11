package dev.klepto.kweb3.core.type;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Represents ethereum virtual machine data type. Acts as intermediary for encoding/decoding between java and blockchain
 * data-types.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthType {

    Map<Class<? extends EthType>, String> SOLIDITY_NAMES = Map.of(
            EthAddress.class, "address",
            EthBool.class, "bool",
            EthBytes.class, "bytes",
            EthInt.class, "int",
            EthString.class, "string",
            EthUint.class, "uint"
    );

    /**
     * Returns solidity name equivalent of given ethereum data type.
     *
     * @param type the type class
     * @return the solidity name of given ethereum data type or <code>null</code> if type does not have a name
     */
    @Nullable
    static String getSolidityName(Class<?> type) {
        return SOLIDITY_NAMES.get(type);
    }

}
