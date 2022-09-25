package dev.klepto.kweb3.type;

import dev.klepto.kweb3.util.Math;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Represents Uint8 solidity type.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Uint8 extends Uint implements Math<Uint8> {

    public Uint8() {
        this(BigInteger.ZERO);
    }

    public Uint8(BigInteger value) {
        super("uint8", value);
    }

    public Uint8(BigDecimal value) {
        this(value.toBigInteger());
    }

    public static Uint8 uint8(BigInteger value) {
        return new Uint8(value);
    }

    public static Uint8 uint8(long value) {
        return uint8(BigInteger.valueOf(value));
    }

    public static final Uint8 ZERO = uint8(BigInteger.ZERO);

}
