package dev.klepto.kweb3.contract.codec;

import com.google.common.reflect.TypeToken;
import dev.klepto.kweb3.util.number.Numeric;
import lombok.SneakyThrows;
import lombok.val;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static dev.klepto.kweb3.Web3Error.error;
import static dev.klepto.kweb3.Web3Error.require;
import static dev.klepto.kweb3.util.Collections.cast;
import static dev.klepto.kweb3.util.Collections.list;

/**
 * Utilities for decoding web3 contract responses.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class ContractDecoder {

    private ContractDecoder() {
    }

    @SneakyThrows
    public static Object decodeReturnValues(List<Object> values, TypeToken<?> type) {
        // If single value, decode first element.
        if (values.size() == 1) {
            return decodeReturnValue(values.get(0), type);
        }

        // Decode collection elements one by one.
        val componentType = ContractCodec.getComponentType(type);
        if (componentType != null) {
            val decodedValues = values.stream()
                    .map(value -> decodeReturnValue(value, componentType))
                    .collect(Collectors.toList());
            return type.isArray() ? cast(decodedValues.toArray(), componentType.getRawType()) : decodedValues;
        }

        // Decode structs.
        if (values.isEmpty()) {
            return null;
        }

        val fields = type.getRawType().getDeclaredFields();
        require(
                fields.length == values.size(),
                "Fields count {} does not match result size for type {}.",
                fields.length, type
        );

        val fieldTypes = Arrays.stream(fields).map(Field::getType).toArray(Class[]::new);
        var constructor = type.getRawType().getDeclaredConstructor(fieldTypes);

        val fieldValues = new ArrayList<>();
        for (var i = 0; i < fields.length; i++) {
            val field = fields[i];
            val fieldType = TypeToken.of(field.getGenericType());
            val fieldValue = decodeReturnValue(values.get(i), fieldType);
            fieldValues.add(fieldValue);
        }

        return constructor.newInstance(fieldValues.toArray());
    }

    public static Object decodeReturnValue(Object value, TypeToken<?> type) {
        var rawType = type.getRawType();
        if (rawType.isInstance(value)) {
            return value;
        }

        if (value instanceof List) {
            return decodeReturnValues((List<Object>) value, type);
        }

        if (value.getClass().isArray()) {
            return decodeReturnValues(list(((Object[]) value)), type);
        }

        if (value instanceof Numeric<?,?>) {
            val convertedValue = Numeric.to(rawType, value);
            if (convertedValue != null) {
                return convertedValue;
            }
        }

        error("Couldn't decode {} value to {} type.", value, type);
        return null;
    }

}
