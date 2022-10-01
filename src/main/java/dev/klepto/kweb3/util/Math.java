package dev.klepto.kweb3.util;

import dev.klepto.kweb3.type.convert.BigDecimalValue;
import lombok.val;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Utility interface for numeric values to enable simple math functions.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Math<T extends BigDecimalValue> extends Reflection.Creatable<T>, BigDecimalValue {

    default T add(BigDecimalValue value) {
        val newValue = getBigDecimalValue().add(value.getBigDecimalValue());
        return create(newValue);
    }

    default T add(BigInteger value) {
        val newValue = getBigDecimalValue().add(new BigDecimal(value));
        return create(newValue);
    }

    default T add(BigDecimal value) {
        val newValue = getBigDecimalValue().add(value);
        return create(newValue);
    }

    default T add(long value) {
        val newValue = getBigDecimalValue().add(BigDecimal.valueOf(value));
        return create(newValue);
    }

    default T add(double value) {
        val newValue = getBigDecimalValue().add(BigDecimal.valueOf(value));
        return create(newValue);
    }

    default T sub(BigDecimalValue value) {
        val newValue = getBigDecimalValue().subtract(value.getBigDecimalValue());
        return create(newValue);
    }

    default T sub(BigInteger value) {
        val newValue = getBigDecimalValue().subtract(new BigDecimal(value));
        return create(newValue);
    }

    default T sub(BigDecimal value) {
        val newValue = getBigDecimalValue().subtract(value);
        return create(newValue);
    }

    default T sub(long value) {
        val newValue = getBigDecimalValue().subtract(BigDecimal.valueOf(value));
        return create(newValue);
    }

    default T sub(double value) {
        val newValue = getBigDecimalValue().subtract(BigDecimal.valueOf(value));
        return create(newValue);
    }

    default T mul(BigDecimalValue value) {
        val newValue = getBigDecimalValue().multiply(value.getBigDecimalValue());
        return create(newValue);
    }

    default T mul(BigInteger value) {
        val newValue = getBigDecimalValue().multiply(new BigDecimal(value));
        return create(newValue);
    }

    default T mul(BigDecimal value) {
        val newValue = getBigDecimalValue().multiply(value);
        return create(newValue);
    }

    default T mul(long value) {
        val newValue = getBigDecimalValue().multiply(BigDecimal.valueOf(value));
        return create(newValue);
    }

    default T mul(double value) {
        val newValue = getBigDecimalValue().multiply(BigDecimal.valueOf(value));
        return create(newValue);
    }

    default T div(BigDecimalValue value) {
        val newValue = getBigDecimalValue().divide(value.getBigDecimalValue(), RoundingMode.FLOOR);
        return create(newValue);
    }

    default T div(BigInteger value) {
        val newValue = getBigDecimalValue().divide(new BigDecimal(value), RoundingMode.FLOOR);
        return create(newValue);
    }

    default T div(BigDecimal value) {
        val newValue = getBigDecimalValue().divide(value, RoundingMode.FLOOR);
        return create(newValue);
    }

    default T div(long value) {
        val newValue = getBigDecimalValue().divide(BigDecimal.valueOf(value), RoundingMode.FLOOR);
        return create(newValue);
    }

    default T div(double value) {
        val newValue = getBigDecimalValue().divide(BigDecimal.valueOf(value), RoundingMode.FLOOR);
        return create(newValue);
    }

    default T pow(int n) {
        val newValue = getBigDecimalValue().pow(n);
        return create(newValue);
    }

}
