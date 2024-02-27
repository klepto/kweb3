package dev.klepto.kweb3.core.type;

import org.jetbrains.annotations.NotNull;

/**
 * Represents ethereum <code>string</code> data type.
 *
 * @param value the string value
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public record EthString(String value) implements EthType {

    /**
     * Empty string constant.
     */
    public static final EthString EMPTY = string("");

    @Override
    public String toString() {
        return "string(" + value + ")";
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /* Solidity style string initializers */
    @NotNull
    public static EthString string(@NotNull String value) {
        return new EthString(value);
    }

}
