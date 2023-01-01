package dev.klepto.kweb3.abi.type;

import lombok.Getter;

import java.util.Objects;

import static dev.klepto.kweb3.Web3Error.require;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public abstract class AbiSizedValue<T extends AbiSizedValue<?, V>, V> implements AbiValue {

    private final Object value;
    private final int size;

    public AbiSizedValue(Object value, int size) {
        this.value = value;
        this.size = size;
        checkCapacity();
    }

    public V getValue() {
        return (V) value;
    }

    public abstract T withValue(Object value);

    public abstract T withSize(int size);

    public abstract void checkCapacity();

    @Override
    public AbiType getType() {
        return new AbiType(getClass()).withValueSize(getSize());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, size);
    }

}
