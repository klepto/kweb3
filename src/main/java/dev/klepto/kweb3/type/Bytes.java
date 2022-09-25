package dev.klepto.kweb3.type;

import dev.klepto.kweb3.type.convert.ByteArrayValue;
import lombok.Getter;

/**
 * Represents Bytes solidity type.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public class Bytes extends SolidityType implements ByteArrayValue {

    private final byte[] value;

    public Bytes() {
        this(new byte[0]);
    }

    public Bytes(byte[] value) {
        super("bytes");
        this.value = value;
    }

    @Override
    public byte[] getByteArrayValue() {
        return getValue();
    }

}
