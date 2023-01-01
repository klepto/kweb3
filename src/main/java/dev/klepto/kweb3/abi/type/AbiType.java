package dev.klepto.kweb3.abi.type;

import lombok.Value;
import lombok.With;
import lombok.val;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
@With
public class AbiType {

    Class<?> type;
    List<AbiType> children;
    boolean array;
    int valueSize;
    int arraySize;

    public AbiType(Class<?> type) {
        this(type, List.of(), false, 0, 0);
    }

    public AbiType(Class<?> type, List<AbiType> children, boolean array, int valueSize, int arraySize) {
        this.type = type;
        this.children = children;
        this.array = array;
        this.valueSize = valueSize;
        this.arraySize = arraySize;
    }

    public AbiType toStructType() {
        return new AbiType(Struct.class).withChildren(List.of(this));
    }

    @Override
    public String toString() {
        val name = getTypeName();
        val suffix = getTypeSuffix();
        val result = name + suffix;
        if (type != Struct.class) {
            return result;
        }
        return "(" + name + ")" + suffix;
    }

    private String getTypeSuffix() {
        val valueSuffix = valueSize > 0 ? "" + valueSize : "";
        val arraySuffix = "[" + (arraySize > 0 ? "" + arraySize : "") + "]";
        return valueSuffix + (array ? arraySuffix : "");
    }

    private String getTypeName() {
        if (type == Address.class) {
            return "address";
        } else if (type == Bytes.class) {
            return "bytes";
        } else if (type == Int.class) {
            return "int";
        } else if (type == Uint.class) {
            return "uint";
        } else if (type == String.class) {
            return "string";
        } else if (type == boolean.class) {
            return "bool";
        } else if (type == Struct.class) {
            return children.stream().map(AbiType::toString).collect(Collectors.joining(","));
        }
        return null;
    }

}
