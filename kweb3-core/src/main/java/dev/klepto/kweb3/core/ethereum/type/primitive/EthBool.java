package dev.klepto.kweb3.core.ethereum.type.primitive;

import dev.klepto.kweb3.core.ethereum.type.EthNumericValue;
import dev.klepto.kweb3.core.ethereum.type.EthValue;
import lombok.With;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.Objects;

import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * Container for <code>ethereum bool</code> value.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public class EthBool implements EthValue, EthNumericValue<EthBool> {

    /**
     * True bool constant.
     */
    public static final EthBool TRUE = bool(true);

    /**
     * False bool constant.
     */
    public static final EthBool FALSE = bool(false);

    /**
     * Boolean value.
     */
    @NotNull
    private final BigInteger value;

    /**
     * Constructs new <code>ethereum bool</code> with the specified value.
     *
     * @param value the boolean value
     */
    public EthBool(BigInteger value) {
        val isTrue = value.equals(BigInteger.ONE);
        val isFalse = value.equals(BigInteger.ZERO);
        require(isTrue || isFalse, "BigInteger {} is not a boolean value.", value);
        this.value = value;
    }

    /**
     * Returns <code>true</code> if this <code>ethereum bool</code> is <code>true</code>, <code>false</code> otherwise.
     *
     * @return <code>true</code> if this <code>ethereum bool</code> is <code>true</code>, <code>false</code> otherwise
     */
    public boolean check() {
        return value.equals(BigInteger.ONE);
    }

    /**
     * Returns the value of this <code>ethereum bool</code>.
     *
     * @return the value of this <code>ethereum bool</code>
     */
    @NotNull
    public BigInteger value() {
        return value;
    }

    /**
     * Returns string representation of this <code>ethereum bool</code>.
     *
     * @return the string representation of this <code>ethereum bool</code>.
     */
    @Override
    @NotNull
    public String toString() {
        return "bool(" + check() + ")";
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
     * Arithmetic equals method for <code>ethereum boolean</code> values.
     *
     * @param object the object to compare with
     * @return true if the objects have the same value; false otherwise
     */
    public boolean equals(@Nullable Object object) {
        if (object instanceof Number number) {
            return equals(number);
        }
        if (object instanceof EthNumericValue<?> numeric) {
            return equals(numeric.value());
        }
        return false;
    }

    /**
     * Compares this <code>ethereum bool</code> to the specified object.
     *
     * @param object the object to compare with
     * @return true if the objects are the same; false otherwise
     */
    public boolean matches(@Nullable Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!(object instanceof EthBool other)) {
            return false;
        }
        return check() == other.check();
    }

    /* Solidity style static initializers */
    @NotNull
    public static EthBool bool(@NotNull Number value) {
        val intValue = EthNumericValue.parseBigInteger(value);
        return new EthBool(intValue);
    }

    @NotNull
    public static EthBool bool(boolean value) {
        return bool(value ? BigInteger.ONE : BigInteger.ZERO);
    }

}
