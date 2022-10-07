package dev.klepto.kweb3.util;

import dev.klepto.kweb3.type.sized.Uint256;
import dev.klepto.kweb3.util.function.Numeric;
import lombok.val;

/**
 * Numeric utility methods.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Tokens {

    public static final int DEFAULT_DECIMALS = 18;

    public static Uint256 ether(Object value) {
        return ether(value, DEFAULT_DECIMALS);
    }

    public static Uint256 ether(Object value, Object decimals) {
        val numericValue = Numeric.of(value);
        val multiplier = Numeric.of(10).pow(decimals);
        return numericValue.mul(multiplier).toUint256();
    }

}
