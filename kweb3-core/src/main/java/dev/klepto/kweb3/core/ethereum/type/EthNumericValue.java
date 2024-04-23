package dev.klepto.kweb3.core.ethereum.type;

import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Represents an ethereum data type that's backed by {@link BigInteger}.
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
     * Returns true if this numeric value is equal to the specified number.
     *
     * @param number the number to compare
     * @return true if this numeric value is equal to the specified number
     */
    default boolean equals(Number number) {
        return compareTo(number) == 0;
    }

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
        val result = parseBigDecimal(value()).multiply(parseBigDecimal(other));
        return withValue(result.toBigInteger());
    }

    /**
     * Divides this numeric value by the specified number.
     *
     * @param other the number to divide
     * @return a new instance of numeric type with the result of the division
     */
    @NotNull
    default T div(@NotNull Number other) {
        val result = parseBigDecimal(value()).divide(parseBigDecimal(other), RoundingMode.FLOOR);
        return withValue(result.toBigInteger());
    }

    /**
     * Returns the remainder of the division of this numeric value by the specified number.
     *
     * @param other the number to divide by
     * @return a new instance of numeric type with the remainder of the division
     */
    @NotNull
    default T rem(@NotNull Number other) {
        val result = parseBigDecimal(value()).remainder(parseBigDecimal(other));
        return withValue(result.toBigInteger());
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
    default BigDecimal toBigDecimal() {
        return toBigDecimal(0).setScale(18, RoundingMode.FLOOR);
    }

    /**
     * Returns numeric value as {@link BigDecimal} value.
     *
     * @param decimals the amount of decimal places
     * @return the numeric value as big decimal
     */
    @NotNull
    default BigDecimal toBigDecimal(int decimals) {
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
     * Converts numeric value to string.
     *
     * @return a string representation of the numeric value
     */
    default String toValueString() {
        return value().toString();
    }

    /**
     * Parses a number into a {@link BigInteger}.
     *
     * @param number the number to parse
     * @return the parsed big integer
     */
    @NotNull
    static BigInteger parseBigInteger(@NotNull Number number) {
        return parseBigDecimal(number).toBigInteger();
    }

    /**
     * Parses a {@link Number} into a {@link BigDecimal}.
     *
     * @param number the {@link Number} to parse
     * @return the parsed {@link BigDecimal}
     */
    @NotNull
    static BigDecimal parseBigDecimal(@NotNull Number number) {
        var result = new BigDecimal(number.longValue());
        if (number instanceof BigDecimal value) {
            result = value;
        } else if (number instanceof BigInteger value) {
            result = new BigDecimal(value);
        } else if (number instanceof Float value) {
            result = BigDecimal.valueOf(value);
        } else if (number instanceof Double value) {
            result = BigDecimal.valueOf(value);
        } else if (number instanceof EthNumericValue<?> value) {
            result = new BigDecimal(value.value());
        }
        return result.setScale(18, RoundingMode.FLOOR);
    }


    /**
     * Returns the minimum of two numeric values.
     *
     * @param valueA the first numeric value
     * @param valueB the second numeric value
     * @return the minimum of two numeric values
     */
    static <T extends EthNumericValue<T>> T min(T valueA, T valueB) {
        return valueA.compareTo(valueB.value()) < 0 ? valueA : valueB;
    }

    /**
     * Returns the maximum of two numeric values.
     *
     * @param valueA the first numeric value
     * @param valueB the second numeric value
     * @return the maximum of two numeric values
     */
    static <T extends EthNumericValue<T>> T max(T valueA, T valueB) {
        return valueA.compareTo(valueB.value()) > 0 ? valueA : valueB;
    }

}
