package dev.klepto.kweb3.core.ethereum.type;

import dev.klepto.kweb3.core.ethereum.type.primitive.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Represents <i>ethereum virtual machine</i> value. Acts as intermediary for encoding/decoding between JVM and
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

    /**
     * Validates the value, size and range constraints of ethereum type.
     *
     * @return the invalid value reason, or null if the value is valid
     */
    @Nullable
    default InvalidValue validate() {
        return null;
    }

    /**
     * Enumeration of invalid value reasons.
     */
    enum InvalidValue {
        OUT_OF_RANGE,
        UNSUPPORTED_VALUE,
        UNSUPPORTED_SIZE
    }

}
