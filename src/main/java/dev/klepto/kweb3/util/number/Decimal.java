package dev.klepto.kweb3.util.number;

import lombok.Getter;
import lombok.val;

import java.math.BigDecimal;

import static java.math.RoundingMode.FLOOR;

/**
 * A decimal numeric that loosely maintains its scale.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public class Decimal implements Numeric<Decimal, BigDecimal> {

    private final BigDecimal value;

    private Decimal(BigDecimal value) {
        this.value = value;
    }

    @Override
    public Decimal mul(Object value) {
        val valueA = this.value;
        val valueB = Numeric.toBigDecimal(value);
        return ensureScale(valueA, valueB, valueA.multiply(valueB));
    }

    @Override
    public Decimal div(Object value) {
        val valueA = this.value;
        val valueB = Numeric.toBigDecimal(value);
        return ensureScale(valueA, valueB, valueA.divide(valueB, FLOOR));
    }

    @Override
    public Decimal pow(Object value) {
        val valueA = this.value;
        val valueB = Numeric.toBigDecimal(value);
        return ensureScale(valueA, valueB, valueA.pow(valueB.intValue()));
    }

    @Override
    public String toString() {
        return getValue().toPlainString();
    }

    public static Decimal ensureScale(BigDecimal valueA, BigDecimal valueB, BigDecimal product) {
        val scale = Math.max(valueA.scale(), valueB.scale());
       return new Decimal(product.setScale(scale, FLOOR));
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