package dev.klepto.kweb3.abi;

import com.esaulpaugh.headlong.abi.TupleType;
import com.esaulpaugh.headlong.util.FastHex;
import dev.klepto.kweb3.abi.type.*;
import dev.klepto.kweb3.abi.type.util.Convertibles;
import dev.klepto.kweb3.abi.type.util.Hex;
import dev.klepto.kweb3.abi.type.util.Types;
import lombok.val;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import static dev.klepto.kweb3.Web3Error.error;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class HeadlongCodec implements AbiEncoder, AbiDecoder {

    @Override
    public Tuple decode(String abi, AbiType type) {
        type = type.getType() == Tuple.class ? type : type.wrapTuple();
        val tuple = TupleType.parse(type.toString());
        val data = Hex.toByteArray(abi);
        if (data.length == 0) {
            return null;
        }

        val result = tuple.decode(data);
        return (Tuple) decodeValue(result, type);
    }

    private Object decodeValue(Object value, AbiType type) {
        if (value.getClass() != byte[].class && value.getClass().isArray()) {
            return Arrays.stream((Object[]) value)
                    .map(element -> decodeValue(element, type))
                    .toArray();
        }

        val size = type.getValueSize();
        if (type.getType() == Address.class) {
            return Types.address(((com.esaulpaugh.headlong.abi.Address) value).value());
        } else if (type.getType() == Bytes.class) {
            return Convertibles.toBytes(value).withSize(size);
        } else if (type.getType() == Int.class) {
            return Convertibles.toInt256(value).withSize(size);
        } else if (type.getType() == Uint.class) {
            return Convertibles.toUint256(value).withSize(size);
        } else if (type.getType() == String.class) {
            return value;
        } else if (type.getType() == boolean.class) {
            return value;
        } else if (type.getType() == Tuple.class) {
            val tuple = (com.esaulpaugh.headlong.abi.Tuple) value;
            val values = new ArrayList<>();
            for (var i = 0; i < tuple.size(); i++) {
                values.add(decodeValue(tuple.get(i), type.getChildren().get(i)));
            }
            return Types.tuple(values);
        }

        error("Couldn't decode type {} to type {}.", value.getClass(), type.getType());
        return null;
    }

    @Override
    public String encode(Tuple value, AbiType type) {
        type = type.getType() == Tuple.class ? type : type.wrapTuple();
        val tupleType = TupleType.parse(type.toString());
        val result = tupleType.encode((com.esaulpaugh.headlong.abi.Tuple) encodeValue(value)).array();
        return FastHex.encodeToString(result, 0, result.length);
    }

    private Object encodeValue(Object value) {
        if (value.getClass().isArray()) {
            val result = Arrays.stream((Object[]) value)
                    .map(element -> element.getClass().cast(element))
                    .map(this::encodeValue)
                    .toArray();
            return Types.arrayCast(result, result[0].getClass());
        }

        if (value instanceof Address result) {
            val checksum = com.esaulpaugh.headlong.abi.Address.toChecksumAddress(result.toHex());
            return com.esaulpaugh.headlong.abi.Address.wrap(checksum);
        } else if (value instanceof Bytes result) {
            return result.getValue();
        } else if (value instanceof AbiNumericValue result) {
            val bigInteger = (BigInteger) result.getValue();
            if (result.getSize() <= 32) {
                return bigInteger.intValue();
            } else if (result.getSize() <= 64) {
                return bigInteger.longValue();
            }
            return result.getValue();
        } else if (value instanceof String result) {
            return result;
        } else if (value instanceof Boolean result) {
            return result;
        } else if (value instanceof Tuple result) {
            val values = new Object[result.size()];
            for (var i = 0; i < result.size(); i++) {
                values[i] = encodeValue(result.get(i));
            }
            return com.esaulpaugh.headlong.abi.Tuple.of(values);
        }

        error("Couldn't encode type {}.", value.getClass());
        return null;
    }

}
