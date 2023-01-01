package dev.klepto.kweb3.abi.type.util;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */

import lombok.Getter;
import lombok.val;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static java.math.RoundingMode.FLOOR;

/**
 * A decimal numeric that loosely maintains its scale.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public class Decimal implements Numeric<Decimal> {

    public static final int MINIMUM_DECIMALS = 18;

    private final BigDecimal value;

    private Decimal(BigDecimal value) {
        this.value = value;
    }

    @Override
    public Decimal withValue(Object newValue) {
        return new Decimal(Convertibles.toBigDecimal(newValue));
    }

    public Decimal mul(Object value) {
        val valueA = this.value;
        val valueB = Convertibles.toBigDecimal(value);
        return ensureScale(valueA, valueB, valueA.multiply(valueB));
    }

    public Decimal div(Object value) {
        val valueA = this.value;
        val valueB = Convertibles.toBigDecimal(value);
        return ensureScale(valueA, valueB, valueA.divide(valueB, FLOOR));
    }

    public Decimal pow(Object value) {
        val valueA = this.value;
        val valueB = Convertibles.toBigDecimal(value);
        return ensureScale(valueA, valueB, valueA.pow(valueB.intValue()));
    }

    public Decimal sqrt() {
        return new Decimal(getValue().sqrt(new MathContext(getValue().scale(), RoundingMode.FLOOR)));
    }

    @Override
    public String toString() {
        return getValue().toPlainString();
    }

    public static Decimal ensureScale(BigDecimal valueA, BigDecimal valueB, BigDecimal product) {
        val scale = Math.max(valueA.scale(), valueB.scale());
        return new Decimal(product.setScale(scale, FLOOR));
    }

    public static Decimal create(BigDecimal value) {
        return create(value, 0);
    }

    public static Decimal create(BigDecimal value, int initialScale) {
        return create(value, initialScale, MINIMUM_DECIMALS);
    }

    public static Decimal create(BigDecimal value, int initialScale, int decimals) {
        val unscaledValue = value.scale() < decimals ? value.setScale(decimals, FLOOR) : value;
        val scaledDown = initialScale >= 0;
        val scalePower = BigDecimal.valueOf(10).pow(Math.abs(initialScale));
        val scaledValue = scaledDown
                ? unscaledValue.divide(scalePower, FLOOR)
                : unscaledValue.multiply(scalePower);
        return new Decimal(scaledValue);
    }

}