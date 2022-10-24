package dev.klepto.kweb3.util.number;

import lombok.Getter;
import lombok.val;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public class Decimal implements Numeric<Decimal, BigDecimal> {

    private final BigDecimal value;

    private Decimal(BigDecimal value) {
        this.value = value;
    }

    @Override
    public Decimal div(Object value) {
        val newValue = this.value.divide(
                Numeric.toBigDecimal(value),
                this.value.scale(),
                RoundingMode.FLOOR
        );
        return new Decimal(newValue);
    }

    @Override
    public String toString() {
        return getValue().toPlainString();
    }

    public static Decimal create(BigDecimal value, int initialScale, int decimals) {
        val unscaledValue = value.setScale(decimals, RoundingMode.FLOOR);
        val scaledDown = initialScale >= 0;
        val scalePower = BigDecimal.valueOf(10).pow(Math.abs(initialScale));
        val scaledValue = scaledDown
                ? unscaledValue.divide(scalePower, RoundingMode.FLOOR)
                : unscaledValue.multiply(scalePower);
        return new Decimal(scaledValue);
    }

}