package dev.klepto.kweb3.contract;


import dev.klepto.kweb3.Web3Result;
import dev.klepto.kweb3.abi.descriptor.EthArrayTypeDescriptor;
import dev.klepto.kweb3.abi.descriptor.EthSizedTypeDescriptor;
import dev.klepto.kweb3.abi.descriptor.EthTupleTypeDescriptor;
import dev.klepto.kweb3.abi.descriptor.TypeDescriptor;
import dev.klepto.kweb3.contract.annotation.ArraySize;
import dev.klepto.kweb3.contract.annotation.ValueSize;
import dev.klepto.kweb3.type.*;
import dev.klepto.unreflect.MethodAccess;
import dev.klepto.unreflect.ParameterAccess;
import dev.klepto.unreflect.UnreflectType;
import dev.klepto.unreflect.property.Reflectable;
import lombok.val;

import java.util.List;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static dev.klepto.kweb3.util.Conditions.require;

/**
 * Handles encoding/decoding of {@link EthType} JVM types for use with {@link ContractExecutor}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class ContractCodec {

    /**
     * Parses a tuple type descriptor for a given list of {@link Reflectable} types. List can contain method parameters,
     * class fields or methods themselves in-order to infer their ABI signature based on their JVM types.
     *
     * @param reflectables the list of reflectables (methods, fields, parameters)
     * @return the ABI-compatible type descriptor
     */
    public static TypeDescriptor parseTupleDescriptor(List<? extends Reflectable> reflectables) {
        val values = reflectables.stream()
                .map(ContractCodec::parseDescriptor)
                .collect(toImmutableList());

        return new EthTupleTypeDescriptor(values);
    }

    /**
     * Parses a type descriptor for a given {@link Reflectable}, usually {@link MethodAccess} for contract return type
     * description, or {@link ParameterAccess} for contract input type description.
     *
     * @param reflectable the reflectable for ABI type inference
     * @return the ABI-compatible type descriptor
     */
    public static TypeDescriptor parseDescriptor(Reflectable reflectable) {
        val arraySizeAnnotation = reflectable.annotation(ArraySize.class);
        val valueSizeAnnotation = reflectable.annotation(ValueSize.class);
        val arraySize = arraySizeAnnotation != null ? arraySizeAnnotation.value() : -1;
        val valueSize = valueSizeAnnotation != null ? valueSizeAnnotation.value() : -1;
        return parseDescriptor(reflectable.type(), arraySize, valueSize);
    }

    /**
     * Parses a type descriptor of a given {@link EthType} represented by {@link UnreflectType} with a given value size
     * and array size. Used for annotation processing.
     *
     * @param type      the ethereum data type
     * @param valueSize the value size, or -1 if size is not specified
     * @param arraySize the array size , or -1 if size is not specified
     * @return the ABI-compatible type descriptor
     */
    public static TypeDescriptor parseDescriptor(UnreflectType type, int valueSize, int arraySize) {
        if (type.matchesExact(Web3Result.class)) {
            val genericType = type.genericType();
            require(genericType != null, "Contract functions with Web3Result return type must have a generic type.");
            return parseDescriptor(genericType, valueSize, arraySize);
        }

        if (type.matchesExact(EthArray.class)) {
            return parseArrayDescriptor(type, arraySize, valueSize);
        } else if (!type.matches(EthType.class)) {
            return parseTupleDescriptor(type);
        }

        // Hard-coded defaults, not a huge fan.
        if (valueSize == -1) {
            if (type.matches(EthUint.class) || type.matches(EthInt.class)) {
                valueSize = 256;
            } else if (type.matches(EthBytes.class)) {
                valueSize = 32;
            }
        }

        return new EthSizedTypeDescriptor(type, valueSize);
    }

    /**
     * Parses a type descriptor for {@link EthArray} by inferring its generic type.
     *
     * @param type      the type representing ethereum array
     * @param valueSize the value size, or -1 if size is not specified
     * @param arraySize the array size , or -1 if size is not specified
     * @return the ABI-compatible array type descriptor
     */
    private static TypeDescriptor parseArrayDescriptor(UnreflectType type, int valueSize, int arraySize) {
        require(type.matchesExact(EthArray.class), "Not EthArray type.");

        val componentType = type.genericType();
        require(componentType != null, "EthArray must contain generic type.");

        val typeDescriptor = parseDescriptor(componentType, valueSize, arraySize);
        return new EthArrayTypeDescriptor(typeDescriptor, arraySize);
    }

    /**
     * Parses a tuple type descriptor for any JVM type based on its fields. Mainly used in encoding/decoding of
     * structs.
     *
     * @param type the JVM type containing ethereum data fields
     * @return the ABI-compatible array type descriptor
     */
    private static TypeDescriptor parseTupleDescriptor(UnreflectType type) {
        return parseTupleDescriptor(type.reflect().fields().toList());
    }


}
