package dev.klepto.kweb3.core.type;

import lombok.With;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Objects;

import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * Container for <code>ethereum bool</code> value.
 *
 * @param value true or false <code>boolean</code> value
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public record EthBool(boolean value) implements EthValue {

    /**
     * True bool constant.
     */
    public static final EthBool TRUE = bool(true);

    /**
     * False bool constant.
     */
    public static final EthBool FALSE = bool(false);

    /**
     * Returns string representation of this <code>ethereum bool</code>.
     *
     * @return the string representation of this <code>ethereum bool</code>.
     */
    @Override
    public String toString() {
        return "bool(" + value + ")";
    }

    /**
     * Returns hash code of this <code>ethereum bool</code>.
     *
     * @return hash code of this <code>ethereum bool</code>
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * Compares this <code>ethereum bool</code> to the specified object.
     *
     * @param object the object to compare with
     * @return true if the objects are the same; false otherwise
     */
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!(object instanceof EthBool other)) {
            return false;
        }
        return value == other.value;
    }

    /* Solidity style static initializers */
    @NotNull
    public static EthBool bool(boolean value) {
        return new EthBool(value);
    }

    @NotNull
    public static EthBool bool(@NotNull Number value) {
        val intValue = EthNumericValue.parseBigInteger(value);
        val isTrue = intValue.equals(BigInteger.ONE);
        val isFalse = intValue.equals(BigInteger.ZERO);
        require(isTrue || isFalse, "Number {} is not a boolean value.", value);
        return new EthBool(isTrue);
    }

}
