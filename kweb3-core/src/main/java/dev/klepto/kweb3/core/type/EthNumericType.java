package dev.klepto.kweb3.core.type;

import dev.klepto.kweb3.core.util.hash.Keccak;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import static dev.klepto.kweb3.core.util.hash.Keccak256.keccak256Checksum;

/**
 * Represents an ethereum numeric data type that's backed by {@link BigInteger}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthNumericType<T extends EthType> extends EthType, Comparable<Number> {

    /**
     * Returns numeric value as {@link BigInteger}.
     *
     * @return the numeric value as big integer
     */
    @NotNull
    BigInteger value();

    /**
     * Creates a new instance of numeric type with given value.
     *
     * @param value the value to set
     * @return a new instance of numeric type with given value
     */
    T withValue(BigInteger value);

    /**
     * Compares this numeric value with another number value.
     *
     * @param other the other number value
     * @return -1, 0 or 1 as this numeric value is numerically less than, equal to, or greater than <code>other</code>.
     */
    @Override
    default int compareTo(@NotNull Number other) {
        return value().compareTo(parseBigInteger(other));
    }

    /**
     * Adds the specified number to this numeric value.
     *
     * @param other the number to add
     * @return a new instance of numeric type with the result of the addition
     */
    default T plus(Number other) {
        val result = value().add(parseBigInteger(other));
        return withValue(result);
    }

    /**
     * Subtracts the specified number from this numeric value.
     *
     * @param other the number to subtract
     * @return a new instance of numeric type with the result of the subtraction
     */
    default T minus(Number other) {
        val result = value().subtract(parseBigInteger(other));
        return withValue(result);
    }

    /**
     * Multiplies this numeric value by the specified number.
     *
     * @param other the number to multiply
     * @return a new instance of numeric type with the result of the multiplication
     */
    default T times(Number other) {
        val result = value().multiply(parseBigInteger(other));
        return withValue(result);
    }

    /**
     * Divides this numeric value by the specified number.
     *
     * @param other the number to divide
     * @return a new instance of numeric type with the result of the division
     */
    default T div(Number other) {
        val result = value().divide(parseBigInteger(other));
        return withValue(result);
    }

    /**
     * Returns the remainder of the division of this numeric value by the specified number.
     *
     * @param other the number to divide by
     * @return a new instance of numeric type with the remainder of the division
     */
    default T rem(Number other) {
        val result = value().remainder(parseBigInteger(other));
        return withValue(result);
    }

    /**
     * Returns this numeric value.
     *
     * @return a new instance of this numeric value
     */
    default T unaryPlus() {
        return withValue(value());
    }

    /**
     * Returns the negation of this numeric value.
     *
     * @return a new instance of numeric type with the negation of this value
     */
    default T unaryMinus() {
        val result = value().negate();
        return withValue(result);
    }

    /**
     * Returns the value of the specified number as a {@code byte}.
     *
     * @return the numeric value represented by this object after conversion to type {@code byte}.
     */
    default byte byteValue() {
        return value().byteValue();
    }

    /**
     * Returns the value of the specified number as a {@code short}.
     *
     * @return the numeric value represented by this object after conversion to type {@code short}.
     */
    default short shortValue() {
        return value().shortValue();
    }

    /**
     * Returns the value of the specified number as an {@code int}.
     *
     * @return the numeric value represented by this object after conversion to type {@code int}.
     */
    default int intValue() {
        return value().intValue();
    }

    /**
     * Returns the value of the specified number as a {@code long}.
     *
     * @return the numeric value represented by this object after conversion to type {@code long}.
     */
    default long longValue() {
        return value().longValue();
    }

    /**
     * Returns the value of the specified number as a {@code float}.
     *
     * @return the numeric value represented by this object after conversion to type {@code float}.
     */
    default float floatValue() {
        return value().floatValue();
    }

    /**
     * Returns numeric value as <code>double</code> value with <code>18</code> decimals.
     *
     * @return the numeric value as double with 18 decimal places
     */
    default double doubleValue() {
        return doubleValue(18);
    }

    /**
     * Returns numeric value as <code>double</code> value.
     *
     * @param decimals the amount of decimal places
     * @return the numeric value as double
     */
    default double doubleValue(int decimals) {
        return decimalValue(decimals).doubleValue();
    }

    /**
     * Returns numeric value as {@link BigDecimal}> value with <code>18</code> decimals.
     *
     * @return the numeric value as big decimal with 18 decimal places
     */
    default BigDecimal decimalValue() {
        return decimalValue(18);
    }

    /**
     * Returns numeric value as {@link BigDecimal} value.
     *
     * @param decimals the amount of decimal places
     * @return the numeric value as big decimal
     */
    default BigDecimal decimalValue(int decimals) {
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

    /**
     * Parses a number into a {@link BigInteger}.
     *
     * @param number the number to parse
     * @return the parsed big integer
     */
    static BigInteger parseBigInteger(Number number) {
        if (number instanceof BigInteger) {
            return (BigInteger) number;
        } else if (number instanceof Float) {
            return BigDecimal.valueOf(number.floatValue()).toBigInteger();
        } else if (number instanceof Double) {
            return BigDecimal.valueOf(number.doubleValue()).toBigInteger();
        } else if (number instanceof BigDecimal) {
            return ((BigDecimal) number).toBigInteger();
        } else if (number instanceof EthNumericType) {
            return ((EthNumericType<?>) number).value();
        }
        return BigInteger.valueOf(number.longValue());
    }

}
