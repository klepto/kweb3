package dev.klepto.kweb3.core.type;

import dev.klepto.kweb3.core.util.Hex;
import dev.klepto.kweb3.core.util.hash.Keccak256;
import lombok.With;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

import static dev.klepto.kweb3.core.util.Conditions.require;
import static dev.klepto.kweb3.core.util.Hex.stripPrefix;
import static dev.klepto.kweb3.core.util.Hex.toBigInteger;

/**
 * Container for <code>ethereum address</code> value.
 *
 * @param value the big integer value
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public record EthAddress(BigInteger value) implements EthType, EthNumericType<EthAddress> {

    /**
     * Zero address constant.
     */
    public static final EthAddress ZERO = address(0);

    public EthAddress {
        require(value.bitLength() <= 160, "Malformed address: {}", Hex.toHex(value));
    }

    /**
     * Returns string representation of this <code>ethereum address</code>.
     *
     * @return the string representation of this <code>ethereum address</code>.
     */
    @Override
    public String toString() {
        return "address(" + toChecksumHex() + ")";
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
     * Compares this <code>ethereum address</code> to the specified object.
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
        if (!(object instanceof EthAddress other)) {
            return false;
        }
        return value.equals(other.value);
    }

    /* Solidity style static initializers */
    @NotNull
    public static EthAddress address(@NotNull Number value) {
        return new EthAddress(EthNumericType.parseBigInteger(value));
    }

    @NotNull
    public static EthAddress address(@NotNull String hex) {
        hex = stripPrefix(Keccak256.keccak256Checksum(hex));
        return address(toBigInteger(hex));
    }

}
