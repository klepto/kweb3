package dev.klepto.kweb3.web3j;

import dev.klepto.kweb3.Web3Error;
import dev.klepto.kweb3.Web3Request;
import dev.klepto.kweb3.type.*;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.Bytes;
import dev.klepto.kweb3.type.sized.Uint256;
import lombok.val;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.Uint;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Encodes solidity types to Web3j types.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Web3jEncoder {

    static Type encodeValue(Object value) {
        if (value.getClass().isArray()) {
            val stream = Arrays.stream((Object[]) value);
            val values = stream.map(Web3jEncoder::encodeValue).toArray(Type[]::new);
            return new DynamicArray(values);
        } else if (value instanceof Boolean) {
            return new org.web3j.abi.datatypes.Bool((Boolean) value);
        } else if (value instanceof String) {
            return new org.web3j.abi.datatypes.Utf8String((String) value);
        } else if (value instanceof Address) {
            return new org.web3j.abi.datatypes.Address(((Address) value).toBigInteger());
        } else if (value instanceof Uint256) {
            return new org.web3j.abi.datatypes.generated.Uint256(((Uint256) value).getValue());
        } else if (value instanceof Bytes) {
            return new org.web3j.abi.datatypes.DynamicBytes(((Bytes) value).getValue());
        } else if (value instanceof Struct) {
            val values = ((Struct) value).getValue().stream().map(Web3jEncoder::encodeValue).toList();
            return new org.web3j.abi.datatypes.DynamicStruct(values);
        }
        throw new Web3Error("Couldn't encode value {} of type {} to web3j.", value, value.getClass());
    }

    static TypeReference<?> encodeType(Object type, boolean indexed) {
        try {
            return TypeReference.makeTypeReference(encodeTypeName(type), indexed, false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Web3Error("Couldn't encode type {} to web3j.", type);
        }
    }

    static String encodeTypeName(Object type) {
        // Struct array
        if (type.getClass().isArray()) {
            val structTypes = (Object[]) type;
            val typeNames = Arrays.stream(structTypes).map(Web3jEncoder::encodeTypeName)
                    .collect(Collectors.joining(","));
            return "(" + typeNames + ")[]";
        }

        val classType = (Class<?>) type;
        val componentType = classType.isArray() ? classType.getComponentType() : classType;
        var typeName = "";
        if (componentType == String.class) {
            typeName = "string";
        } else if (componentType == Boolean.class) {
            typeName = "bool";
        } else {
            typeName = componentType.getSimpleName().toLowerCase();
        }

        return typeName + (classType.isArray() ? "[]" : "");
    }

    static List<TypeReference<?>> encodeTypes(List<Object> types) {
        return types.stream().<TypeReference<?>>map(type -> encodeType(type, false)).toList();
    }

    static Function encodeFunction(Web3Request request) {
        val parameters = request.getParameters().stream()
                .map(Web3jEncoder::encodeValue)
                .toList();
        val returnTypes = (List<TypeReference<?>>) encodeTypes(request.getFunction().getReturnTypes());
        return new Function(request.getFunction().getName(), parameters, returnTypes);
    }

    static Event encodeEvent(Web3Request.Event event) {
        val name = event.getName();
        val parameters = new ArrayList<TypeReference<?>>();
        for (var i = 0; i < event.getValueTypes().size(); i++) {
            val parameter = event.getValueTypes().get(i);
            val indexed = event.getIndexedValues()[i];
            val reference = encodeType(parameter, indexed);
            parameters.add(reference);
        }
        return new Event(name, parameters);
    }

}
