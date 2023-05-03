package dev.klepto.kweb3.abi.type.util;

import com.google.common.collect.Streams;
import dev.klepto.kweb3.abi.type.*;
import lombok.val;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Easy to access, flexible, static utilities for most contract/ABI use-cases.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Types {

    private Types() {

    }

    public static <T> T[] array(T... elements) {
        return elements;
    }

    public static <T> T[] arrayCast(Object object, Class<T> componentType) {
        val array = (Object[]) object;
        val newArray = Array.newInstance(componentType, array.length);
        System.arraycopy(array, 0, newArray, 0, array.length);
        return (T[]) newArray;
    }

    public static Address address(Object value) {
        return new Address(value);
    }

    public static Decimal decimal(Object value) {
        return decimal(value, 0);
    }

    public static Decimal decimal(Object value, Object scale) {
        val bigDecimal = Convertibles.toBigDecimal(value);
        return Decimal.create(bigDecimal, Convertibles.toInteger(scale));
    }

    public static Uint tokens(Object value) {
        return tokens(value, Decimal.MINIMUM_DECIMALS);
    }

    public static Uint tokens(Object value, Object decimals) {
        val scale = decimal(decimals).negate();
        return decimal(value, scale).toUint256();
    }

    public static Bytes bytes(Object value) {
        return Convertibles.toBytes(value);
    }

    public static Bytes bytes1(Object value) {
        return Convertibles.toBytes1(value);
    }

    public static Bytes bytes2(Object value) {
        return Convertibles.toBytes2(value);
    }

    public static Bytes bytes3(Object value) {
        return Convertibles.toBytes3(value);
    }

    public static Bytes bytes4(Object value) {
        return Convertibles.toBytes4(value);
    }

    public static Bytes bytes5(Object value) {
        return Convertibles.toBytes5(value);
    }

    public static Bytes bytes6(Object value) {
        return Convertibles.toBytes6(value);
    }

    public static Bytes bytes7(Object value) {
        return Convertibles.toBytes7(value);
    }

    public static Bytes bytes8(Object value) {
        return Convertibles.toBytes8(value);
    }

    public static Bytes bytes9(Object value) {
        return Convertibles.toBytes9(value);
    }

    public static Bytes bytes10(Object value) {
        return Convertibles.toBytes10(value);
    }

    public static Bytes bytes11(Object value) {
        return Convertibles.toBytes11(value);
    }

    public static Bytes bytes12(Object value) {
        return Convertibles.toBytes12(value);
    }

    public static Bytes bytes13(Object value) {
        return Convertibles.toBytes13(value);
    }

    public static Bytes bytes14(Object value) {
        return Convertibles.toBytes14(value);
    }

    public static Bytes bytes15(Object value) {
        return Convertibles.toBytes15(value);
    }

    public static Bytes bytes16(Object value) {
        return Convertibles.toBytes16(value);
    }

    public static Bytes bytes17(Object value) {
        return Convertibles.toBytes17(value);
    }

    public static Bytes bytes18(Object value) {
        return Convertibles.toBytes18(value);
    }

    public static Bytes bytes19(Object value) {
        return Convertibles.toBytes19(value);
    }

    public static Bytes bytes20(Object value) {
        return Convertibles.toBytes20(value);
    }

    public static Bytes bytes21(Object value) {
        return Convertibles.toBytes21(value);
    }

    public static Bytes bytes22(Object value) {
        return Convertibles.toBytes22(value);
    }

    public static Bytes bytes23(Object value) {
        return Convertibles.toBytes23(value);
    }

    public static Bytes bytes24(Object value) {
        return Convertibles.toBytes24(value);
    }

    public static Bytes bytes25(Object value) {
        return Convertibles.toBytes25(value);
    }

    public static Bytes bytes26(Object value) {
        return Convertibles.toBytes26(value);
    }

    public static Bytes bytes27(Object value) {
        return Convertibles.toBytes27(value);
    }

    public static Bytes bytes28(Object value) {
        return Convertibles.toBytes28(value);
    }

    public static Bytes bytes29(Object value) {
        return Convertibles.toBytes29(value);
    }

    public static Bytes bytes30(Object value) {
        return Convertibles.toBytes30(value);
    }

    public static Bytes bytes31(Object value) {
        return Convertibles.toBytes31(value);
    }

    public static Bytes bytes32(Object value) {
        return Convertibles.toBytes32(value);
    }

    public static Tuple tuple(Object... values) {
        return tuple(Arrays.stream(values));
    }

    public static Tuple tuple(Iterable<Object> values) {
        return tuple(Streams.stream(values));
    }

    public static Tuple tuple(Stream<Object> values) {
        return new Tuple(values.toList());
    }

    public static Uint uint8(Object value) {
        return Convertibles.toUint8(value);
    }

    public static Uint uint16(Object value) {
        return Convertibles.toUint16(value);
    }

    public static Uint uint24(Object value) {
        return Convertibles.toUint24(value);
    }

    public static Uint uint32(Object value) {
        return Convertibles.toUint32(value);
    }

    public static Uint uint40(Object value) {
        return Convertibles.toUint40(value);
    }

    public static Uint uint48(Object value) {
        return Convertibles.toUint48(value);
    }

    public static Uint uint56(Object value) {
        return Convertibles.toUint56(value);
    }

    public static Uint uint64(Object value) {
        return Convertibles.toUint64(value);
    }

    public static Uint uint72(Object value) {
        return Convertibles.toUint72(value);
    }

    public static Uint uint80(Object value) {
        return Convertibles.toUint80(value);
    }

    public static Uint uint88(Object value) {
        return Convertibles.toUint88(value);
    }

    public static Uint uint96(Object value) {
        return Convertibles.toUint96(value);
    }

    public static Uint uint104(Object value) {
        return Convertibles.toUint104(value);
    }

    public static Uint uint112(Object value) {
        return Convertibles.toUint112(value);
    }

    public static Uint uint120(Object value) {
        return Convertibles.toUint120(value);
    }

    public static Uint uint128(Object value) {
        return Convertibles.toUint128(value);
    }

    public static Uint uint136(Object value) {
        return Convertibles.toUint136(value);
    }

    public static Uint uint144(Object value) {
        return Convertibles.toUint144(value);
    }

    public static Uint uint152(Object value) {
        return Convertibles.toUint152(value);
    }

    public static Uint uint160(Object value) {
        return Convertibles.toUint160(value);
    }

    public static Uint uint168(Object value) {
        return Convertibles.toUint168(value);
    }

    public static Uint uint176(Object value) {
        return Convertibles.toUint176(value);
    }

    public static Uint uint184(Object value) {
        return Convertibles.toUint184(value);
    }

    public static Uint uint192(Object value) {
        return Convertibles.toUint192(value);
    }

    public static Uint uint200(Object value) {
        return Convertibles.toUint200(value);
    }

    public static Uint uint208(Object value) {
        return Convertibles.toUint208(value);
    }

    public static Uint uint216(Object value) {
        return Convertibles.toUint216(value);
    }

    public static Uint uint224(Object value) {
        return Convertibles.toUint224(value);
    }

    public static Uint uint232(Object value) {
        return Convertibles.toUint232(value);
    }

    public static Uint uint240(Object value) {
        return Convertibles.toUint240(value);
    }

    public static Uint uint248(Object value) {
        return Convertibles.toUint248(value);
    }

    public static Uint uint256(Object value) {
        return Convertibles.toUint256(value);
    }

    public static Int int8(Object value) {
        return Convertibles.toInt8(value);
    }

    public static Int int16(Object value) {
        return Convertibles.toInt16(value);
    }

    public static Int int24(Object value) {
        return Convertibles.toInt24(value);
    }

    public static Int int32(Object value) {
        return Convertibles.toInt32(value);
    }

    public static Int int40(Object value) {
        return Convertibles.toInt40(value);
    }

    public static Int int48(Object value) {
        return Convertibles.toInt48(value);
    }

    public static Int int56(Object value) {
        return Convertibles.toInt56(value);
    }

    public static Int int64(Object value) {
        return Convertibles.toInt64(value);
    }

    public static Int int72(Object value) {
        return Convertibles.toInt72(value);
    }

    public static Int int80(Object value) {
        return Convertibles.toInt80(value);
    }

    public static Int int88(Object value) {
        return Convertibles.toInt88(value);
    }

    public static Int int96(Object value) {
        return Convertibles.toInt96(value);
    }

    public static Int int104(Object value) {
        return Convertibles.toInt104(value);
    }

    public static Int int112(Object value) {
        return Convertibles.toInt112(value);
    }

    public static Int int120(Object value) {
        return Convertibles.toInt120(value);
    }

    public static Int int128(Object value) {
        return Convertibles.toInt128(value);
    }

    public static Int int136(Object value) {
        return Convertibles.toInt136(value);
    }

    public static Int int144(Object value) {
        return Convertibles.toInt144(value);
    }

    public static Int int152(Object value) {
        return Convertibles.toInt152(value);
    }

    public static Int int160(Object value) {
        return Convertibles.toInt160(value);
    }

    public static Int int168(Object value) {
        return Convertibles.toInt168(value);
    }

    public static Int int176(Object value) {
        return Convertibles.toInt176(value);
    }

    public static Int int184(Object value) {
        return Convertibles.toInt184(value);
    }

    public static Int int192(Object value) {
        return Convertibles.toInt192(value);
    }

    public static Int int200(Object value) {
        return Convertibles.toInt200(value);
    }

    public static Int int208(Object value) {
        return Convertibles.toInt208(value);
    }

    public static Int int216(Object value) {
        return Convertibles.toInt216(value);
    }

    public static Int int224(Object value) {
        return Convertibles.toInt224(value);
    }

    public static Int int232(Object value) {
        return Convertibles.toInt232(value);
    }

    public static Int int240(Object value) {
        return Convertibles.toInt240(value);
    }

    public static Int int248(Object value) {
        return Convertibles.toInt248(value);
    }

    public static Int int256(Object value) {
        return Convertibles.toInt256(value);
    }

}
