package dev.klepto.kweb3.core.type;

import dev.klepto.kweb3.core.util.Hex;
import dev.klepto.kweb3.core.util.hash.Keccak256;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

import static com.google.common.base.Preconditions.checkArgument;
import static dev.klepto.kweb3.core.util.Conditions.require;
import static dev.klepto.kweb3.core.util.Hex.stripPrefix;
import static dev.klepto.kweb3.core.util.Hex.toBigInteger;

/**
 * Represents ethereum <code>address</code> data type.
 *
 * @param value the integer value
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public record EthAddress(BigInteger value) implements EthNumericType {

    /**
     * Zero address constant.
     */
    public static final EthAddress ZERO = address("0x0");

    public EthAddress {
        require(value.bitLength() <= 160, "Malformed address: {}", Hex.toHex(value));
    }

    @Override
    public String toString() {
        return "address(" + toChecksumHex() + ")";
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /* Solidity style address initializers */

    /**
     * Converts {@link BigInteger} to ethereum address type.
     */
    @NotNull
    public static EthAddress address(@NotNull BigInteger value) {
        return new EthAddress(value);
    }

    /**
     * Converts hexadecimal string to ethereum address type.
     */
    @NotNull
    public static EthAddress address(@NotNull String hex) {
        hex = stripPrefix(Keccak256.keccak256Checksum(hex));
        return address(toBigInteger(hex));
    }

    /**
     * Converts ethereum {@link EthUint} to ethereum address type.
     */
    @NotNull
    public static EthAddress address(@NotNull EthUint value) {
        checkArgument(value.size() == 160, "Only uint160 can be converted to address");
        return address(value.value());
    }

}
