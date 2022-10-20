package dev.klepto.kweb3.contract.codec;

import com.google.common.reflect.TypeToken;
import dev.klepto.kweb3.contract.Type;
import dev.klepto.kweb3.type.SolidityType;
import dev.klepto.kweb3.type.sized.Uint256;
import lombok.SneakyThrows;
import lombok.val;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import static dev.klepto.kweb3.Web3Error.require;

/**
 * Utilities for enconding/decoding contract web3 requests.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class ContractCodec {

    private ContractCodec() {
    }

    public static Class<?> getSolidityType(TypeToken<?> type, Type annotation) {
        if (annotation != null) {
            val types = annotation.value();
            require(types.length == 1, "Multiple annotated types cannot be mapped to a single type.");
            return types[0];
        }

        if (isSolidityType(type)) {
            return type.getRawType();
        }

        // Map List type to array, they should be inter-changeable.
        if (type.isSubtypeOf(TypeToken.of(List.class))) {
            val componentType = getListComponentType(type);
            if (isSolidityType(componentType)) {
                val arrayType = Array.newInstance(componentType.getRawType(), 0);
                return arrayType.getClass();
            }
        }

        return null;
    }

    public static boolean isSolidityType(Class<?> type) {
        return isSolidityType(TypeToken.of(type));
    }

    public static boolean isSolidityType(java.lang.reflect.Type type) {
        return isSolidityType(TypeToken.of(type));
    }

    public static boolean isSolidityType(TypeToken<?> type) {
        val solidityType = TypeToken.of(SolidityType.class);
        if (type.isSubtypeOf(solidityType)) {
            return true;
        }

        if (type.isArray() && type.getComponentType() != null) {
            return type.getComponentType().isSubtypeOf(solidityType);
        }

        return false;
    }

    public static TypeToken<?> getComponentType(TypeToken<?> type) {
        if (type.isArray()) {
            return type.getComponentType();
        }

        if (type.isSubtypeOf(TypeToken.of(List.class))) {
            return getListComponentType(type);
        }

        return null;
    }

    public static boolean isPrimitiveType(TypeToken<?> type) {
        var rawType = type.getRawType();
        if (type.getComponentType() != null) {
            rawType = type.getComponentType().getRawType();
        }

        if (rawType == String.class) {
            return true;
        } else if (rawType == boolean.class || rawType == Boolean.class) {
            return true;
        }

        return false;
    }

    public static TypeToken<?> getListComponentType(TypeToken<?> type) {
        val parametirizedType = (ParameterizedType) type.getType();
        val typeArguments = parametirizedType.getActualTypeArguments();
        require(typeArguments.length == 1, "No generic type arguments found for a given list.");
        return TypeToken.of(typeArguments[0]);
    }

    @Type(Uint256.class)
    public static Type UINT256_TYPE = resolveFieldTypeAnnotation("UINT256_TYPE");

    @SneakyThrows
    private static Type resolveFieldTypeAnnotation(String name) {
        return ContractCodec.class.getDeclaredField(name).getAnnotation(Type.class);
    }

}
