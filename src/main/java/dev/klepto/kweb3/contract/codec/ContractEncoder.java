package dev.klepto.kweb3.contract.codec;

import com.google.common.reflect.TypeToken;
import dev.klepto.kweb3.Web3Request;
import dev.klepto.kweb3.Web3Response;
import dev.klepto.kweb3.contract.ContractResponse;
import dev.klepto.kweb3.contract.Type;
import dev.klepto.kweb3.contract.event.Event;
import dev.klepto.kweb3.contract.event.Indexed;
import dev.klepto.kweb3.type.SolidityType;
import dev.klepto.kweb3.type.Struct;
import dev.klepto.kweb3.util.Reflection;
import lombok.SneakyThrows;
import lombok.val;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static dev.klepto.kweb3.Web3Error.error;
import static dev.klepto.kweb3.Web3Error.require;
import static dev.klepto.kweb3.util.Collections.arrayCast;

/**
 * Utilities for encoding web3 contract requests.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class ContractEncoder {

    private ContractEncoder() {
    }

    @SneakyThrows
    public static Object encodeParameterValue(Object value, TypeToken<?> type, Type annotation) {
        val solidityType = ContractCodec.getSolidityType(type, annotation);
        if (solidityType == null) {
            return null;
        }

        // Encoding not needed.
        if (solidityType.isInstance(value)) {
            return value;
        }

        // For array types, encode values one by one.
        if (solidityType.isArray()) {
            val isList = value instanceof List;
            val isArray = value.getClass().isArray();
            val componentType = solidityType.getComponentType();
            require(
                    isList || isArray,
                    "Only {}[] or List<{}> type allowed for encoding array type.",
                    componentType, componentType
            );

            val valueStream = isList ? ((List) value).stream() : Arrays.stream((Object[]) value);
            val encodedValues = valueStream
                    .map(element -> encodeParameterValue(element, TypeToken.of(componentType), null))
                    .toArray();
            return arrayCast(encodedValues, componentType);
        }

        if (solidityType == Struct.class) {
            return encodeStructValue(value);
        }

        // Attempt calling constructor of solidity type with given value.
        return Reflection.create(solidityType, value);
    }

    @SneakyThrows
    public static Object encodeStructValue(Object value) {
        val values = new ArrayList<SolidityType>();
        for (val field : value.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            val fieldValue = field.get(value);
            val fieldType = TypeToken.of(field.getGenericType());
            val fieldAnnotation = field.getAnnotation(Type.class);
            values.add((SolidityType) encodeParameterValue(fieldValue, fieldType, fieldAnnotation));
        }
        return new Struct(values);
    }

    public static Class<?> encodeParameterType(TypeToken<?> type, Type annotation) {
        // If annotated, use that before anything else.
        if (annotation != null) {
            val types = annotation.value();
            require(types.length == 1, "@Type annotation on parameters can only have 1 solidity type.");
            return types[0];
        }

        // If solidity type, no annotation required.
        val solidityType = ContractCodec.getSolidityType(type, null);
        if (solidityType != null) {
            return solidityType;
        }

        error("Couldn't encode parameter type {}.", type);
        return null;
    }

    @SneakyThrows
    public static List<Class<?>> encodeReturnTypes(TypeToken<?> type, Type annotation) {
        // If annotated, use that before anything else.
        if (annotation != null) {
            return Arrays.stream(annotation.value()).collect(Collectors.toList());
        }

        // If primitive type, no annotation required.
        if (ContractCodec.isPrimitiveType(type)) {
            return List.of(type.getRawType());
        }

        // If solidity type, no annotation required.
        val solidityType = ContractCodec.getSolidityType(type, null);
        if (solidityType != null) {
            return List.of(solidityType);
        }

        // If Web3Response, no return type required.
        if (type.getRawType() == Web3Response.class || type.getRawType() == ContractResponse.class) {
            return Collections.emptyList();
        }

        // Otherwise it's a struct, parse fields to find out the types.
        val fields = type.getRawType().getDeclaredFields();
        val fieldTypes = new ArrayList<Class<?>>();
        for (val field : fields) {
            val fieldType = TypeToken.of(field.getGenericType());
            val fieldAnnotation = field.getAnnotation(Type.class);
            fieldTypes.add(encodeParameterType(fieldType, fieldAnnotation));
        }
        return fieldTypes;
    }

    public static List<Web3Request.Event> encodeEvents(Class<?> contractType) {
        val eventClasses = encodeEventClasses(contractType);
        val events = new ArrayList<Web3Request.Event>();
        for (val eventClass : eventClasses) {
            events.add(encodeEvent(eventClass));
        }
        return events;
    }

    public static List<Class<?>> encodeEventClasses(Class<?> contractType) {
        return Arrays.stream(contractType.getDeclaredClasses())
                .filter(type -> type.isAnnotationPresent(Event.class))
                .collect(Collectors.toList());
    }

    public static String encodeEventName(Class<?> eventClass) {
        var name = eventClass.getAnnotation(Event.class).value();
        if (name.isBlank()) {
            name = eventClass.getSimpleName();
        }
        return name;
    }

    public static Web3Request.Event encodeEvent(Class<?> eventClass) {
        val name = encodeEventName(eventClass);
        val valueTypes = new ArrayList<Class<?>>();
        val fields = eventClass.getDeclaredFields();
        val indexedValues = new boolean[fields.length];
        for (var i = 0; i < fields.length; i++) {
            val field = fields[i];
            val fieldType = TypeToken.of(field.getGenericType());
            val fieldAnnotation = field.getAnnotation(Type.class);
            indexedValues[i] = field.isAnnotationPresent(Indexed.class);
            valueTypes.add(encodeParameterType(fieldType, fieldAnnotation));
        }
        return new Web3Request.Event(name, valueTypes, indexedValues);
    }

}
