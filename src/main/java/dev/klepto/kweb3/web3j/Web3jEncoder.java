package dev.klepto.kweb3.web3j;

import dev.klepto.kweb3.Web3Error;
import dev.klepto.kweb3.type.*;
import lombok.val;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Type;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Encodes solidity types to Web3j types.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Web3jEncoder {

    public Type encodeValue(Object value) {
        if (value.getClass().isArray()) {
            val stream = Arrays.stream((Object[]) value);
            val values = stream.map(this::encodeValue).toArray(Type[]::new);
            return new DynamicArray(values);
        } else if (value instanceof Boolean) {
            return new org.web3j.abi.datatypes.Bool((Boolean) value);
        } else if (value instanceof String) {
            return new org.web3j.abi.datatypes.Utf8String((String) value);
        } else if (value instanceof Address) {
            return new org.web3j.abi.datatypes.Address(((Address) value).getBigIntegerValue());
        } else if (value instanceof Uint256) {
            return new org.web3j.abi.datatypes.generated.Uint256(((Uint256) value).getValue());
        } else if (value instanceof Bytes) {
            return new org.web3j.abi.datatypes.DynamicBytes(((Bytes) value).getValue());
        } else if (value instanceof Struct) {
            val values = ((Struct) value).getValues().stream().map(this::encodeValue).collect(Collectors.toList());
            return new org.web3j.abi.datatypes.DynamicStruct(values);
        }
        throw new Web3Error("Couldn't encode value {} of type {} to web3j.", value, value.getClass());
    }

    public TypeReference encodeType(Class<?> type, boolean indexed) {
        try {
            val componentType = type.isArray() ? type.getComponentType() : type;
            var typeName = "";
            if (componentType == String.class) {
                typeName = "string";
            } else if (componentType == Boolean.class) {
                typeName = "bool";
            } else {
                val solidityType = (SolidityType) componentType.newInstance();
                typeName = solidityType.getEncodedName();
            }
            val parameterizedName = type.isArray() ? typeName + "[]" : typeName;
            return TypeReference.makeTypeReference(parameterizedName, indexed, false);
        } catch (Exception e) {
            throw new Web3Error("Couldn't encode type {} to web3j.", type);
        }
    }


}
