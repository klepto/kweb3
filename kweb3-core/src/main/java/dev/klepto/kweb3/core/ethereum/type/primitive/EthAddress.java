package dev.klepto.kweb3.core.ethereum.type.primitive;

import com.google.common.base.Strings;
import dev.klepto.kweb3.core.ethereum.type.EthNumericValue;
import dev.klepto.kweb3.core.ethereum.type.EthValue;
import dev.klepto.kweb3.core.util.Hex;
import dev.klepto.kweb3.core.util.hash.Keccak256;
import lombok.With;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

import static dev.klepto.kweb3.core.util.Conditions.require;
import static dev.klepto.kweb3.core.util.Hex.stripPrefix;
import static dev.klepto.kweb3.core.util.Hex.toBigInteger;
import static dev.klepto.kweb3.core.util.hash.Keccak256.keccak256Checksum;

/**
 * Container for <code>ethereum address</code> value.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public class EthAddress implements EthValue, EthNumericValue<EthAddress> {

    /**
     * Zero address constant.
     */
    public static final EthAddress ZERO = address(0);

    /**
     * Address value.
     */
    @NotNull
    private final BigInteger value;

    /**
     * Constructs new <code>ethereum address</code> from the specified big integer value.
     *
     * @param value the big integer value
     */
    public EthAddress(@NotNull BigInteger value) {
        require(value.bitLength() <= 160, "Malformed address: {}", Hex.toHex(value));
        this.value = value;
    }

    /**
     * Returns {@link BigInteger} value that represents this <code>ethereum address</code>.
     *
     * @return the big integer value of this <code>ethereum address</code>
     */
    @Override
    public @NotNull BigInteger value() {
        return value;
    }

    /**
     * Generates a hex of this <code>ethereum address</code> with checksum.
     *
     * @return a check-summed hex of this <code>ethereum address</code>
     */
    @Override
    public @NotNull String toHex() {
        val hex = value().toString(16);
        val paddedHex = Strings.padStart(hex, 40, '0');
        return keccak256Checksum(paddedHex);
    }

    /**
     * Returns string representation of this <code>ethereum address</code>.
     *
     * @return the string representation of this <code>ethereum address</code>.
     */
    @Override
    @NotNull
    public String toString() {
        return "address(" + toHex() + ")";
    }

    /**
     * Returns hash code of this <code>ethereum address</code>.
     *
     * @return hash code of this <code>ethereum address</code>
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Arithmetic equals method for <code>ethereum address</code> values.
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
     * Compares this <code>ethereum address</code> to the specified object.
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
        if (!(object instanceof EthAddress other)) {
            return false;
        }
        return value.equals(other.value);
    }

    /* Solidity style static initializers */
    @NotNull
    public static EthAddress address(@NotNull Number value) {
        return new EthAddress(EthNumericValue.parseBigInteger(value));
    }

    @NotNull
    public static EthAddress address(@NotNull String hex) {
        hex = stripPrefix(Keccak256.keccak256Checksum(hex));
        return address(toBigInteger(hex));
    }

}
