package dev.klepto.kweb3.type.sized;

import dev.klepto.kweb3.type.Uint;
import dev.klepto.kweb3.util.Math;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Uint32 extends Uint implements Math<Uint112> {

    public Uint32() {
        this(BigInteger.ZERO);
    }

    public Uint32(BigInteger value) {
        super("uint32", value);
    }

    public Uint32(BigDecimal value) {
        this(value.toBigInteger());
    }

    public static Uint32 uint32(BigInteger value) {
        return new Uint32(value);
    }

    public static Uint32 uint32(long value) {
        return uint32(BigInteger.valueOf(value));
    }

    public static final Uint32 ZERO = uint32(BigInteger.ZERO);

}
