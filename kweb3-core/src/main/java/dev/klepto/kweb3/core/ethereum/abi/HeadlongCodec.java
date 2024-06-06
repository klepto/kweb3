package dev.klepto.kweb3.core.ethereum.abi;

import com.esaulpaugh.headlong.abi.Address;
import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.abi.TupleType;
import dev.klepto.kweb3.core.ethereum.abi.descriptor.EthArrayTypeDescriptor;
import dev.klepto.kweb3.core.ethereum.abi.descriptor.EthSizedTypeDescriptor;
import dev.klepto.kweb3.core.ethereum.abi.descriptor.EthTupleTypeDescriptor;
import dev.klepto.kweb3.core.ethereum.abi.descriptor.TypeDescriptor;
import dev.klepto.kweb3.core.ethereum.type.EthNumericValue;
import dev.klepto.kweb3.core.ethereum.type.EthSizedValue;
import dev.klepto.kweb3.core.ethereum.type.EthValue;
import dev.klepto.kweb3.core.ethereum.type.primitive.*;
import io.ethers.core.FastHex;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import static dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress.address;
import static dev.klepto.kweb3.core.ethereum.type.primitive.EthArray.array;
import static dev.klepto.kweb3.core.ethereum.type.primitive.EthBool.bool;
import static dev.klepto.kweb3.core.ethereum.type.primitive.EthBytes.bytes;
import static dev.klepto.kweb3.core.ethereum.type.primitive.EthInt.int256;
import static dev.klepto.kweb3.core.ethereum.type.primitive.EthString.string;
import static dev.klepto.kweb3.core.ethereum.type.primitive.EthTuple.tuple;
import static dev.klepto.kweb3.core.ethereum.type.primitive.EthUint.uint256;
import static dev.klepto.kweb3.core.util.Collections.arrayCast;
import static dev.klepto.kweb3.core.util.Hex.toByteArray;

/**
 * Implementation of {@link AbiCodec} using {@link com.esaulpaugh.headlong} library.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class HeadlongCodec implements AbiCodec {

    /**
     * Decodes given ABI string into ethereum values contained within {@link EthTuple} using
     * {@link com.esaulpaugh.headlong} codec. Returns <code>null</code> if given ABI string contains no data.
     *
     * @param abi        the ABI data string
     * @param descriptor the type description of given string
     * @return a tuple containing decoded values or <code>null</code> if ABI string contained no data
     */
    @Override
    @Nullable
    public EthTuple decode(@NotNull String abi, @NotNull TypeDescriptor descriptor) {
        descriptor = descriptor instanceof EthTupleTypeDescriptor
                ? descriptor
                : descriptor.wrap();

        val tuple = TupleType.parse(descriptor.toAbiDescriptor());
        val data = toByteArray(abi);
        if (data.length == 0) {
            return null;
        }

        val result = tuple.decode(data);
        return (EthTuple) decodeValue(result, descriptor);
    }

    /**
     * Converts any given headlong value into appropriate {@link EthValue} value.
     *
     * @param value      the headlong value
     * @param descriptor the ethereum type descriptor
     * @return the decoded ethereum data value
     * @throws IllegalArgumentException if decoder for a given value was not found
     */
    private EthValue decodeValue(Object value, TypeDescriptor descriptor) throws IllegalArgumentException {
        if (value instanceof Address address) {
            return decodeAddress(address, descriptor);
        } else if (value instanceof byte[] bytes && descriptor instanceof EthSizedTypeDescriptor) {
            return decodeBytes(bytes, (EthSizedTypeDescriptor) descriptor);
        } else if (value instanceof Number number && descriptor instanceof EthSizedTypeDescriptor) {
            return decodeNumeric(number, (EthSizedTypeDescriptor) descriptor);
        } else if (value instanceof String string) {
            return decodeString(string, descriptor);
        } else if (value instanceof Boolean bool) {
            return decodeBool(bool, descriptor);
        } else if (value instanceof Object[] && descriptor instanceof EthArrayTypeDescriptor) {
            return decodeArray((Object[]) value, (EthArrayTypeDescriptor) descriptor);
        } else if (value instanceof Tuple tuple && descriptor instanceof EthTupleTypeDescriptor) {
            return decodeTuple(tuple, (EthTupleTypeDescriptor) descriptor);
        }

        throw new IllegalArgumentException("Couldn't decode headlong type: " + value);
    }

    /**
     * Converts headlong <code>Address</code> value into {@link EthAddress} value.
     *
     * @param value      the address value
     * @param descriptor the type descriptor
     * @return the decoded ethereum address value
     */
    private EthAddress decodeAddress(Address value, TypeDescriptor descriptor) {
        return address(value.toString());
    }

    /**
     * Converts <code>byte</code> array value into {@link EthBool} value.
     *
     * @param value      the byte array value
     * @param descriptor the type descriptor
     * @return the decoded ethereum bytes value
     */
    private EthBytes decodeBytes(byte[] value, EthSizedTypeDescriptor descriptor) {
        return bytes(value).withSize(descriptor.valueSize());
    }

    /**
     * Converts <code>Number</code> value into {@link EthNumericValue} value.
     *
     * @param value      the number value
     * @param descriptor the type descriptor
     * @return the decoded ethereum numeric value
     */
    private EthNumericValue decodeNumeric(Number value, EthSizedTypeDescriptor descriptor) {
        val integer = value instanceof BigInteger
                ? (BigInteger) value
                : BigInteger.valueOf(value.longValue());

        return descriptor.type().matches(EthInt.class)
                ? int256(integer).withSize(descriptor.valueSize())
                : uint256(integer).withSize(descriptor.valueSize());
    }

    /**
     * Converts <code>String</code> value into {@link EthString} value.
     *
     * @param value      the string value
     * @param descriptor the type descriptor
     * @return the decoded ethereum string value
     */
    private EthString decodeString(String value, TypeDescriptor descriptor) {
        return string(value);
    }

    /**
     * Converts <code>boolean</code> value into {@link EthBool} value.
     *
     * @param value      the boolean value
     * @param descriptor the type descriptor
     * @return the decoded ethereum bool value
     */
    private EthBool decodeBool(boolean value, TypeDescriptor descriptor) {
        return bool(value);
    }

    /**
     * Recursively converts array of values into {@link EthArray} value.
     *
     * @param value      the array of values
     * @param descriptor the type descriptor
     * @return the decoded ethereum array value
     */
    @SuppressWarnings("unchecked")
    private EthArray<?> decodeArray(Object[] value, EthArrayTypeDescriptor descriptor) {
        val values = Arrays.stream(value)
                .map(element -> decodeValue(element, descriptor.descriptor()))
                .toArray();
        val result = arrayCast(values, (Class<? extends EthValue>) descriptor.type().toClass());
        return array(descriptor.arraySize(), result);
    }

    /**
     * Recursively converts headlong {@link Tuple} value into {@link EthTuple} value.
     *
     * @param value      the headlong tuple value
     * @param descriptor the type descriptor
     * @return the decoded ethereum tuple value
     */
    private EthTuple decodeTuple(Tuple value, EthTupleTypeDescriptor descriptor) {
        val values = new ArrayList<>();
        for (var i = 0; i < value.size(); i++) {
            values.add(decodeValue(value.get(i), descriptor.children().get(i)));
        }
        return tuple(values.stream().map(EthValue.class::cast).toArray(EthValue[]::new));
    }

    /**
     * Encodes given {@link EthValue} value into an ABI string using {@link com.esaulpaugh.headlong} codec.
     *
     * @param value      the ethereum type value
     * @param descriptor the type description
     * @return the ABI encoded string containing the ethereum value
     */
    @Override
    @NotNull
    public String encode(@NotNull EthValue value, @NotNull TypeDescriptor descriptor) {
        if (value instanceof EthTuple) {
            return encode((EthTuple) value, descriptor);
        } else {
            return encode(tuple(value), descriptor.wrap());
        }
    }

    /**
     * Encodes given {@link EthTuple} value into an ABI string using {@link com.esaulpaugh.headlong} codec.
     *
     * @param value      the ethereum tuple value
     * @param descriptor the type description
     * @return the ABI encoded string containing the ethereum value
     */
    private String encode(EthTuple value, TypeDescriptor descriptor) {
        val tupleType = TupleType.parse(descriptor.toAbiDescriptor());
        val result = tupleType.encode((Tuple) encodeValue(value)).array();
        return FastHex.encodeWithoutPrefix(result);
    }

    /**
     * Converts any given {@link EthValue} value into equivalent {@link com.esaulpaugh.headlong} value.
     *
     * @param value the ethereum value
     * @return the headlong-compatible representation of the value
     * @throws IllegalArgumentException if encoder for a given value was not found
     */
    private Object encodeValue(EthValue value) throws IllegalArgumentException {
        if (value instanceof EthArray<?> array) {
            return encodeArray(array);
        } else if (value instanceof EthAddress address) {
            return encodeAddress(address);
        } else if (value instanceof EthBytes bytes) {
            return encodeBytes(bytes);
        } else if (value instanceof EthBool bool) {
            return encodeBool(bool);
        } else if (value instanceof EthNumericValue numeric) {
            return encodeNumeric(numeric);
        } else if (value instanceof EthString string) {
            return encodeString(string);
        } else if (value instanceof EthTuple tuple) {
            return encodeTuple(tuple);
        }

        throw new IllegalArgumentException("Couldn't encode ethereum type: " + value);
    }

    /**
     * Recursively converts {@link EthArray} value into regular JVM array value.
     *
     * @param value the ethereum array value
     * @return the headlong-compatible JVM array
     */
    private Object encodeArray(EthArray<?> value) {
        val result = value.stream()
                .map(this::encodeValue)
                .toArray();
        return arrayCast(result, result[0].getClass());
    }

    /**
     * Converts {@link EthAddress} value into {@link Address} value.
     *
     * @param value the ethereum address value
     * @return the headlong-compatible address value
     */
    private Address encodeAddress(EthAddress value) {
        return Address.wrap(value.toHex());
    }

    /**
     * Converts {@link EthBytes} value into <code>byte</code> array value.
     *
     * @param value the ethereum bytes value
     * @return the headlong-compatible byte array value
     */
    private byte[] encodeBytes(EthBytes value) {
        return value.toByteArray();
    }

    /**
     * Converts {@link EthNumericValue} value into {@link Number} value.
     *
     * @param value the ethereum numeric value
     * @return the headlong-compatible number value
     */
    private Number encodeNumeric(EthNumericValue value) {
        val integer = (BigInteger) value.value();
        if (value instanceof EthSizedValue sized) {
            if (sized.size() <= 32) {
                return integer.intValue();
            } else if (sized.size() <= 64) {
                return integer.longValue();
            }
        }
        return integer;
    }

    /**
     * Converts {@link EthString} value into {@link String} value.
     *
     * @param value the ethereum numeric value
     * @return the headlong-compatible string value
     */
    private String encodeString(EthString value) {
        return value.value();
    }

    /**
     * Converts {@link EthBool} value into <code>boolean</code> value.
     *
     * @param value the ethereum numeric value
     * @return the headlong-compatible boolean value
     */
    private boolean encodeBool(EthBool value) {
        return value.check();
    }

    /**
     * Converts {@link EthTuple} value into {@link Tuple} value.
     *
     * @param value the ethereum address value
     * @return the headlong-compatible tuple value
     */
    private Tuple encodeTuple(EthTuple value) {
        val values = new Object[value.size()];
        for (var i = 0; i < values.length; i++) {
            values[i] = encodeValue(value.get(i));
        }
        return Tuple.of(values);
    }

}
