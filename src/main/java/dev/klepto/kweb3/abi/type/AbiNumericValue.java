package dev.klepto.kweb3.abi.type;

import lombok.val;

import static dev.klepto.kweb3.Web3Error.require;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public abstract class AbiNumericValue<T extends AbiNumericValue<?, V>, V extends Comparable<V>> extends AbiSizedValue<T, V> {

    public static final int MIN_SIZE = 8;
    public static final int MAX_SIZE = 256;

    public AbiNumericValue(V value) {
        this(value, MAX_SIZE);
    }

    public AbiNumericValue(V value, int size) {
        super(value, size);
    }

    public abstract V getMinValue();

    public abstract V getMaxValue();

    public void checkCapacity() {
        val value = getValue();
        val size = getSize();

        require(size >= MIN_SIZE && size <= MAX_SIZE, "Unsupported size: {}.", size);
        require(
                value.compareTo(getMinValue()) >= 0 && value.compareTo(getMaxValue()) <= 0,
                "Unsupported value: {} for size: {}.", value, size
        );
    }

}
