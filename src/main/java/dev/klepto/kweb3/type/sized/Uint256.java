package dev.klepto.kweb3.type.sized;

import dev.klepto.kweb3.type.Uint;

/**
 * Represents Uint256 solidity type.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Uint256 extends Uint<Uint256> {

    public Uint256(Object value) {
        super(value);
    }

    public static Uint256 uint256(Object value) {
        return new Uint256(value);
    }

    public static final Uint256 ZERO = new Uint256(0);

}