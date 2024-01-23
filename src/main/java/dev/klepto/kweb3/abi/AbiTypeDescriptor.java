package dev.klepto.kweb3.abi;

import com.google.common.collect.ImmutableList;
import dev.klepto.kweb3.type.*;
import lombok.With;
import lombok.val;

import java.util.stream.Collectors;

/**
 * Contains information about ethereum data type in-order to generates ABI-compatible type descriptor.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public record AbiTypeDescriptor(
        Class<? extends EthType> type,
        ImmutableList<AbiTypeDescriptor> children,
        boolean array,
        int valueSize,
        int arraySize
) {

    /**
     * Creates a type descriptor with default values.
     */
    public AbiTypeDescriptor() {
        this(null, ImmutableList.of(), false, -1, -1);
    }

    /**
     * Returns ABI-compatible type description string.
     *
     * @return the ABI type description
     */
    @Override
    public String toString() {
        val name = getTypeName();
        val suffix = getTypeSuffix();
        val result = name + suffix;
        if (type != EthTuple.class) {
            return result;
        }
        return "(" + name + ")" + suffix;
    }

    /**
     * Wraps this type descriptor in a tuple type. For example: <code>uint256</code> becomes <code>(uint256)</code>.
     * Useful when decoding blockchain function results which are wrapped in tuple by default.
     *
     * @return this type wrapped in a tuple
     */
    public AbiTypeDescriptor wrap() {
        return new AbiTypeDescriptor()
                .withType(EthTuple.class)
                .withChildren(ImmutableList.of(this));
    }

    private String getTypeSuffix() {
        val valueSuffix = valueSize > 0 ? "" + valueSize : "";
        val arraySuffix = "[" + (arraySize > 0 ? "" + arraySize : "") + "]";
        return valueSuffix + (array ? arraySuffix : "");
    }

    private String getTypeName() {
        if (type == EthAddress.class) {
            return "address";
        } else if (type == EthBytes.class) {
            return "bytes";
        } else if (type == EthInt.class) {
            return "int";
        } else if (type == EthUint.class) {
            return "uint";
        } else if (type == EthString.class) {
            return "string";
        } else if (type == EthBool.class) {
            return "bool";
        } else if (type == EthTuple.class) {
            return children.stream().map(AbiTypeDescriptor::toString).collect(Collectors.joining(","));
        }
        return null;
    }

    /**
     * Generates ABI type description for given {@link EthType} value.
     *
     * @param value the ethereum data value
     * @return the ABI type descriptor
     */
    public static AbiTypeDescriptor parse(EthType value) {
        // encoding tuples and tuple arrays
        if (value instanceof EthTuple
                || (value instanceof EthArray<?> array && array.getComponentType() == EthTuple.class)) {
            val isArray = value instanceof EthArray<?>;
            val arraySize = isArray ? ((EthArray<?>) value).capacity() : -1;
            val tuple = (EthTuple) (isArray ? ((EthArray<?>) value).get(0) : value);
            val children = tuple.stream()
                    .map(AbiTypeDescriptor::parse)
                    .collect(ImmutableList.toImmutableList());
            return new AbiTypeDescriptor()
                    .withType(EthTuple.class)
                    .withChildren(children)
                    .withArray(isArray)
                    .withArraySize(arraySize);
        }

        if (value instanceof EthSizedType sizedType && !(value instanceof EthAddress)) {
            return new AbiTypeDescriptor()
                    .withType(value.getClass())
                    .withValueSize(sizedType.size());
        } else if (value instanceof EthArray<?> array) {
            return new AbiTypeDescriptor()
                    .withType(array.getComponentType())
                    .withArray(true)
                    .withArraySize(array.capacity());
        }

        return new AbiTypeDescriptor()
                .withType(value.getClass());
    }

}
