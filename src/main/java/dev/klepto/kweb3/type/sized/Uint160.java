package dev.klepto.kweb3.type.sized;

import dev.klepto.kweb3.type.Uint;

/**
 * Represents Uint160 solidity type.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Uint160 extends Uint<Uint160> {

    public Uint160(Object value) {
        super(value);
    }

    public static Uint160 uint160(Object value) {
        return new Uint160(value);
    }

    public static final Uint160 ZERO = new Uint160(0);

}
