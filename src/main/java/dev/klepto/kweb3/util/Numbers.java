package dev.klepto.kweb3.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Numberic utility methods.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Numbers {

    public static final int WEI_TO_ETHER_SCALE = 18;
    public static final BigDecimal WEI_IN_ETHER = BigDecimal.TEN.pow(WEI_TO_ETHER_SCALE);

    public static BigInteger toWei(double value) {
        return toWei(BigDecimal.valueOf(value));
    }

    public static BigInteger toWei(BigInteger value) {
        return toWei(new BigDecimal(value));
    }

    public static BigInteger toWei(BigDecimal value) {
        return value.multiply(WEI_IN_ETHER).toBigInteger();
    }

    public static BigDecimal toEther(BigInteger value) {
        return toEther(new BigDecimal(value));
    }

    public static BigDecimal toEther(BigDecimal value) {
        return toEther(value, WEI_TO_ETHER_SCALE);
    }

    public static BigDecimal toEther(BigDecimal value, int scale) {
        return value.divide(WEI_IN_ETHER, scale, RoundingMode.FLOOR);
    }

}
