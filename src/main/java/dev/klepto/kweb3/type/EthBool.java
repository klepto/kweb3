package dev.klepto.kweb3.type;

import lombok.val;

import static dev.klepto.kweb3.error.Conditions.require;

/**
 * Represents ethereum bool data type.
 *
 * @param value true or false <code>boolean</code> value
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public record EthBool(boolean value) implements EthType {

    /* Solidity style bool initializers */

    /**
     * Converts JVM {@link boolean} type to ethereum bool type.
     */
    public static EthBool bool(boolean value) {
        return new EthBool(value);
    }

    /**
     * Converts ethereum {@link EthUint} type to ethereum bool type.
     */
    public static EthBool bool(EthUint value) {
        val intValue = value.value().intValue();
        require(intValue == 1 || intValue == 0, "Not a boolean value");
        return new EthBool(intValue == 1);
    }

    /**
     * Converts ethereum {@link EthInt} type to ethereum bool type.
     */
    public static EthBool bool(EthInt value) {
        val intValue = value.value().intValue();
        require(intValue == 1 || intValue == 0, "Not a boolean value");
        return new EthBool(intValue == 1);
    }

}
