package dev.klepto.kweb3.core.type;

import dev.klepto.kweb3.core.util.hash.Keccak;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

import static dev.klepto.kweb3.core.util.hash.Keccak256.keccak256Checksum;

/**
 * Represents an ethereum numeric data type that's backed by {@link BigInteger}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthNumericType extends EthType {

    /**
     * Returns numeric value as {@link BigInteger}.
     *
     * @return the numeric value as big integer
     */
    @NotNull
    BigInteger value();

    /**
     * Converts numeric value to hexadecimal string.
     *
     * @return a hexadecimal string value
     */
    @NotNull
    default String toHex() {
        var hex = value().toString(16);
        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }
        return "0x" + hex;
    }

    /**
     * Converts numeric value to hexadecimal string with {@link Keccak}-256 checksum.
     *
     * @return a checksum hexadecimal string value
     */
    @NotNull
    default String toChecksumHex() {
        return keccak256Checksum(toHex());
    }

}
