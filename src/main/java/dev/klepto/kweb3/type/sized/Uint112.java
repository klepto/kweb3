package dev.klepto.kweb3.type.sized;

import dev.klepto.kweb3.type.Uint;
import dev.klepto.kweb3.util.Math;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Uint112 extends Uint implements Math<Uint112> {

    public Uint112() {
        this(BigInteger.ZERO);
    }

    public Uint112(BigInteger value) {
        super("uint112", value);
    }

    public Uint112(BigDecimal value) {
        this(value.toBigInteger());
    }

    public static Uint112 uint112(BigInteger value) {
        return new Uint112(value);
    }

    public static Uint112 uint112(long value) {
        return uint112(BigInteger.valueOf(value));
    }

    public static final Uint112 ZERO = uint112(BigInteger.ZERO);

}
