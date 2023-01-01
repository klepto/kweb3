package dev.klepto.kweb3.abi.type.util;

import com.google.common.primitives.Primitives;
import dev.klepto.kweb3.abi.type.Address;
import dev.klepto.kweb3.abi.type.Bytes;
import dev.klepto.kweb3.abi.type.Int;
import dev.klepto.kweb3.abi.type.Uint;
import lombok.val;

import java.math.BigDecimal;
import java.math.BigInteger;

import static dev.klepto.kweb3.Web3Error.error;
import static dev.klepto.kweb3.Web3Error.require;
import static dev.klepto.kweb3.abi.type.util.Types.address;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Convertibles {

    private Convertibles() {

    }

    /*
     * java types:
     * Byte
     * Short
     * Integer
     * Long
     * Float
     * Double
     * BigInteger
     * BigDecimal
     * Decimal
     *
     * solidity types:
     * Int
     * Uint
     * boolean - uint8 (1 or 0)
     * String - hex string
     * Bytes - hex string bytes
     * byte[] - hex string bytes
     */

    private static void checkBounds(BigDecimal value, Object min, Object max) {
        val floor = parseBigDecimal(min);
        val ceiling = parseBigDecimal(max);
        require(value.compareTo(floor) >= 0 && value.compareTo(ceiling) <= 0, "Value out of bounds.");
    }

    private static Class<?> unwrapType(Object value) {
        return Primitives.unwrap(value.getClass());
    }

    /**
     * Attempts to convert any given value to BigDecimal. This is our base type for all numeric operations.
     *
     * @param value the value
     * @return the parsed BigDecimal value
     */
    private static BigDecimal parseBigDecimal(Object value) {
        val type = unwrapType(value);
        if (type == byte.class) {
            return BigDecimal.valueOf((byte) value);
        } else if (type == short.class) {
            return BigDecimal.valueOf((short) value);
        } else if (type == int.class) {
            return BigDecimal.valueOf((int) value);
        } else if (type == long.class) {
            return BigDecimal.valueOf((long) value);
        }  else if (type == float.class) {
            return BigDecimal.valueOf((float) value);
        } else if (type == double.class) {
            return BigDecimal.valueOf((double) value);
        } else if (type == BigInteger.class) {
            return new BigDecimal((BigInteger) value);
        } else if (type == BigDecimal.class) {
            return (BigDecimal) value;
        } else if (type == Decimal.class) {
            return ((Decimal) value).getValue();
        } else if (type == Int.class) {
            return parseBigDecimal(((Int) value).getValue());
        } else if (value instanceof Uint uint) {
            return parseBigDecimal(uint.getValue());
        } else if (type == boolean.class) {
            return parseBigDecimal((boolean) value ? 1 : 0);
        } else if (type == String.class) {
            return parseBigDecimal(Hex.toBigInteger((String) value));
        } else if (type == Bytes.class) {
            return parseBigDecimal(((Bytes) value).getValue());
        } else if (type == byte[].class) {
            return parseBigDecimal(Hex.toHex((byte[]) value));
        }

        error("Couldn't parse {} to BigDecimal type.", value.getClass());
        return null;
    }

    public static <T> T to(Object value, Class<T> type) {
        val unwrapped = Primitives.unwrap(type);
        if (unwrapped == byte.class) return (T) (Byte) toByte(value);
        else if (unwrapped == short.class) return (T) (Short) toShort(value);
        else if (unwrapped == int.class) return (T) (Integer) toInteger(value);
        else if (unwrapped == long.class) return (T) (Long) toLong(value);
        else if (unwrapped == float.class) return (T) (Float) toFloat(value);
        else if (unwrapped == double.class) return (T) (Double) toDouble(value);
        else if (unwrapped == BigInteger.class) return (T) toBigInteger(value);
        else if (unwrapped == BigDecimal.class) return (T) toBigDecimal(value);
        else if (unwrapped == Decimal.class) return (T) toDecimal(value);
        else if (unwrapped == Int.class) return (T) toInt256(value);
        else if (unwrapped == Uint.class) return (T) toUint256(value);
        else if (unwrapped == boolean.class) return (T) (Boolean) toBoolean(value);
        else if (unwrapped == String.class) return (T) toHex(value);
        else if (unwrapped == Bytes.class) return (T) toBytes(value);
        else if (unwrapped == byte[].class) return (T) toByteArray(value);
        else if (unwrapped == Address.class) return (T) address(value);

        error("Couldn't convert {} to {}.", value.getClass(), type);
        return null;
    }

    public static byte toByte(Object value) {
        if (unwrapType(value) == byte.class) {
            return (byte) value;
        }

        val result = parseBigDecimal(value);
        checkBounds(result, Byte.MIN_VALUE, Byte.MAX_VALUE);
        return result.byteValue();
    }

    public static short toShort(Object value) {
        if (unwrapType(value) == short.class) {
            return (short) value;
        }

        val result = parseBigDecimal(value);
        checkBounds(result, Short.MIN_VALUE, Short.MAX_VALUE);
        return result.shortValue();
    }

    public static int toInteger(Object value) {
        if (unwrapType(value) == int.class) {
            return (int) value;
        }

        val result = parseBigDecimal(value);
        checkBounds(result, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return result.intValue();
    }

    public static long toLong(Object value) {
        if (unwrapType(value) == long.class) {
            return (long) value;
        }

        val result = parseBigDecimal(value);
        checkBounds(result, Long.MIN_VALUE, Long.MAX_VALUE);
        return result.longValue();
    }

    public static float toFloat(Object value) {
        if (unwrapType(value) == float.class) {
            return (float) value;
        }

        val result = parseBigDecimal(value);
        checkBounds(result, Float.MIN_VALUE, Float.MAX_VALUE);
        return result.floatValue();
    }

    public static double toDouble(Object value) {
        if (unwrapType(value) == double.class) {
            return (double) value;
        }

        val result = parseBigDecimal(value);
        checkBounds(result, Double.MIN_VALUE, Double.MAX_VALUE);
        return result.doubleValue();
    }

    public static BigInteger toBigInteger(Object value) {
        if (value instanceof BigInteger result) {
            return result;
        }
        return parseBigDecimal(value).toBigInteger();
    }

    public static BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal result) {
            return result;
        }
        return parseBigDecimal(value);
    }

    public static Decimal toDecimal(Object value) {
        if (value instanceof Decimal result) {
            return result;
        }
        val result = parseBigDecimal(value);
        return Decimal.create(result);
    }

    public static String toString(Object value, Object scale) {
        val bigIntegerValue = toBigInteger(value);
        val integerScale = toInteger(scale);
        return new BigDecimal(bigIntegerValue, integerScale).toPlainString();
    }

    public static Int toInt8(Object value) {
        return toInt256(value).withSize(8);
    }

    public static Int toInt16(Object value) {
        return toInt256(value).withSize(16);
    }

    public static Int toInt24(Object value) {
        return toInt256(value).withSize(24);
    }

    public static Int toInt32(Object value) {
        return toInt256(value).withSize(32);
    }

    public static Int toInt40(Object value) {
        return toInt256(value).withSize(40);
    }

    public static Int toInt48(Object value) {
        return toInt256(value).withSize(48);
    }

    public static Int toInt56(Object value) {
        return toInt256(value).withSize(56);
    }

    public static Int toInt64(Object value) {
        return toInt256(value).withSize(64);
    }

    public static Int toInt72(Object value) {
        return toInt256(value).withSize(72);
    }

    public static Int toInt80(Object value) {
        return toInt256(value).withSize(80);
    }

    public static Int toInt88(Object value) {
        return toInt256(value).withSize(88);
    }

    public static Int toInt96(Object value) {
        return toInt256(value).withSize(96);
    }

    public static Int toInt104(Object value) {
        return toInt256(value).withSize(104);
    }

    public static Int toInt112(Object value) {
        return toInt256(value).withSize(112);
    }

    public static Int toInt120(Object value) {
        return toInt256(value).withSize(120);
    }

    public static Int toInt128(Object value) {
        return toInt256(value).withSize(128);
    }

    public static Int toInt136(Object value) {
        return toInt256(value).withSize(136);
    }

    public static Int toInt144(Object value) {
        return toInt256(value).withSize(144);
    }

    public static Int toInt152(Object value) {
        return toInt256(value).withSize(152);
    }

    public static Int toInt160(Object value) {
        return toInt256(value).withSize(160);
    }

    public static Int toInt168(Object value) {
        return toInt256(value).withSize(168);
    }

    public static Int toInt176(Object value) {
        return toInt256(value).withSize(176);
    }

    public static Int toInt184(Object value) {
        return toInt256(value).withSize(184);
    }

    public static Int toInt192(Object value) {
        return toInt256(value).withSize(192);
    }

    public static Int toInt200(Object value) {
        return toInt256(value).withSize(200);
    }

    public static Int toInt208(Object value) {
        return toInt256(value).withSize(208);
    }

    public static Int toInt216(Object value) {
        return toInt256(value).withSize(216);
    }

    public static Int toInt224(Object value) {
        return toInt256(value).withSize(224);
    }

    public static Int toInt232(Object value) {
        return toInt256(value).withSize(232);
    }

    public static Int toInt240(Object value) {
        return toInt256(value).withSize(240);
    }

    public static Int toInt248(Object value) {
        return toInt256(value).withSize(248);
    }

    public static Int toInt256(Object value) {
        if (value instanceof Int result) {
            return result;
        }
        return new Int(value);
    }

    public static Uint toUint8(Object value) {
        return toUint256(value).withSize(8);
    }

    public static Uint toUint16(Object value) {
        return toUint256(value).withSize(16);
    }

    public static Uint toUint24(Object value) {
        return toUint256(value).withSize(24);
    }

    public static Uint toUint32(Object value) {
        return toUint256(value).withSize(32);
    }

    public static Uint toUint40(Object value) {
        return toUint256(value).withSize(40);
    }

    public static Uint toUint48(Object value) {
        return toUint256(value).withSize(48);
    }

    public static Uint toUint56(Object value) {
        return toUint256(value).withSize(56);
    }

    public static Uint toUint64(Object value) {
        return toUint256(value).withSize(64);
    }

    public static Uint toUint72(Object value) {
        return toUint256(value).withSize(72);
    }

    public static Uint toUint80(Object value) {
        return toUint256(value).withSize(80);
    }

    public static Uint toUint88(Object value) {
        return toUint256(value).withSize(88);
    }

    public static Uint toUint96(Object value) {
        return toUint256(value).withSize(96);
    }

    public static Uint toUint104(Object value) {
        return toUint256(value).withSize(104);
    }

    public static Uint toUint112(Object value) {
        return toUint256(value).withSize(112);
    }

    public static Uint toUint120(Object value) {
        return toUint256(value).withSize(120);
    }

    public static Uint toUint128(Object value) {
        return toUint256(value).withSize(128);
    }

    public static Uint toUint136(Object value) {
        return toUint256(value).withSize(136);
    }

    public static Uint toUint144(Object value) {
        return toUint256(value).withSize(144);
    }

    public static Uint toUint152(Object value) {
        return toUint256(value).withSize(152);
    }

    public static Uint toUint160(Object value) {
        return toUint256(value).withSize(160);
    }

    public static Uint toUint168(Object value) {
        return toUint256(value).withSize(168);
    }

    public static Uint toUint176(Object value) {
        return toUint256(value).withSize(176);
    }

    public static Uint toUint184(Object value) {
        return toUint256(value).withSize(184);
    }

    public static Uint toUint192(Object value) {
        return toUint256(value).withSize(192);
    }

    public static Uint toUint200(Object value) {
        return toUint256(value).withSize(200);
    }

    public static Uint toUint208(Object value) {
        return toUint256(value).withSize(208);
    }

    public static Uint toUint216(Object value) {
        return toUint256(value).withSize(216);
    }

    public static Uint toUint224(Object value) {
        return toUint256(value).withSize(224);
    }

    public static Uint toUint232(Object value) {
        return toUint256(value).withSize(232);
    }

    public static Uint toUint240(Object value) {
        return toUint256(value).withSize(240);
    }

    public static Uint toUint248(Object value) {
        return toUint256(value).withSize(248);
    }

    public static Uint toUint256(Object value) {
        if (value instanceof Uint result) {
            return result.withSize(Uint.MAX_SIZE);
        }

        return new Uint(value);
    }

    public static boolean toBoolean(Object value) {
        if (unwrapType(value) == boolean.class) {
            return (boolean) value;
        }
        val result = parseBigDecimal(value);
        checkBounds(result, 0, 1);
        return result.intValue() == 1;
    }

    public static String toHex(Object value) {
        if (value instanceof String result && Hex.isHex(result)) {
            return result;
        } else if (value instanceof Bytes result) {
            return Hex.toHex(result.getValue());
        } else if (value instanceof byte[] result) {
            return Hex.toHex(result);
        }

        return Hex.toHex(toBigInteger(value));
    }

    public static Bytes toBytes(Object value) {
        if (value instanceof Bytes result) {
            return result;
        }
        return new Bytes(toByteArray(value));
    }

    public static Bytes toBytes1(Object value) {
        return toBytes(value).withSize(1);
    }

    public static Bytes toBytes2(Object value) {
        return toBytes(value).withSize(2);
    }

    public static Bytes toBytes3(Object value) {
        return toBytes(value).withSize(3);
    }

    public static Bytes toBytes4(Object value) {
        return toBytes(value).withSize(4);
    }

    public static Bytes toBytes5(Object value) {
        return toBytes(value).withSize(5);
    }

    public static Bytes toBytes6(Object value) {
        return toBytes(value).withSize(6);
    }

    public static Bytes toBytes7(Object value) {
        return toBytes(value).withSize(7);
    }

    public static Bytes toBytes8(Object value) {
        return toBytes(value).withSize(8);
    }

    public static Bytes toBytes9(Object value) {
        return toBytes(value).withSize(9);
    }

    public static Bytes toBytes10(Object value) {
        return toBytes(value).withSize(10);
    }

    public static Bytes toBytes11(Object value) {
        return toBytes(value).withSize(11);
    }

    public static Bytes toBytes12(Object value) {
        return toBytes(value).withSize(12);
    }

    public static Bytes toBytes13(Object value) {
        return toBytes(value).withSize(13);
    }

    public static Bytes toBytes14(Object value) {
        return toBytes(value).withSize(14);
    }

    public static Bytes toBytes15(Object value) {
        return toBytes(value).withSize(15);
    }

    public static Bytes toBytes16(Object value) {
        return toBytes(value).withSize(16);
    }

    public static Bytes toBytes17(Object value) {
        return toBytes(value).withSize(17);
    }

    public static Bytes toBytes18(Object value) {
        return toBytes(value).withSize(18);
    }

    public static Bytes toBytes19(Object value) {
        return toBytes(value).withSize(19);
    }

    public static Bytes toBytes20(Object value) {
        return toBytes(value).withSize(20);
    }

    public static Bytes toBytes21(Object value) {
        return toBytes(value).withSize(21);
    }

    public static Bytes toBytes22(Object value) {
        return toBytes(value).withSize(22);
    }

    public static Bytes toBytes23(Object value) {
        return toBytes(value).withSize(23);
    }

    public static Bytes toBytes24(Object value) {
        return toBytes(value).withSize(24);
    }

    public static Bytes toBytes25(Object value) {
        return toBytes(value).withSize(25);
    }

    public static Bytes toBytes26(Object value) {
        return toBytes(value).withSize(26);
    }

    public static Bytes toBytes27(Object value) {
        return toBytes(value).withSize(27);
    }

    public static Bytes toBytes28(Object value) {
        return toBytes(value).withSize(28);
    }

    public static Bytes toBytes29(Object value) {
        return toBytes(value).withSize(29);
    }

    public static Bytes toBytes30(Object value) {
        return toBytes(value).withSize(30);
    }

    public static Bytes toBytes31(Object value) {
        return toBytes(value).withSize(31);
    }

    public static Bytes toBytes32(Object value) {
        return toBytes(value).withSize(32);
    }
    
    public static byte[] toByteArray(Object value) {
        if (value instanceof byte[] result) {
            return result;
        }
        return Hex.toByteArray(toHex(value));
    }

}