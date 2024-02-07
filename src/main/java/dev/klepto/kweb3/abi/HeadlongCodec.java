package dev.klepto.kweb3.abi;

import com.esaulpaugh.headlong.abi.Address;
import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.abi.TupleType;
import com.esaulpaugh.headlong.util.FastHex;
import dev.klepto.kweb3.abi.descriptor.EthArrayTypeDescriptor;
import dev.klepto.kweb3.abi.descriptor.EthSizedTypeDescriptor;
import dev.klepto.kweb3.abi.descriptor.EthTupleTypeDescriptor;
import dev.klepto.kweb3.abi.descriptor.TypeDescriptor;
import dev.klepto.kweb3.type.*;
import lombok.val;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import static dev.klepto.kweb3.type.EthAddress.address;
import static dev.klepto.kweb3.type.EthArray.array;
import static dev.klepto.kweb3.type.EthBool.bool;
import static dev.klepto.kweb3.type.EthBytes.bytes;
import static dev.klepto.kweb3.type.EthInt.int256;
import static dev.klepto.kweb3.type.EthString.string;
import static dev.klepto.kweb3.type.EthTuple.tuple;
import static dev.klepto.kweb3.type.EthUint.uint256;
import static dev.klepto.kweb3.util.Collections.arrayCast;
import static dev.klepto.kweb3.util.Hex.toByteArray;

/**
 * Implementation of {@link AbiCodec} using {@link com.esaulpaugh.headlong} library.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class HeadlongCodec implements AbiCodec {

    @Override
    public EthTuple decode(String abi, TypeDescriptor descriptor) {
        descriptor = descriptor instanceof EthTupleTypeDescriptor
                ? descriptor
                : descriptor.wrap();

        val tuple = TupleType.parse(descriptor.toAbiDescriptor());
        val data = toByteArray(abi);
        if (data.length == 0) {
            return null;
        }

        val result = tuple.decode(data);
        return (EthTuple) decodeHeadlongValue(result, descriptor);
    }

    private Object decodeHeadlongValue(Object value, TypeDescriptor descriptor) {
        val valueSize = descriptor instanceof EthSizedTypeDescriptor sized ? sized.valueSize() : -1;
        if (value.getClass().isArray() && descriptor instanceof EthArrayTypeDescriptor arrayDescriptor) {
            val array = (Object[]) value;
            val result = arrayCast(
                    Arrays.stream(array)
                            .map(element -> decodeHeadlongValue(element, arrayDescriptor.descriptor()))
                            .toArray(),
                    (Class<? extends EthType>) arrayDescriptor.type().toClass()
            );
            return array(arrayDescriptor.arraySize(), result);
        } else if (descriptor.type().matchesExact(EthAddress.class)) {
            return address(((Address) value).toString());
        } else if (descriptor.type().matchesExact(EthBytes.class)) {
            return bytes((byte[]) value).withSize(valueSize);
        } else if (descriptor.type().matchesExact(EthInt.class)) {
            if (value instanceof Integer integer) {
                return int256(integer).withSize(valueSize);
            } else {
                return int256((BigInteger) value).withSize(valueSize);
            }
        } else if (descriptor.type().matchesExact(EthUint.class)) {
            if (value instanceof Integer integer) {
                return uint256(integer).withSize(valueSize);
            } else {
                return uint256((BigInteger) value).withSize(valueSize);
            }
        } else if (descriptor.type().matchesExact(EthString.class)) {
            return string((String) value);
        } else if (descriptor.type().matchesExact(EthBool.class)) {
            return bool((boolean) value);
        } else if (descriptor instanceof EthTupleTypeDescriptor tupleDescriptor) {
            val tuple = (Tuple) value;
            val values = new ArrayList<>();
            for (var i = 0; i < tuple.size(); i++) {
                values.add(decodeHeadlongValue(tuple.get(i), tupleDescriptor.children().get(i)));
            }
            return tuple(values.stream().map(EthType.class::cast).toArray(EthType[]::new));
        }

        throw new IllegalArgumentException("Couldn't decode headlong type: " + value);
    }

    @Override
    public String encode(EthType value) {
        return encode(value, TypeDescriptor.parse(value));
    }

    @Override
    public String encode(EthType value, TypeDescriptor descriptor) {
        if (value instanceof EthTuple) {
            return encode((EthTuple) value, descriptor);
        } else {
            return encode(tuple(value), descriptor.wrap());
        }
    }

    private String encode(EthTuple value, TypeDescriptor descriptor) {
        val tupleType = TupleType.parse(descriptor.toAbiDescriptor());
        val result = tupleType.encode((Tuple) encodeHeadlongValue(value)).array();
        return FastHex.encodeToString(result, 0, result.length);
    }

    private Object encodeHeadlongValue(Object value) {
        if (value instanceof EthArray<?> array) {
            val result = array.stream()
                    .map(this::encodeHeadlongValue)
                    .toArray();
            return arrayCast(result, result[0].getClass());
        } else if (value instanceof EthAddress address) {
            return Address.wrap(address.toChecksumHex());
        } else if (value instanceof EthBytes bytes) {
            return bytes.toByteArray();
        } else if (value instanceof EthNumericType numeric
                && value instanceof EthSizedType sized) {
            val bigInteger = (BigInteger) numeric.value();
            if (sized.size() <= 32) {
                return bigInteger.intValue();
            } else if (sized.size() <= 64) {
                return bigInteger.longValue();
            }
            return bigInteger;
        } else if (value instanceof EthString string) {
            return string.value();
        } else if (value instanceof EthBool bool) {
            return bool.value();
        } else if (value instanceof EthTuple tuple) {
            val values = new Object[tuple.size()];
            for (var i = 0; i < values.length; i++) {
                values[i] = encodeHeadlongValue(tuple.get(i));
            }
            return Tuple.of(values);
        }

        throw new IllegalArgumentException("Couldn't encode ethereum type: " + value);
    }


}
