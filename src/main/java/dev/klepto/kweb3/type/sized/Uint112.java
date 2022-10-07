package dev.klepto.kweb3.type.sized;

import dev.klepto.kweb3.type.Uint;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Uint112 extends Uint<Uint112> {

    public Uint112(Object value) {
        super(value);
    }

    public static Uint112 uint112(Object value) {
        return new Uint112(value);
    }

    public static final Uint112 ZERO = new Uint112(0);

}
