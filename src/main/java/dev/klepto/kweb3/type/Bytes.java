package dev.klepto.kweb3.type;

import dev.klepto.kweb3.util.number.Numeric;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents Bytes solidity type.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
@RequiredArgsConstructor
public class Bytes extends NumericType<Bytes, byte[]> {

    private final byte[] value;

    public static Bytes bytes(Object value) {
        return new Bytes(Numeric.toByteArray(value));
    }

}
