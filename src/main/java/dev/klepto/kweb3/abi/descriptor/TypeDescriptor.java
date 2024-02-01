package dev.klepto.kweb3.abi.descriptor;

import com.google.common.collect.ImmutableList;
import dev.klepto.kweb3.Web3Error;
import dev.klepto.kweb3.type.EthArray;
import dev.klepto.kweb3.type.EthSizedType;
import dev.klepto.kweb3.type.EthTuple;
import dev.klepto.kweb3.type.EthType;
import dev.klepto.unreflect.UnreflectType;
import lombok.val;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface TypeDescriptor {

    UnreflectType type();

    default EthTupleTypeDescriptor wrap() {
        return new EthTupleTypeDescriptor(ImmutableList.of(this));
    }

    default String toAbiDescriptor() {
        return EthType.getSolidityName(type().toClass());
    }

    /**
     * Generates ABI type descriptor for given {@link EthType} value.
     *
     * @param value the ethereum data value
     * @return the ABI type descriptor
     */
    static TypeDescriptor parse(EthType value) {
        if (value instanceof EthArray<?> array) {
            return parse(array);
        } else if (value instanceof EthSizedType sized) {
            return parse(sized);
        } else if (value instanceof EthTuple tuple) {
            return parse(tuple);
        }

        return new EthTypeDescriptor(UnreflectType.of(value));
    }

    static TypeDescriptor parse(EthArray<?> value) {
        if (value.isEmpty()) {
            throw new Web3Error("Cannot infer component type for empty arrays.");
        }

        val componentType = parse(value.get(0));
        return new EthArrayTypeDescriptor(componentType, value.capacity());
    }

    static TypeDescriptor parse(EthSizedType value) {
        return new EthSizedTypeDescriptor(UnreflectType.of(value), value.size());
    }

    static TypeDescriptor parse(EthTuple value) {
        if (value.isEmpty()) {
            throw new Web3Error("Cannot infer children types for empty tuples.");
        }

        val children = value.stream()
                .map(TypeDescriptor::parse)
                .collect(toImmutableList());

        return new EthTupleTypeDescriptor(children);
    }


}
