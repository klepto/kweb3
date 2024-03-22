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
public interface EthNumericValue<T extends EthValue> extends EthValue, Comparable<Number> {

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
    @NotNull
    T withValue(@NotNull BigInteger value);

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
    @NotNull
    default T plus(@NotNull Number other) {
        val result = value().add(parseBigInteger(other));
        return withValue(result);
    }

    /**
     * Subtracts the specified number from this numeric value.
     *
     * @param other the number to subtract
     * @return a new instance of numeric type with the result of the subtraction
     */
    @NotNull
    default T minus(@NotNull Number other) {
        val result = value().subtract(parseBigInteger(other));
        return withValue(result);
    }

    /**
     * Multiplies this numeric value by the specified number.
     *
     * @param other the number to multiply
     * @return a new instance of numeric type with the result of the multiplication
     */
    @NotNull
    default T times(@NotNull Number other) {
        val result = value().multiply(parseBigInteger(other));
        return withValue(result);
    }

    /**
     * Divides this numeric value by the specified number.
     *
     * @param other the number to divide
     * @return a new instance of numeric type with the result of the division
     */
    @NotNull
    default T div(@NotNull Number other) {
        val result = value().divide(parseBigInteger(other));
        return withValue(result);
    }

    /**
     * Returns the remainder of the division of this numeric value by the specified number.
     *
     * @param other the number to divide by
     * @return a new instance of numeric type with the remainder of the division
     */
    @NotNull
    default T rem(@NotNull Number other) {
        val result = value().remainder(parseBigInteger(other));
        return withValue(result);
    }

    /**
     * Returns this numeric value.
     *
     * @return a new instance of this numeric value
     */
    @NotNull
    default T unaryPlus() {
        return withValue(value());
    }

    /**
     * Returns the negation of this numeric value.
     *
     * @return a new instance of numeric type with the negation of this value
     */
    @NotNull
    default T unaryMinus() {
        val result = value().negate();
        return withValue(result);
    }

    /**
     * Returns numeric value as {@link BigDecimal}> value with <code>18</code> decimals.
     *
     * @return the numeric value as big decimal with 18 decimal places
     */
    @NotNull
    default BigDecimal decimalValue() {
        return decimalValue(18);
    }

    /**
     * Returns numeric value as {@link BigDecimal} value.
     *
     * @param decimals the amount of decimal places
     * @return the numeric value as big decimal
     */
    @NotNull
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
        return "0x" + hex;
    }

    /**
     * Parses a number into a {@link BigInteger}.
     *
     * @param number the number to parse
     * @return the parsed big integer
     */
    @NotNull
    static BigInteger parseBigInteger(@NotNull Number number) {
        if (number instanceof BigInteger) {
            return (BigInteger) number;
        } else if (number instanceof Float) {
            return BigDecimal.valueOf(number.floatValue()).toBigInteger();
        } else if (number instanceof Double) {
            return BigDecimal.valueOf(number.doubleValue()).toBigInteger();
        } else if (number instanceof BigDecimal) {
            return ((BigDecimal) number).toBigInteger();
        } else if (number instanceof EthNumericValue) {
            return ((EthNumericValue<?>) number).value();
        }
        return BigInteger.valueOf(number.longValue());
    }

}
