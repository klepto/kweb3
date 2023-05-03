package dev.klepto.kweb3.contract;

import com.google.common.reflect.TypeToken;
import dev.klepto.kweb3.Web3Response;
import dev.klepto.kweb3.abi.type.*;
import dev.klepto.kweb3.abi.type.util.Convertibles;
import dev.klepto.kweb3.abi.type.util.Types;
import dev.klepto.kweb3.contract.event.EventDecoder;
import lombok.SneakyThrows;
import lombok.val;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.klepto.kweb3.Web3Error.require;
import static dev.klepto.kweb3.abi.type.util.Types.arrayCast;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class ContractCodec {

    private static final EventDecoder eventDecoder = new EventDecoder();

    private ContractCodec() {
    }

    @SneakyThrows
    public static Object encodeValue(Object value, ValueType type) {
        // Encode collections.
        val typeToken = TypeToken.of(value.getClass());
        if (isCollection(typeToken)) {
            val stream = isArray(typeToken)
                    ? Arrays.stream((Object[]) value)
                    : ((List) value).stream();
            return stream.map(element -> encodeValue(element, type)).toArray();
        }

        // Encode tuples.
        if (type.getAbiType().getType() == Tuple.class) {
            val fields = value.getClass().getDeclaredFields();
            val fieldTypes = type.getChildren();
            val values = new ArrayList<>();
            for (var i = 0; i < fields.length; i++) {
                val field = fields[i];
                field.setAccessible(true);

                val fieldValue = field.get(value);
                val fieldType = fieldTypes.get(i);
                values.add(encodeValue(fieldValue, fieldType));
            }
            return Types.tuple(values);
        }

        // Encode single values.
        return Convertibles.to(value, type.getAbiType().getType());
    }

    @SneakyThrows
    public static Object decodeValue(Object value, ValueType type) {
        // Decode collections.
        val typeToken = type.getType();
        val rawType = typeToken.getRawType();
        if (isCollection(typeToken)) {
            val componentType = type.getComponentType();
            val stream = Arrays.stream((Object[]) value)
                    .map(element -> decodeValue(element, componentType));
            return isList(typeToken)
                    ? stream.toList()
                    : arrayCast(stream.toArray(), componentType.getType().getRawType());
        }

        // Decode tuples.
        if (type.getAbiType().getType() == Tuple.class) {
            val values = ((Tuple) value).getValue();
            val types = type.getChildren();
            val parameters = new ArrayList<>();
            for (var i = 0; i < values.size(); i++) {
                val parameterValue = values.get(i);
                val parameterType = types.get(i);
                parameters.add(decodeValue(parameterValue, parameterType));
            }

            val constructor = rawType.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            return constructor.newInstance(parameters.toArray());
        }

        if (rawType.isInstance(value)) {
            return value;
        }

        // Decode single values.
        return Convertibles.to(value, rawType);
    }

    public static Object decodeResponse(Web3Response response) {
        if (response == null) {
            return null;
        }

        if (response.getError() != null) {
            throw response.getError();
        }

        val events = eventDecoder.decode(response);
        response = response.withEvents(events);

        val function = response.getRequest().getFunction();
        val returnType = function.getReturnType();
        if (returnType == null) {
            return response;
        }

        return decodeResponse(function, response.getResult());
    }

    public static Object decodeResponse(Function function, Tuple result) {
        if (result == null) {
            return null;
        }

        val returnType = function.getReturnType();

        if (function.isStruct()) {
            return decodeValue(result.getFirst(), returnType.unwrapTuple());
        } else if (returnType.getAbiType().getType() == Tuple.class) {
            return decodeValue(result, returnType);
        }

        return decodeValue(result.getFirst(), returnType);
    }

    public static ValueType parseParametersType(List<Parameter> parameters) {
        val definitions = parameters.stream()
                .map(parameter -> {
                    val type = TypeToken.of(parameter.getParameterizedType());
                    val annotation = parameter.getAnnotation(Type.class);
                    val indexed = parameter.isAnnotationPresent(Event.Indexed.class);
                    return new ValueDefinition(type, annotation, indexed);
                }).toList();
        return parseValuesType(definitions);
    }

    public static ValueType parseFieldsType(List<Field> fields) {
        val definitions = fields.stream()
                .map(field -> {
                    val type = TypeToken.of(field.getGenericType());
                    val annotation = field.getAnnotation(Type.class);
                    val indexed = field.isAnnotationPresent(Event.Indexed.class);
                    return new ValueDefinition(type, annotation, indexed);
                }).toList();
        return parseValuesType(definitions);
    }

    public static ValueType parseValuesType(List<ValueDefinition> definitions) {
        val children = definitions.stream()
                .map(def -> parseValueType(def.getType(), def.getAnnotation(), def.isIndexed()))
                .toList();
        val abiChildren = children.stream().map(ValueType::getAbiType).toList();
        return new ValueType(TypeToken.of(Tuple.class), children, new AbiType(Tuple.class, abiChildren, false, 0, 0), false);
    }

    public static ValueType parseValueType(TypeToken<?> type, Type annotation, boolean indexed) {
        val componentType = getComponentType(type);
        val arraySize = annotation == null ? 0 : annotation.arraySize();
        val valueSize = annotation == null ? 0 : annotation.valueSize();
        val annotationValueType = annotation == null ? null : annotation.value() == Type.class ? null : annotation.value();
        val valueType = annotationValueType == null ? componentType : getComponentType(TypeToken.of(annotationValueType));
        return parseValueType(type, valueType, valueSize, arraySize, indexed);
    }

    public static ValueType parseValueType(
            TypeToken<?> type,
            TypeToken<?> valueType,
            int valueSize,
            int arraySize,
            boolean indexed
    ) {
        if (valueType.getRawType() == Struct.class) {
            valueType = TypeToken.of(Tuple.class);
        }

        if (!isSolidityType(valueType)) {
            return null;
        }

        if (valueSize == 0) {
            if (valueType.getRawType() == Int.class) {
                valueSize = Int.MAX_SIZE;
            } else if (valueType.getRawType() == Uint.class) {
                valueSize = Uint.MAX_SIZE;
            }
        }

        val isCollection = isCollection(type);
        var children = List.<ValueType>of();
        if (valueType.getRawType() == Tuple.class) {
            val componentType = getComponentType(type).getRawType();
            children = parseFieldsType(Arrays.asList(componentType.getDeclaredFields())).getChildren();
        }

        val childrenTypes = children.stream().map(ValueType::getAbiType).toList();
        val abiType = new AbiType(valueType.getRawType(), childrenTypes, isCollection, valueSize, arraySize);
        return new ValueType(type, children, abiType, indexed);
    }

    public static boolean isSolidityType(TypeToken<?> type) {
        val unwrapped = type.unwrap().getRawType();
        if (unwrapped == Address.class) return true;
        else if (unwrapped == Bytes.class) return true;
        else if (unwrapped == Int.class) return true;
        else if (unwrapped == Uint.class) return true;
        else if (unwrapped == Tuple.class) return true;
        else if (unwrapped == boolean.class) return true;
        else if (unwrapped == String.class) return true;
        return false;
    }

    public static boolean isCollection(TypeToken<?> type) {
        return isArray(type) || isList(type);
    }

    public static boolean isArray(TypeToken<?> type) {
        return type.isArray();
    }

    public static boolean isList(TypeToken<?> type) {
        if (Tuple.class.isAssignableFrom(type.getRawType())) {
            return false;
        }

        return List.class.isAssignableFrom(type.getRawType());
    }

    public static TypeToken<?> getArrayType(TypeToken<?> type) {
        return TypeToken.of(Array.newInstance(type.getRawType(), 0).getClass());
    }

    public static TypeToken<?> getComponentType(TypeToken<?> type) {
        if (isArray(type)) {
            return type.getComponentType();
        }

        if (isList(type)) {
            return getListComponentType(type);
        }

        return type;
    }

    public static TypeToken<?> getListComponentType(TypeToken<?> type) {
        val parametirizedType = (ParameterizedType) type.getType();
        val typeArguments = parametirizedType.getActualTypeArguments();
        require(typeArguments.length == 1, "No generic type arguments found for a given list.");
        return TypeToken.of(typeArguments[0]);
    }


}
