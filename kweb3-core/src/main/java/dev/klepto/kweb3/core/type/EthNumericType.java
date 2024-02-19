package dev.klepto.kweb3.core.type;

import dev.klepto.kweb3.core.util.hash.Keccak;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
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
     * Returns numeric value as <code>integer</code> value.
     *
     * @return the numeric value as integer
     */
    default int toInt() {
        return value().intValue();
    }

    /**
     * Returns numeric value as <code>long</code> value.
     *
     * @return the numeric value as long
     */
    default long toLong() {
        return value().longValue();
    }

    /**
     * Returns numeric value as <code>double</code> value with <code>18</code> decimals.
     *
     * @return the numeric value as double with 18 decimal places
     */
    default double toDouble() {
        return toDouble(18);
    }

    /**
     * Returns numeric value as <code>double</code> value.
     *
     * @param decimals the amount of decimal places
     * @return the numeric value as double
     */
    default double toDouble(int decimals) {
        return toDecimal(decimals).doubleValue();
    }

    /**
     * Returns numeric value as {@link BigDecimal}> value with <code>18</code> decimals.
     *
     * @return the numeric value as big decimal with 18 decimal places
     */
    default BigDecimal toDecimal() {
        return toDecimal(18);
    }

    /**
     * Returns numeric value as {@link BigDecimal} value.
     *
     * @param decimals the amount of decimal places
     * @return the numeric value as big decimal
     */
    default BigDecimal toDecimal(int decimals) {
        return new BigDecimal(value(), decimals);
    }

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
