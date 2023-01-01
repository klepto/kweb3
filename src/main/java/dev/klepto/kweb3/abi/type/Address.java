package dev.klepto.kweb3.abi.type;

import dev.klepto.kweb3.abi.type.util.Hex;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Address extends Uint implements AbiValue {

    public Address(Object value) {
        super(value, 160);
    }

    @Override
    public AbiType getType() {
        return new AbiType(getClass());
    }

    @Override
    public String toHex() {
        return Hex.toChecksumHex(super.toHex());
    }

    @Override
    public String toString() {
        return "address(" + toHex() + ")";
    }
    
}
