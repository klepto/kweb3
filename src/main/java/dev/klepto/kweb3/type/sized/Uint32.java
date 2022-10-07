package dev.klepto.kweb3.type.sized;

import dev.klepto.kweb3.type.Uint;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Uint32 extends Uint<Uint32> {

    public Uint32(Object value) {
        super(value);
    }

    public static Uint32 uint32(Object value) {
        return new Uint32(value);
    }

    public static final Uint32 ZERO = new Uint32(0);

}
