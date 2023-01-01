package dev.klepto.kweb3.abi.type.util;

import dev.klepto.kweb3.abi.type.Bytes;
import dev.klepto.kweb3.abi.type.Int;
import dev.klepto.kweb3.abi.type.Uint;
import lombok.val;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Numeric<T extends Numeric<?>> extends Comparable<Object> {

    T withValue(Object newValue);

    Object getValue();

    default T add(Object value) {
        val newValue = toBigDecimal().add(Convertibles.toBigDecimal(value));
        return withValue(newValue);
    }

    default T sub(Object value) {
        val newValue = toBigDecimal().subtract(Convertibles.toBigDecimal(value));
        return withValue(newValue);
    }

    default T mul(Object value) {
        val newValue = toBigDecimal().multiply(Convertibles.toBigDecimal(value));
        return withValue(newValue);
    }

    default T div(Object value) {
        val newValue = toBigDecimal().divide(Convertibles.toBigDecimal(value), RoundingMode.FLOOR);
        return withValue(newValue);
    }

    default T pow(Object value) {
        val newValue = toBigDecimal().pow(Convertibles.toInteger(value));
        return withValue(newValue);
    }

    default T sqrt() {
        val decimal = toBigDecimal();
        val newValue = decimal.sqrt(new MathContext(Decimal.MINIMUM_DECIMALS, RoundingMode.FLOOR));
        return withValue(newValue);
    }

    default T negate() {
        val newValue = toBigDecimal().negate();
        return withValue(newValue);
    }

    default boolean moreThan(Object value) {
        return compareTo(value) > 0;
    }

    default boolean moreThanOrEquals(Object value) {
        return compareTo(value) >= 0;
    }

    default boolean lessThan(Object value) {
        return compareTo(value) < 0;
    }

    default boolean lessThanOrEquals(Object value) {
        return compareTo(value) <= 0;
    }

    default boolean equalTo(Object value) {
        return compareTo(value) == 0;
    }

    @Override
    default int compareTo(Object other) {
        return toBigDecimal().compareTo(Convertibles.toBigDecimal(other));
    }

    default byte toByte() {
        return Convertibles.toByte(getValue());
    }

    default short toShort() {
        return Convertibles.toShort(getValue());
    }

    default int toInteger() {
        return Convertibles.toInteger(getValue());
    }

    default long toLong() {
        return Convertibles.toLong(getValue());
    }

    default float toFloat() {
        return Convertibles.toFloat(getValue());
    }

    default double toDouble() {
        return Convertibles.toDouble(getValue());
    }

    default BigInteger toBigInteger() {
        return Convertibles.toBigInteger(getValue());
    }

    default BigDecimal toBigDecimal() {
        return Convertibles.toBigDecimal(getValue());
    }

    default Decimal toDecimal() {
        return Convertibles.toDecimal(getValue());
    }

    default String toString(Object scale) {
        return Convertibles.toString(getValue(), scale);
    }

    default Int toInt8() {
        return Convertibles.toInt8(getValue());
    }

    default Int toInt16() {
        return Convertibles.toInt16(getValue());
    }

    default Int toInt24() {
        return Convertibles.toInt24(getValue());
    }

    default Int toInt32() {
        return Convertibles.toInt32(getValue());
    }

    default Int toInt40() {
        return Convertibles.toInt40(getValue());
    }

    default Int toInt48() {
        return Convertibles.toInt48(getValue());
    }

    default Int toInt56() {
        return Convertibles.toInt56(getValue());
    }

    default Int toInt64() {
        return Convertibles.toInt64(getValue());
    }

    default Int toInt72() {
        return Convertibles.toInt72(getValue());
    }

    default Int toInt80() {
        return Convertibles.toInt80(getValue());
    }

    default Int toInt88() {
        return Convertibles.toInt88(getValue());
    }

    default Int toInt96() {
        return Convertibles.toInt96(getValue());
    }

    default Int toInt104() {
        return Convertibles.toInt104(getValue());
    }

    default Int toInt112() {
        return Convertibles.toInt112(getValue());
    }

    default Int toInt120() {
        return Convertibles.toInt120(getValue());
    }

    default Int toInt128() {
        return Convertibles.toInt128(getValue());
    }

    default Int toInt136() {
        return Convertibles.toInt136(getValue());
    }

    default Int toInt144() {
        return Convertibles.toInt144(getValue());
    }

    default Int toInt152() {
        return Convertibles.toInt152(getValue());
    }

    default Int toInt160() {
        return Convertibles.toInt160(getValue());
    }

    default Int toInt168() {
        return Convertibles.toInt168(getValue());
    }

    default Int toInt176() {
        return Convertibles.toInt176(getValue());
    }

    default Int toInt184() {
        return Convertibles.toInt184(getValue());
    }

    default Int toInt192() {
        return Convertibles.toInt192(getValue());
    }

    default Int toInt200() {
        return Convertibles.toInt200(getValue());
    }

    default Int toInt208() {
        return Convertibles.toInt208(getValue());
    }

    default Int toInt216() {
        return Convertibles.toInt216(getValue());
    }

    default Int toInt224() {
        return Convertibles.toInt224(getValue());
    }

    default Int toInt232() {
        return Convertibles.toInt232(getValue());
    }

    default Int toInt240() {
        return Convertibles.toInt240(getValue());
    }

    default Int toInt248() {
        return Convertibles.toInt248(getValue());
    }

    default Int toInt256() {
        return Convertibles.toInt256(getValue());
    }

    default Uint toUint8() {
        return Convertibles.toUint8(getValue());
    }

    default Uint toUint16() {
        return Convertibles.toUint16(getValue());
    }

    default Uint toUint24() {
        return Convertibles.toUint24(getValue());
    }

    default Uint toUint32() {
        return Convertibles.toUint32(getValue());
    }

    default Uint toUint40() {
        return Convertibles.toUint40(getValue());
    }

    default Uint toUint48() {
        return Convertibles.toUint48(getValue());
    }

    default Uint toUint56() {
        return Convertibles.toUint56(getValue());
    }

    default Uint toUint64() {
        return Convertibles.toUint64(getValue());
    }

    default Uint toUint72() {
        return Convertibles.toUint72(getValue());
    }

    default Uint toUint80() {
        return Convertibles.toUint80(getValue());
    }

    default Uint toUint88() {
        return Convertibles.toUint88(getValue());
    }

    default Uint toUint96() {
        return Convertibles.toUint96(getValue());
    }

    default Uint toUint104() {
        return Convertibles.toUint104(getValue());
    }

    default Uint toUint112() {
        return Convertibles.toUint112(getValue());
    }

    default Uint toUint120() {
        return Convertibles.toUint120(getValue());
    }

    default Uint toUint128() {
        return Convertibles.toUint128(getValue());
    }

    default Uint toUint136() {
        return Convertibles.toUint136(getValue());
    }

    default Uint toUint144() {
        return Convertibles.toUint144(getValue());
    }

    default Uint toUint152() {
        return Convertibles.toUint152(getValue());
    }

    default Uint toUint160() {
        return Convertibles.toUint160(getValue());
    }

    default Uint toUint168() {
        return Convertibles.toUint168(getValue());
    }

    default Uint toUint176() {
        return Convertibles.toUint176(getValue());
    }

    default Uint toUint184() {
        return Convertibles.toUint184(getValue());
    }

    default Uint toUint192() {
        return Convertibles.toUint192(getValue());
    }

    default Uint toUint200() {
        return Convertibles.toUint200(getValue());
    }

    default Uint toUint208() {
        return Convertibles.toUint208(getValue());
    }

    default Uint toUint216() {
        return Convertibles.toUint216(getValue());
    }

    default Uint toUint224() {
        return Convertibles.toUint224(getValue());
    }

    default Uint toUint232() {
        return Convertibles.toUint232(getValue());
    }

    default Uint toUint240() {
        return Convertibles.toUint240(getValue());
    }

    default Uint toUint248() {
        return Convertibles.toUint248(getValue());
    }

    default Uint toUint256() {
        return Convertibles.toUint256(getValue());
    }

    default boolean toBoolean() {
        return Convertibles.toBoolean(getValue());
    }

    default String toHex() {
        return Convertibles.toHex(getValue());
    }

    default Bytes toBytes() {
        return Convertibles.toBytes(getValue());
    }

    default Bytes toBytes1() {
        return Convertibles.toBytes1(getValue());
    }

    default Bytes toBytes2() {
        return Convertibles.toBytes2(getValue());
    }

    default Bytes toBytes3() {
        return Convertibles.toBytes3(getValue());
    }

    default Bytes toBytes4() {
        return Convertibles.toBytes4(getValue());
    }

    default Bytes toBytes5() {
        return Convertibles.toBytes5(getValue());
    }

    default Bytes toBytes6() {
        return Convertibles.toBytes6(getValue());
    }

    default Bytes toBytes7() {
        return Convertibles.toBytes7(getValue());
    }

    default Bytes toBytes8() {
        return Convertibles.toBytes8(getValue());
    }

    default Bytes toBytes9() {
        return Convertibles.toBytes9(getValue());
    }

    default Bytes toBytes10() {
        return Convertibles.toBytes10(getValue());
    }

    default Bytes toBytes11() {
        return Convertibles.toBytes11(getValue());
    }

    default Bytes toBytes12() {
        return Convertibles.toBytes12(getValue());
    }

    default Bytes toBytes13() {
        return Convertibles.toBytes13(getValue());
    }

    default Bytes toBytes14() {
        return Convertibles.toBytes14(getValue());
    }

    default Bytes toBytes15() {
        return Convertibles.toBytes15(getValue());
    }

    default Bytes toBytes16() {
        return Convertibles.toBytes16(getValue());
    }

    default Bytes toBytes17() {
        return Convertibles.toBytes17(getValue());
    }

    default Bytes toBytes18() {
        return Convertibles.toBytes18(getValue());
    }

    default Bytes toBytes19() {
        return Convertibles.toBytes19(getValue());
    }

    default Bytes toBytes20() {
        return Convertibles.toBytes20(getValue());
    }

    default Bytes toBytes21() {
        return Convertibles.toBytes21(getValue());
    }

    default Bytes toBytes22() {
        return Convertibles.toBytes22(getValue());
    }

    default Bytes toBytes23() {
        return Convertibles.toBytes23(getValue());
    }

    default Bytes toBytes24() {
        return Convertibles.toBytes24(getValue());
    }

    default Bytes toBytes25() {
        return Convertibles.toBytes25(getValue());
    }

    default Bytes toBytes26() {
        return Convertibles.toBytes26(getValue());
    }

    default Bytes toBytes27() {
        return Convertibles.toBytes27(getValue());
    }

    default Bytes toBytes28() {
        return Convertibles.toBytes28(getValue());
    }

    default Bytes toBytes29() {
        return Convertibles.toBytes29(getValue());
    }

    default Bytes toBytes30() {
        return Convertibles.toBytes30(getValue());
    }

    default Bytes toBytes31() {
        return Convertibles.toBytes31(getValue());
    }

    default Bytes toBytes32() {
        return Convertibles.toBytes32(getValue());
    }

    default byte[] toByteArray() {
        return Convertibles.toByteArray(getValue());
    }

}
