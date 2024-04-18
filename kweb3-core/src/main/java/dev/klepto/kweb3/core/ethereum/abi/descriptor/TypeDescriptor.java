package dev.klepto.kweb3.core.ethereum.abi.descriptor;

import com.google.common.collect.ImmutableList;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthArray;
import dev.klepto.kweb3.core.ethereum.type.EthSizedValue;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthTuple;
import dev.klepto.kweb3.core.ethereum.type.EthValue;
import dev.klepto.unreflect.UnreflectType;
import lombok.val;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * A generic ABI type descriptor not tied to any {@link EthValue}. Contains factory methods for constructing and/or
 * parsing available type descriptors.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface TypeDescriptor {

    /**
     * Returns {@link EthValue} that this type descriptor represents.
     *
     * @return the represented ethereum data type
     */
    UnreflectType type();

    /**
     * Returns this type descriptor wrapped in a {@link EthTuple}, useful for decoding function return values, etc.
     *
     * @return this type descriptor that's wrapped in a tuple
     */
    default EthTupleTypeDescriptor wrap() {
        return new EthTupleTypeDescriptor(ImmutableList.of(this));
    }

    /**
     * Returns ABI-compatible descriptor string.
     *
     * @return the ABI compatible string that describes this type
     */
    default String toAbiDescriptor() {
        return EthValue.getSolidityName(type().toClass());
    }

    /**
     * Generates ABI type descriptor by inferring types in a given {@link EthValue} value.
     *
     * @param value the ethereum data value
     * @return the ABI type descriptor
     */
    static TypeDescriptor parse(EthValue value) {
        if (value instanceof EthArray<?> array) {
            return parseArray(array);
        } else if (value instanceof EthSizedValue sized) {
            return parseSized(sized);
        } else if (value instanceof EthTuple tuple) {
            return parseTuple(tuple);
        }

        return new EthTypeDescriptor(UnreflectType.of(value));
    }

    /**
     * Generates ABI type descriptor by parsing information in a given {@link EthSizedValue} value.
     *
     * @param value the ethereum sized value
     * @return the ABI type descriptor
     */
    static TypeDescriptor parseSized(EthSizedValue value) {
        return new EthSizedTypeDescriptor(UnreflectType.of(value), value.size());
    }

    /**
     * Generates ABI type descriptor by inferring types in a given {@link EthArray} value.
     *
     * @param value the ethereum array value
     * @return the ABI type descriptor
     */
    static TypeDescriptor parseArray(EthArray<?> value) {
        require(!value.isEmpty(), "Cannot infer component type for empty arrays.");

        val componentType = parse(value.get(0));
        return new EthArrayTypeDescriptor(componentType, value.capacity());
    }

    /**
     * Generates ABI type descriptor by inferring types in a given {@link EthTuple} value.
     *
     * @param value the ethereum tuple value
     * @return the ABI type descriptor
     */
    static TypeDescriptor parseTuple(EthTuple value) {
        require(!value.isEmpty(), "Cannot infer children types for empty tuples.");

        val children = value.stream()
                .map(TypeDescriptor::parse)
                .collect(toImmutableList());

        return new EthTupleTypeDescriptor(children);
    }

}
