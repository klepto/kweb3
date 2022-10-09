package dev.klepto.kweb3.util.number;

import dev.klepto.kweb3.type.sized.Uint256;
import lombok.val;

/**
 * Numeric utility methods.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Tokens {

    public static final int DEFAULT_DECIMALS = 18;

    public static Uint256 tokens(Object value) {
        return tokens(value, DEFAULT_DECIMALS);
    }

    public static Uint256 tokens(Object value, Object decimals) {
        val numericValue = Numeric.toDecimal(value);
        val multiplier = Numeric.toDecimal(10).pow(decimals);
        return numericValue.mul(multiplier).toUint256();
    }

}
