package dev.klepto.kweb3.core.ethereum.type.primitive;

import dev.klepto.kweb3.core.ethereum.type.EthNumericValue;
import dev.klepto.kweb3.core.ethereum.type.EthValue;
import lombok.With;
import lombok.experimental.Delegate;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

/**
 * Container for <code>ethereum string</code> value.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public class EthString implements EthValue, CharSequence {

    /**
     * Empty <code>ethereum string</code> constant.
     */
    public static final EthString EMPTY = string("");

    @NotNull
    private final @Delegate String value;

    /**
     * Constructs new <code>ethereum string</code> with the specified value.
     *
     * @param value the string value
     */
    public EthString(@NotNull String value) {
        this.value = value;
    }

    /**
     * Returns the value of this <code>ethereum string</code>.
     *
     * @return the value of this <code>ethereum string</code>
     */
    @NotNull
    public String value() {
        return value;
    }

    /**
     * Returns string representation of this <code>ethereum string</code>.
     *
     * @return the string representation of this <code>ethereum string</code>.
     */
    @Override
    @NotNull
    public String toString() {
        return "string(" + value + ")";
    }

    /**
     * Returns hash code of this <code>ethereum string</code>.
     *
     * @return hash code of this <code>ethereum string</code>
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Compares this <code>ethereum string</code> to the specified object.
     *
     * @param object the object to compare with
     * @return true if the objects are the same; false otherwise
     */
    public boolean equals(@Nullable Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (object instanceof EthString other) {
            return value.equals(other.value);
        }
        return false;
    }

    /* Solidity style string initializers */
    @NotNull
    public static EthString string(@NotNull String value) {
        return new EthString(value);
    }

    @NotNull
    public static EthString string(@NotNull Number value) {
        if (value instanceof Float || value instanceof Double || value instanceof BigDecimal) {
            return new EthString(value.toString());
        }

        val intValue = EthNumericValue.parseBigInteger(value);
        return new EthString(intValue.toString());
    }

}
