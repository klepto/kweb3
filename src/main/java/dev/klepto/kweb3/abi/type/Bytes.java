package dev.klepto.kweb3.abi.type;

import dev.klepto.kweb3.abi.type.util.Convertibles;
import lombok.Getter;
import lombok.val;

import java.util.Arrays;

import static dev.klepto.kweb3.Web3Error.require;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public class Bytes extends AbiSizedValue<Bytes, byte[]> implements AbiValue {

    public static final int MIN_SIZE = 0;
    public static final int MAX_SIZE = 32;

    public Bytes(Object value) {
        this(value, 0);
    }

    public Bytes(Object value, int size) {
        super(Convertibles.toByteArray(value), size);
    }

    @Override
    public Bytes withValue(Object value) {
        return new Bytes(value, getSize());
    }

    @Override
    public Bytes withSize(int size) {
        return new Bytes(getValue(), size);
    }

    @Override
    public void checkCapacity() {
        val value = getValue();
        val size = getSize();
        val length = value.length;

        require(size >= MIN_SIZE && size <= MAX_SIZE, "Unsupported size: {}.", size);
        require(length == size || size == 0, "Unsupported byte array length: {} for size: {}.", length, size);
    }

    @Override
    public String toString() {
        val values = Arrays.toString(getValue());
        return "bytes" + (getSize() > 0 ? getSize() : "") + "(" + values + ")";
    }

}