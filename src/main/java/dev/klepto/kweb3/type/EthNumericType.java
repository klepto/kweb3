package dev.klepto.kweb3.type;

import java.math.BigInteger;

import static dev.klepto.kweb3.util.hash.Keccak256.keccak256Checksum;

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
    BigInteger value();

    /**
     * Converts numeric value to hexadecimal string.
     *
     * @return a hexadecimal string value
     */
    default String toHex() {
        var hex = value().toString(16);
        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }
        return "0x" + hex;
    }

    /**
     * Converts numeric value to hexadecimal string with {@link dev.klepto.kweb3.util.hash.Keccak}-256 checksum.
     *
     * @return a checksum hexadecimal string value
     */
    default String toChecksumHex() {
        return keccak256Checksum(toHex());
    }

}
