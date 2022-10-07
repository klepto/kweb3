package dev.klepto.kweb3.type.sized;

import dev.klepto.kweb3.type.Uint;

/**
 * Represents Uint8 solidity type.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Uint8 extends Uint<Uint8> {

    public Uint8(Object value) {
        super(value);
    }

    public static Uint8 uint8(Object value) {
        return new Uint8(value);
    }

    public static final Uint8 ZERO = new Uint8(0);

}
