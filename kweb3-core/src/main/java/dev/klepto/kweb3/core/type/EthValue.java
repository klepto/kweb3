package dev.klepto.kweb3.core.type;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Represents <i>ethereum virtual machine</i> value. Acts as intermediary for encoding/decoding between java and
 * blockchain data-types.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthValue {

    Map<Class<? extends EthValue>, String> SOLIDITY_NAMES = Map.of(
            EthAddress.class, "address",
            EthBool.class, "bool",
            EthBytes.class, "bytes",
            EthInt.class, "int",
            EthString.class, "string",
            EthUint.class, "uint"
    );

    /**
     * Returns solidity name equivalent of given <code>ethereum value</code> type.
     *
     * @param type the type class
     * @return the solidity name of given <code>ethereum value</code> type or empty <code>String</code> if type does not
     * have a name
     */
    @NotNull
    static String getSolidityName(Class<?> type) {
        return SOLIDITY_NAMES.getOrDefault(type, "");
    }

}
