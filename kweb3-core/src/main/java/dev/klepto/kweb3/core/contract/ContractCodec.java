package dev.klepto.kweb3.core.contract;


import dev.klepto.kweb3.core.Web3Error;
import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.abi.descriptor.*;
import dev.klepto.kweb3.core.contract.annotation.ArraySize;
import dev.klepto.kweb3.core.contract.annotation.ValueSize;
import dev.klepto.kweb3.core.contract.type.EthStructContainer;
import dev.klepto.kweb3.core.contract.type.EthTupleContainer;
import dev.klepto.kweb3.core.type.*;
import dev.klepto.unreflect.MethodAccess;
import dev.klepto.unreflect.ParameterAccess;
import dev.klepto.unreflect.UnreflectType;
import dev.klepto.unreflect.property.Reflectable;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static dev.klepto.kweb3.core.type.EthArray.array;
import static dev.klepto.kweb3.core.type.EthTuple.tuple;
import static dev.klepto.kweb3.core.util.Conditions.require;
import static dev.klepto.unreflect.Unreflect.reflect;

/**
 * Handles encoding/decoding of {@link EthValue} types for use with {@link ContractExecutor}.<br>
 * <p>
 * Kweb3 has two available pipelines for interacting with smart contracts.<br><br>
 * <p>
 * Raw, verbose pipeline:<br>
 * <code>
 * ethereum data values -> abi encoder -> rpc call -> rpc response -> abi decoder
 * </code><br><br>
 * <p>
 * Easy to use, contract proxy pipeline:<br>
 * <code>
 * contract interface call -> contract encoder -> ethereum data values -> abi encoder -> rpc call -> rpc response -> abi
 * decoder -> contract decoder
 * </code><br><br>
 * <p>
 * This package and mainly this class is used to facilitate the latter pipeline, where we infer all required call
 * information from JVM method signatures and annotations.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public final class ContractCodec {

    private ContractCodec() {
    }

    /**
     * Parses a tuple type descriptor for a given list of {@link Reflectable} types. List can contain method parameters,
     * class fields or methods themselves in-order to infer their ABI signature based on their JVM types.
     *
     * @param reflectables the list of reflectables (methods, fields, parameters)
     * @return the ABI-compatible type descriptor
     */
    public static EthTupleTypeDescriptor parseTupleDescriptor(List<? extends Reflectable> reflectables) {
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
        return parseDescriptor(reflectable.type(), valueSize, arraySize);
    }

    /**
     * Parses a type descriptor of a given {@link EthValue} represented by {@link UnreflectType} with a given value size
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

        if (type.matches(EthSizedValue.class)) {
            // Hard-coded defaults, not a huge fan.
            if (valueSize == -1) {
                if (type.matches(EthUint.class) || type.matches(EthInt.class)) {
                    valueSize = 256;
                }
            }
            return new EthSizedTypeDescriptor(type, valueSize);
        }

        if (type.matches(EthBool.class)
                || type.matches(EthString.class)
                || type.matches(EthAddress.class)) {
            return new EthTypeDescriptor(type);
        }

        if (type.matches(EthArray.class)) {
            return parseArrayDescriptor(type, arraySize, valueSize);
        }

        if (type.matches(EthTupleContainer.class)) {
            return parseTupleContainerDescriptor(type);
        }

        throw new Web3Error("Couldn't parse descriptor for type: {}", type);
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
        require(type.matches(EthArray.class), "Not EthArray type.");

        val componentType = type.genericType();
        require(componentType != null, "EthArray must contain generic type.");

        val typeDescriptor = parseDescriptor(componentType, valueSize, arraySize);
        return new EthArrayTypeDescriptor(typeDescriptor, arraySize);
    }

    /**
     * Parses a tuple type descriptor for any JVM type based on its fields. Used for converting between JVM and ethereum
     * data types during contract calls.
     *
     * @param type the JVM type containing ethereum data fields
     * @return the ABI-compatible array type descriptor
     */
    private static TypeDescriptor parseTupleContainerDescriptor(UnreflectType type) {
        val fields = type.reflect().fields()
                .filter(field -> !field.isStatic())
                .toList();
        return parseTupleDescriptor(fields);
    }

    /**
     * Converts all given parameter values of a contract call to known {@link EthValue} values. Simply returns for
     * already known ethereum values, for JVM tuple containers, recursively encodes each field value.
     *
     * @param descriptor the tuple descriptor of the parameters
     * @param values     the array of values
     * @return normalized parameter values contained in a tuple
     */
    public static EthTuple encodeParameterValues(EthTupleTypeDescriptor descriptor, EthValue[] values) {
        require(values.length == descriptor.children().size(), "Value size mismatch.");
        val result = new ArrayList<EthValue>();
        for (var i = 0; i < values.length; i++) {
            result.add(encodeParameterValue(descriptor.children().get(i), values[i]));
        }
        return tuple(result);
    }

    /**
     * Converts given parameter value of a contract call to ABI-compatible {@link EthValue} value. If a value is regular
     * ethereum value, it simply returns it, if a value is a JVM tuple container or array of tuple containers, this
     * function will recursively encode every field in the container.
     *
     * @param descriptor the type descriptor
     * @param value      the value
     * @return normalized parameter value ready to be used with ABI encoding
     */
    public static EthValue encodeParameterValue(TypeDescriptor descriptor, EthValue value) {
        val isTupleDescriptor = descriptor instanceof EthTupleTypeDescriptor;
        val isArrayDescriptor = descriptor instanceof EthArrayTypeDescriptor;

        if (isArrayDescriptor) {
            require(value instanceof EthArray, "Incorrect EthArray value type.");
            return encodeArrayParameterValue((EthArrayTypeDescriptor) descriptor, (EthArray<?>) value);
        }

        if (isTupleDescriptor) {
            require(
                    value instanceof EthTuple || value instanceof EthTupleContainer,
                    "Incorrect EthTuple value type."
            );
            if (value instanceof EthTuple) {
                return value;
            }
            return encodeTupleParameterValue((EthTupleTypeDescriptor) descriptor, (EthTupleContainer) value);
        }

        return value;
    }

    /**
     * Converts given parameter array value to ABI-compatible {@link EthValue} value. If parameter value is already
     * ABI-compatible, function simply returns it. If parameter contains a {@link EthTupleContainer} or
     * {@link EthStructContainer}, it recursively encodes fields of each container to ABI-compatible values.
     *
     * @param descriptor the type descriptor
     * @param value      the value
     * @return normalized parameter value ready to be used with ABI encoding
     */
    public static EthArray<?> encodeArrayParameterValue(EthArrayTypeDescriptor descriptor, EthArray<?> value) {
        val componentDescriptor = descriptor.descriptor();
        if (!(componentDescriptor instanceof EthTupleTypeDescriptor)) {
            return value;
        }

        val result = value
                .stream()
                .map(element -> encodeParameterValue(componentDescriptor, (EthValue) element))
                .toList();

        return array(value.capacity(), result);
    }

    /**
     * Converts given parameter {@link EthTupleContainer} value to ABI-compatible {@link EthValue} value. Recursively
     * encodes all fields of the container to ABI-compatible values.
     *
     * @param descriptor the tuple type descriptor
     * @param value      the tuple container
     * @return normalized parameter value ready to be used with ABI encoding
     */
    public static EthTuple encodeTupleParameterValue(EthTupleTypeDescriptor descriptor, EthTupleContainer value) {
        val fields = reflect(value).fields()
                .filter(field -> !field.isStatic())
                .toList();
        require(fields.size() == descriptor.children().size(), "Tuple field size mismatch.");

        val result = new ArrayList<EthValue>();
        for (var i = 0; i < fields.size(); i++) {
            result.add(encodeParameterValue(descriptor.children().get(i), fields.get(i).get()));
        }

        return tuple(result);
    }

    /**
     * Decodes value to given return <code>type</code>. If expected return type is simple {@link EthValue} value, simply
     * unpacks it from the tuple and returns it. Otherwise, if expected return type is {@link EthTupleContainer} or
     * {@link EthArray array} of {@link EthTupleContainer}, recursively populates new instances of the containers.
     *
     * @param type  the expected return type
     * @param value the response value decoded from ABI response
     * @return decoded value matching given return type
     */
    public static EthValue decodeReturnValue(UnreflectType type, EthValue value) {
        if (value instanceof EthTuple tuple) {
            require(!tuple.isEmpty(), "Response returned empty tuple.");
        }

        if (type.matches(EthTupleContainer.class)) {
            require(value instanceof EthTuple, "Cannot parse {} into tuple container {}.", value, type);
            return decodeTupleContainer(type, (EthTuple) value);
        }

        if (type.matches(EthArray.class)) {
            val genericType = type.genericType();
            require(value instanceof EthArray, "Cannot parse {} as an EthArray.", value);
            require(genericType != null, "Could not infer EthArray type.");

            val array = (EthArray<?>) value;
            val result = new ArrayList<EthValue>();
            for (var i = 0; i < array.size(); i++) {
                result.add(decodeReturnValue(genericType, (EthTuple) array.get(i)));
            }

            return array(result);
        }

        require(type.matches(value.getClass()), "Could not parse {} as {}.", value, type);
        return value;
    }

    /**
     * Decodes tuple value into specified JVM container of {@link UnreflectType}. Used for struct and multiple value
     * return decoding.
     *
     * @param type  the JVM container
     * @param tuple the tuple values
     * @return the decoded JVM container containing tuple values
     */
    public static EthValue decodeTupleContainer(UnreflectType type, EthTuple tuple) {
        require(
                type.matches(EthTupleContainer.class),
                "Given type {} is not a {} or {}",
                type, EthTupleContainer.class, EthStructContainer.class
        );

        val classAccess = type.reflect();
        val fields = classAccess
                .fields()
                .filter(field -> !field.isStatic())
                .toList();
        require(fields.size() == tuple.size(), "Tuple container size mismatch: {}", type);

        val values = new ArrayList<>();
        for (var i = 0; i < tuple.size(); i++) {
            val field = fields.get(i);
            val value = tuple.get(i);
            val decodedValue = value instanceof EthTuple valueTuple
                    ? decodeTupleContainer(field.type(), valueTuple)
                    : value;
            values.add(decodedValue);
        }

        return (EthValue) classAccess.create(values.toArray());
    }

}
