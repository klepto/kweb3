package dev.klepto.kweb3.core.type;

import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * Represents ethereum bool data type.
 *
 * @param value true or false <code>boolean</code> value
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public record EthBool(boolean value) implements EthType {

    /**
     * True bool constant.
     */
    public static final EthBool TRUE = bool(true);

    /**
     * False bool constant.
     */
    public static final EthBool FALSE = bool(false);

    /* Solidity style bool initializers */

    /**
     * Converts JVM {@link boolean} type to ethereum bool type.
     */
    public static EthBool bool(boolean value) {
        return new EthBool(value);
    }

    @Override
    public String toString() {
        return "bool(" + value + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * Converts ethereum {@link EthUint} type to ethereum bool type.
     */
    @NotNull
    public static EthBool bool(@NotNull EthUint value) {
        val intValue = value.value().intValue();
        require(intValue == 1 || intValue == 0, "Not a boolean value");
        return new EthBool(intValue == 1);
    }

    /**
     * Converts ethereum {@link EthInt} type to ethereum bool type.
     */
    @NotNull
    public static EthBool bool(@NotNull EthInt value) {
        val intValue = value.value().intValue();
        require(intValue == 1 || intValue == 0, "Not a boolean value");
        return new EthBool(intValue == 1);
    }

}
