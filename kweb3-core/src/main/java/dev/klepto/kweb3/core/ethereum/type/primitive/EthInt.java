package dev.klepto.kweb3.core.ethereum.type.primitive;

import dev.klepto.kweb3.core.ethereum.type.EthNumericValue;
import dev.klepto.kweb3.core.ethereum.type.EthSizedValue;
import dev.klepto.kweb3.core.ethereum.type.EthValue;
import dev.klepto.kweb3.core.util.Hex;
import lombok.With;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.Objects;

import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * Container for ethereum <code>int</code> value.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public class EthInt extends Number implements EthValue, EthNumericValue<EthInt>, EthSizedValue {

    /**
     * Zero <code>ethereum int</code> constant.
     */
    public static final EthInt ZERO = int256(BigInteger.ZERO);

    /**
     * One <code>ethereum int</code> constant.
     */
    public static final EthInt ONE = int256(BigInteger.ONE);

    /**
     * Two <code>ethereum int</code> constant.
     */
    public static final EthInt TWO = int256(BigInteger.TWO);

    /**
     * Ten <code>ethereum int</code> constant.
     */
    public static final EthInt TEN = int256(BigInteger.TEN);

    private final int size;
    private final BigInteger value;

    /**
     * Creates a new instance of <code>ethereum int</code> with given size and value.
     *
     * @param size  the size in bits of this <code>ethereum int</code>, from 1 to 256
     * @param value the big integer value
     */
    public EthInt(int size, @NotNull BigInteger value) {
        require(size >= value.bitLength(), "int{} cannot fit value: {}", size, value);
        this.size = size;
        this.value = value;
    }

    /**
     * Returns {@link BigInteger} value that represents this <code>ethereum int</code>.
     *
     * @return the big integer value of this <code>ethereum int</code>
     */
    @Override
    public @NotNull BigInteger value() {
        return value;
    }

    /**
     * Returns the size in bits of this <code>ethereum int</code>
     *
     * @return the size in bits of this <code>ethereum int</code>
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Increments this <code>ethereum int</code> value by one.
     *
     * @return a new instance of <code>ethereum int</code> with the incremented value
     */
    @NotNull
    public EthInt inc() {
        return plus(1);
    }

    /**
     * Decrements this <code>ethereum int</code> value by one.
     *
     * @return a new instance of <code>ethereum int</code> with the decremented value
     */
    @NotNull
    public EthInt dec() {
        return minus(1);
    }

    /**
     * Converts this value to an <code>int</code>.
     *
     * @return the value of this as an <code>int</code>
     */
    @Override
    public int intValue() {
        return value.intValue();
    }

    /**
     * Converts this value to a <code>long</code>.
     *
     * @return the value of this as a <code>long</code>
     */
    @Override
    public long longValue() {
        return value.longValue();
    }

    /**
     * Converts this value to a <code>float</code>.
     *
     * @return the value of this as a <code>float</code>
     */
    @Override
    public float floatValue() {
        return toBigDecimal().floatValue();
    }

    /**
     * Converts this value to a <code>double</code>.
     *
     * @return the value of this as a <code>double</code>
     */
    @Override
    public double doubleValue() {
        return toBigDecimal().doubleValue();
    }

    /**
     * Performs modular exponentiation on this {@link EthInt} instance with the specified exponent,
     * using a modulus of <code>2^{@link EthInt#size}</code> This method is particularly useful for cryptographic
     * operations where calculations need to wrap around at the boundary of <code>2^{@link EthInt#size}</code> to
     * prevent integer overflow and ensure values remain within the {@link EthInt#size} bit limit.
     *
     * @param exponent The {@link EthUint} representing the exponent to which this value is raised.
     * @return A new {@link EthUint} object representing <code>this^exponent % 2^{@link EthInt#size}</code>.
     * @throws ArithmeticException If the <code>exponent</code> is negative and this EthInt is not relatively prime to
     *                             <code>2^{@link EthInt#size}</code>
     */
    public @NotNull EthInt pow(@NotNull EthUint exponent) {
        // BigInteger.ONE.shiftLeft(size) computes 2^size, providing the modulus for the operation.
        BigInteger modulus = BigInteger.ONE.shiftLeft(size);
        return withValue(value.modPow(exponent.value(), modulus));
    }


    /**
     * Returns string representation of this <code>ethereum int</code>.
     *
     * @return string representation of this <code>ethereum int</code>
     */
    @Override
    @NotNull
    public String toString() {
        return "int" + size + "(" + value + ")";
    }

    /**
     * Returns hash code of this <code>ethereum int</code>.
     *
     * @return hash code of this <code>ethereum int</code>
     */
    @Override
    public int hashCode() {
        return Objects.hash(size, value);
    }

    /**
     * Arithmetic equals method for <code>ethereum int</code> values.
     *
     * @param object the object to compare with
     * @return true if the objects have the same value; false otherwise
     */
    public boolean equals(@Nullable Object object) {
        if (object instanceof Number number) {
            return equals(number);
        }
        if (object instanceof EthNumericValue<?> numeric) {
            return equals(numeric.value());
        }
        return false;
    }

    /**
     * Compares this <code>ethereum int</code> to the specified object.
     *
     * @param object the object to compare with
     * @return true if the objects are the same; false otherwise
     */
    public boolean matches(@Nullable Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!(object instanceof EthInt other)) {
            return false;
        }
        return size == other.size && value.equals(other.value);
    }

    /* Solidity style static initializers */
    @NotNull
    public static EthInt int256(@NotNull Number value) {
        return new EthInt(256, EthNumericValue.parseBigInteger(value));
    }

    @NotNull
    public static EthInt int256(@NotNull String hex) {
        require(Hex.isValid(hex), "Malformed hex string: {}", hex);
        return int256(Hex.toBigInteger(hex));
    }

    /* Smaller size solidity int initializers */
    @NotNull
    public static EthInt int8(@NotNull Number value) {
        return int256(value).withSize(8);
    }

    @NotNull
    public static EthInt int10(@NotNull Number value) {
        return int256(value).withSize(10);
    }

    @NotNull
    public static EthInt int12(@NotNull Number value) {
        return int256(value).withSize(12);
    }

    @NotNull
    public static EthInt int14(@NotNull Number value) {
        return int256(value).withSize(14);
    }

    @NotNull
    public static EthInt int16(@NotNull Number value) {
        return int256(value).withSize(16);
    }

    @NotNull
    public static EthInt int18(@NotNull Number value) {
        return int256(value).withSize(18);
    }

    @NotNull
    public static EthInt int20(@NotNull Number value) {
        return int256(value).withSize(20);
    }

    @NotNull
    public static EthInt int22(@NotNull Number value) {
        return int256(value).withSize(22);
    }

    @NotNull
    public static EthInt int24(@NotNull Number value) {
        return int256(value).withSize(24);
    }

    @NotNull
    public static EthInt int26(@NotNull Number value) {
        return int256(value).withSize(26);
    }

    @NotNull
    public static EthInt int28(@NotNull Number value) {
        return int256(value).withSize(28);
    }

    @NotNull
    public static EthInt int30(@NotNull Number value) {
        return int256(value).withSize(30);
    }

    @NotNull
    public static EthInt int32(@NotNull Number value) {
        return int256(value).withSize(32);
    }

    @NotNull
    public static EthInt int34(@NotNull Number value) {
        return int256(value).withSize(34);
    }

    @NotNull
    public static EthInt int36(@NotNull Number value) {
        return int256(value).withSize(36);
    }

    @NotNull
    public static EthInt int38(@NotNull Number value) {
        return int256(value).withSize(38);
    }

    @NotNull
    public static EthInt int40(@NotNull Number value) {
        return int256(value).withSize(40);
    }

    @NotNull
    public static EthInt int42(@NotNull Number value) {
        return int256(value).withSize(42);
    }

    @NotNull
    public static EthInt int44(@NotNull Number value) {
        return int256(value).withSize(44);
    }

    @NotNull
    public static EthInt int46(@NotNull Number value) {
        return int256(value).withSize(46);
    }

    @NotNull
    public static EthInt int48(@NotNull Number value) {
        return int256(value).withSize(48);
    }

    @NotNull
    public static EthInt int50(@NotNull Number value) {
        return int256(value).withSize(50);
    }

    @NotNull
    public static EthInt int52(@NotNull Number value) {
        return int256(value).withSize(52);
    }

    @NotNull
    public static EthInt int54(@NotNull Number value) {
        return int256(value).withSize(54);
    }

    @NotNull
    public static EthInt int56(@NotNull Number value) {
        return int256(value).withSize(56);
    }

    @NotNull
    public static EthInt int58(@NotNull Number value) {
        return int256(value).withSize(58);
    }

    @NotNull
    public static EthInt int60(@NotNull Number value) {
        return int256(value).withSize(60);
    }

    @NotNull
    public static EthInt int62(@NotNull Number value) {
        return int256(value).withSize(62);
    }

    @NotNull
    public static EthInt int64(@NotNull Number value) {
        return int256(value).withSize(64);
    }

    @NotNull
    public static EthInt int66(@NotNull Number value) {
        return int256(value).withSize(66);
    }

    @NotNull
    public static EthInt int68(@NotNull Number value) {
        return int256(value).withSize(68);
    }

    @NotNull
    public static EthInt int70(@NotNull Number value) {
        return int256(value).withSize(70);
    }

    @NotNull
    public static EthInt int72(@NotNull Number value) {
        return int256(value).withSize(72);
    }

    @NotNull
    public static EthInt int74(@NotNull Number value) {
        return int256(value).withSize(74);
    }

    @NotNull
    public static EthInt int76(@NotNull Number value) {
        return int256(value).withSize(76);
    }

    @NotNull
    public static EthInt int78(@NotNull Number value) {
        return int256(value).withSize(78);
    }

    @NotNull
    public static EthInt int80(@NotNull Number value) {
        return int256(value).withSize(80);
    }

    @NotNull
    public static EthInt int82(@NotNull Number value) {
        return int256(value).withSize(82);
    }

    @NotNull
    public static EthInt int84(@NotNull Number value) {
        return int256(value).withSize(84);
    }

    @NotNull
    public static EthInt int86(@NotNull Number value) {
        return int256(value).withSize(86);
    }

    @NotNull
    public static EthInt int88(@NotNull Number value) {
        return int256(value).withSize(88);
    }

    @NotNull
    public static EthInt int90(@NotNull Number value) {
        return int256(value).withSize(90);
    }

    @NotNull
    public static EthInt int92(@NotNull Number value) {
        return int256(value).withSize(92);
    }

    @NotNull
    public static EthInt int94(@NotNull Number value) {
        return int256(value).withSize(94);
    }

    @NotNull
    public static EthInt int96(@NotNull Number value) {
        return int256(value).withSize(96);
    }

    @NotNull
    public static EthInt int98(@NotNull Number value) {
        return int256(value).withSize(98);
    }

    @NotNull
    public static EthInt int100(@NotNull Number value) {
        return int256(value).withSize(100);
    }

    @NotNull
    public static EthInt int102(@NotNull Number value) {
        return int256(value).withSize(102);
    }

    @NotNull
    public static EthInt int104(@NotNull Number value) {
        return int256(value).withSize(104);
    }

    @NotNull
    public static EthInt int106(@NotNull Number value) {
        return int256(value).withSize(106);
    }

    @NotNull
    public static EthInt int108(@NotNull Number value) {
        return int256(value).withSize(108);
    }

    @NotNull
    public static EthInt int110(@NotNull Number value) {
        return int256(value).withSize(110);
    }

    @NotNull
    public static EthInt int112(@NotNull Number value) {
        return int256(value).withSize(112);
    }

    @NotNull
    public static EthInt int114(@NotNull Number value) {
        return int256(value).withSize(114);
    }

    @NotNull
    public static EthInt int116(@NotNull Number value) {
        return int256(value).withSize(116);
    }

    @NotNull
    public static EthInt int118(@NotNull Number value) {
        return int256(value).withSize(118);
    }

    @NotNull
    public static EthInt int120(@NotNull Number value) {
        return int256(value).withSize(120);
    }

    @NotNull
    public static EthInt int122(@NotNull Number value) {
        return int256(value).withSize(122);
    }

    @NotNull
    public static EthInt int124(@NotNull Number value) {
        return int256(value).withSize(124);
    }

    @NotNull
    public static EthInt int126(@NotNull Number value) {
        return int256(value).withSize(126);
    }

    @NotNull
    public static EthInt int128(@NotNull Number value) {
        return int256(value).withSize(128);
    }

    @NotNull
    public static EthInt int130(@NotNull Number value) {
        return int256(value).withSize(130);
    }

    @NotNull
    public static EthInt int132(@NotNull Number value) {
        return int256(value).withSize(132);
    }

    @NotNull
    public static EthInt int134(@NotNull Number value) {
        return int256(value).withSize(134);
    }

    @NotNull
    public static EthInt int136(@NotNull Number value) {
        return int256(value).withSize(136);
    }

    @NotNull
    public static EthInt int138(@NotNull Number value) {
        return int256(value).withSize(138);
    }

    @NotNull
    public static EthInt int140(@NotNull Number value) {
        return int256(value).withSize(140);
    }

    @NotNull
    public static EthInt int142(@NotNull Number value) {
        return int256(value).withSize(142);
    }

    @NotNull
    public static EthInt int144(@NotNull Number value) {
        return int256(value).withSize(144);
    }

    @NotNull
    public static EthInt int146(@NotNull Number value) {
        return int256(value).withSize(146);
    }

    @NotNull
    public static EthInt int148(@NotNull Number value) {
        return int256(value).withSize(148);
    }

    @NotNull
    public static EthInt int150(@NotNull Number value) {
        return int256(value).withSize(150);
    }

    @NotNull
    public static EthInt int152(@NotNull Number value) {
        return int256(value).withSize(152);
    }

    @NotNull
    public static EthInt int154(@NotNull Number value) {
        return int256(value).withSize(154);
    }

    @NotNull
    public static EthInt int156(@NotNull Number value) {
        return int256(value).withSize(156);
    }

    @NotNull
    public static EthInt int158(@NotNull Number value) {
        return int256(value).withSize(158);
    }

    @NotNull
    public static EthInt int160(@NotNull Number value) {
        return int256(value).withSize(160);
    }

    @NotNull
    public static EthInt int162(@NotNull Number value) {
        return int256(value).withSize(162);
    }

    @NotNull
    public static EthInt int164(@NotNull Number value) {
        return int256(value).withSize(164);
    }

    @NotNull
    public static EthInt int166(@NotNull Number value) {
        return int256(value).withSize(166);
    }

    @NotNull
    public static EthInt int168(@NotNull Number value) {
        return int256(value).withSize(168);
    }

    @NotNull
    public static EthInt int170(@NotNull Number value) {
        return int256(value).withSize(170);
    }

    @NotNull
    public static EthInt int172(@NotNull Number value) {
        return int256(value).withSize(172);
    }

    @NotNull
    public static EthInt int174(@NotNull Number value) {
        return int256(value).withSize(174);
    }

    @NotNull
    public static EthInt int176(@NotNull Number value) {
        return int256(value).withSize(176);
    }

    @NotNull
    public static EthInt int178(@NotNull Number value) {
        return int256(value).withSize(178);
    }

    @NotNull
    public static EthInt int180(@NotNull Number value) {
        return int256(value).withSize(180);
    }

    @NotNull
    public static EthInt int182(@NotNull Number value) {
        return int256(value).withSize(182);
    }

    @NotNull
    public static EthInt int184(@NotNull Number value) {
        return int256(value).withSize(184);
    }

    @NotNull
    public static EthInt int186(@NotNull Number value) {
        return int256(value).withSize(186);
    }

    @NotNull
    public static EthInt int188(@NotNull Number value) {
        return int256(value).withSize(188);
    }

    @NotNull
    public static EthInt int190(@NotNull Number value) {
        return int256(value).withSize(190);
    }

    @NotNull
    public static EthInt int192(@NotNull Number value) {
        return int256(value).withSize(192);
    }

    @NotNull
    public static EthInt int194(@NotNull Number value) {
        return int256(value).withSize(194);
    }

    @NotNull
    public static EthInt int196(@NotNull Number value) {
        return int256(value).withSize(196);
    }

    @NotNull
    public static EthInt int198(@NotNull Number value) {
        return int256(value).withSize(198);
    }

    @NotNull
    public static EthInt int200(@NotNull Number value) {
        return int256(value).withSize(200);
    }

    @NotNull
    public static EthInt int202(@NotNull Number value) {
        return int256(value).withSize(202);
    }

    @NotNull
    public static EthInt int204(@NotNull Number value) {
        return int256(value).withSize(204);
    }

    @NotNull
    public static EthInt int206(@NotNull Number value) {
        return int256(value).withSize(206);
    }

    @NotNull
    public static EthInt int208(@NotNull Number value) {
        return int256(value).withSize(208);
    }

    @NotNull
    public static EthInt int210(@NotNull Number value) {
        return int256(value).withSize(210);
    }

    @NotNull
    public static EthInt int212(@NotNull Number value) {
        return int256(value).withSize(212);
    }

    @NotNull
    public static EthInt int214(@NotNull Number value) {
        return int256(value).withSize(214);
    }

    @NotNull
    public static EthInt int216(@NotNull Number value) {
        return int256(value).withSize(216);
    }

    @NotNull
    public static EthInt int218(@NotNull Number value) {
        return int256(value).withSize(218);
    }

    @NotNull
    public static EthInt int220(@NotNull Number value) {
        return int256(value).withSize(220);
    }

    @NotNull
    public static EthInt int222(@NotNull Number value) {
        return int256(value).withSize(222);
    }

    @NotNull
    public static EthInt int224(@NotNull Number value) {
        return int256(value).withSize(224);
    }

    @NotNull
    public static EthInt int226(@NotNull Number value) {
        return int256(value).withSize(226);
    }

    @NotNull
    public static EthInt int228(@NotNull Number value) {
        return int256(value).withSize(228);
    }

    @NotNull
    public static EthInt int230(@NotNull Number value) {
        return int256(value).withSize(230);
    }

    @NotNull
    public static EthInt int232(@NotNull Number value) {
        return int256(value).withSize(232);
    }

    @NotNull
    public static EthInt int234(@NotNull Number value) {
        return int256(value).withSize(234);
    }

    @NotNull
    public static EthInt int236(@NotNull Number value) {
        return int256(value).withSize(236);
    }

    @NotNull
    public static EthInt int238(@NotNull Number value) {
        return int256(value).withSize(238);
    }

    @NotNull
    public static EthInt int240(@NotNull Number value) {
        return int256(value).withSize(240);
    }

    @NotNull
    public static EthInt int242(@NotNull Number value) {
        return int256(value).withSize(242);
    }

    @NotNull
    public static EthInt int244(@NotNull Number value) {
        return int256(value).withSize(244);
    }

    @NotNull
    public static EthInt int246(@NotNull Number value) {
        return int256(value).withSize(246);
    }

    @NotNull
    public static EthInt int248(@NotNull Number value) {
        return int256(value).withSize(248);
    }

    @NotNull
    public static EthInt int250(@NotNull Number value) {
        return int256(value).withSize(250);
    }

    @NotNull
    public static EthInt int252(@NotNull Number value) {
        return int256(value).withSize(252);
    }

    @NotNull
    public static EthInt int254(@NotNull Number value) {
        return int256(value).withSize(254);
    }

    @NotNull
    public static EthInt int8(@NotNull String hex) {
        return int256(hex).withSize(8);
    }

    @NotNull
    public static EthInt int10(@NotNull String hex) {
        return int256(hex).withSize(10);
    }

    @NotNull
    public static EthInt int12(@NotNull String hex) {
        return int256(hex).withSize(12);
    }

    @NotNull
    public static EthInt int14(@NotNull String hex) {
        return int256(hex).withSize(14);
    }

    @NotNull
    public static EthInt int16(@NotNull String hex) {
        return int256(hex).withSize(16);
    }

    @NotNull
    public static EthInt int18(@NotNull String hex) {
        return int256(hex).withSize(18);
    }

    @NotNull
    public static EthInt int20(@NotNull String hex) {
        return int256(hex).withSize(20);
    }

    @NotNull
    public static EthInt int22(@NotNull String hex) {
        return int256(hex).withSize(22);
    }

    @NotNull
    public static EthInt int24(@NotNull String hex) {
        return int256(hex).withSize(24);
    }

    @NotNull
    public static EthInt int26(@NotNull String hex) {
        return int256(hex).withSize(26);
    }

    @NotNull
    public static EthInt int28(@NotNull String hex) {
        return int256(hex).withSize(28);
    }

    @NotNull
    public static EthInt int30(@NotNull String hex) {
        return int256(hex).withSize(30);
    }

    @NotNull
    public static EthInt int32(@NotNull String hex) {
        return int256(hex).withSize(32);
    }

    @NotNull
    public static EthInt int34(@NotNull String hex) {
        return int256(hex).withSize(34);
    }

    @NotNull
    public static EthInt int36(@NotNull String hex) {
        return int256(hex).withSize(36);
    }

    @NotNull
    public static EthInt int38(@NotNull String hex) {
        return int256(hex).withSize(38);
    }

    @NotNull
    public static EthInt int40(@NotNull String hex) {
        return int256(hex).withSize(40);
    }

    @NotNull
    public static EthInt int42(@NotNull String hex) {
        return int256(hex).withSize(42);
    }

    @NotNull
    public static EthInt int44(@NotNull String hex) {
        return int256(hex).withSize(44);
    }

    @NotNull
    public static EthInt int46(@NotNull String hex) {
        return int256(hex).withSize(46);
    }

    @NotNull
    public static EthInt int48(@NotNull String hex) {
        return int256(hex).withSize(48);
    }

    @NotNull
    public static EthInt int50(@NotNull String hex) {
        return int256(hex).withSize(50);
    }

    @NotNull
    public static EthInt int52(@NotNull String hex) {
        return int256(hex).withSize(52);
    }

    @NotNull
    public static EthInt int54(@NotNull String hex) {
        return int256(hex).withSize(54);
    }

    @NotNull
    public static EthInt int56(@NotNull String hex) {
        return int256(hex).withSize(56);
    }

    @NotNull
    public static EthInt int58(@NotNull String hex) {
        return int256(hex).withSize(58);
    }

    @NotNull
    public static EthInt int60(@NotNull String hex) {
        return int256(hex).withSize(60);
    }

    @NotNull
    public static EthInt int62(@NotNull String hex) {
        return int256(hex).withSize(62);
    }

    @NotNull
    public static EthInt int64(@NotNull String hex) {
        return int256(hex).withSize(64);
    }

    @NotNull
    public static EthInt int66(@NotNull String hex) {
        return int256(hex).withSize(66);
    }

    @NotNull
    public static EthInt int68(@NotNull String hex) {
        return int256(hex).withSize(68);
    }

    @NotNull
    public static EthInt int70(@NotNull String hex) {
        return int256(hex).withSize(70);
    }

    @NotNull
    public static EthInt int72(@NotNull String hex) {
        return int256(hex).withSize(72);
    }

    @NotNull
    public static EthInt int74(@NotNull String hex) {
        return int256(hex).withSize(74);
    }

    @NotNull
    public static EthInt int76(@NotNull String hex) {
        return int256(hex).withSize(76);
    }

    @NotNull
    public static EthInt int78(@NotNull String hex) {
        return int256(hex).withSize(78);
    }

    @NotNull
    public static EthInt int80(@NotNull String hex) {
        return int256(hex).withSize(80);
    }

    @NotNull
    public static EthInt int82(@NotNull String hex) {
        return int256(hex).withSize(82);
    }

    @NotNull
    public static EthInt int84(@NotNull String hex) {
        return int256(hex).withSize(84);
    }

    @NotNull
    public static EthInt int86(@NotNull String hex) {
        return int256(hex).withSize(86);
    }

    @NotNull
    public static EthInt int88(@NotNull String hex) {
        return int256(hex).withSize(88);
    }

    @NotNull
    public static EthInt int90(@NotNull String hex) {
        return int256(hex).withSize(90);
    }

    @NotNull
    public static EthInt int92(@NotNull String hex) {
        return int256(hex).withSize(92);
    }

    @NotNull
    public static EthInt int94(@NotNull String hex) {
        return int256(hex).withSize(94);
    }

    @NotNull
    public static EthInt int96(@NotNull String hex) {
        return int256(hex).withSize(96);
    }

    @NotNull
    public static EthInt int98(@NotNull String hex) {
        return int256(hex).withSize(98);
    }

    @NotNull
    public static EthInt int100(@NotNull String hex) {
        return int256(hex).withSize(100);
    }

    @NotNull
    public static EthInt int102(@NotNull String hex) {
        return int256(hex).withSize(102);
    }

    @NotNull
    public static EthInt int104(@NotNull String hex) {
        return int256(hex).withSize(104);
    }

    @NotNull
    public static EthInt int106(@NotNull String hex) {
        return int256(hex).withSize(106);
    }

    @NotNull
    public static EthInt int108(@NotNull String hex) {
        return int256(hex).withSize(108);
    }

    @NotNull
    public static EthInt int110(@NotNull String hex) {
        return int256(hex).withSize(110);
    }

    @NotNull
    public static EthInt int112(@NotNull String hex) {
        return int256(hex).withSize(112);
    }

    @NotNull
    public static EthInt int114(@NotNull String hex) {
        return int256(hex).withSize(114);
    }

    @NotNull
    public static EthInt int116(@NotNull String hex) {
        return int256(hex).withSize(116);
    }

    @NotNull
    public static EthInt int118(@NotNull String hex) {
        return int256(hex).withSize(118);
    }

    @NotNull
    public static EthInt int120(@NotNull String hex) {
        return int256(hex).withSize(120);
    }

    @NotNull
    public static EthInt int122(@NotNull String hex) {
        return int256(hex).withSize(122);
    }

    @NotNull
    public static EthInt int124(@NotNull String hex) {
        return int256(hex).withSize(124);
    }

    @NotNull
    public static EthInt int126(@NotNull String hex) {
        return int256(hex).withSize(126);
    }

    @NotNull
    public static EthInt int128(@NotNull String hex) {
        return int256(hex).withSize(128);
    }

    @NotNull
    public static EthInt int130(@NotNull String hex) {
        return int256(hex).withSize(130);
    }

    @NotNull
    public static EthInt int132(@NotNull String hex) {
        return int256(hex).withSize(132);
    }

    @NotNull
    public static EthInt int134(@NotNull String hex) {
        return int256(hex).withSize(134);
    }

    @NotNull
    public static EthInt int136(@NotNull String hex) {
        return int256(hex).withSize(136);
    }

    @NotNull
    public static EthInt int138(@NotNull String hex) {
        return int256(hex).withSize(138);
    }

    @NotNull
    public static EthInt int140(@NotNull String hex) {
        return int256(hex).withSize(140);
    }

    @NotNull
    public static EthInt int142(@NotNull String hex) {
        return int256(hex).withSize(142);
    }

    @NotNull
    public static EthInt int144(@NotNull String hex) {
        return int256(hex).withSize(144);
    }

    @NotNull
    public static EthInt int146(@NotNull String hex) {
        return int256(hex).withSize(146);
    }

    @NotNull
    public static EthInt int148(@NotNull String hex) {
        return int256(hex).withSize(148);
    }

    @NotNull
    public static EthInt int150(@NotNull String hex) {
        return int256(hex).withSize(150);
    }

    @NotNull
    public static EthInt int152(@NotNull String hex) {
        return int256(hex).withSize(152);
    }

    @NotNull
    public static EthInt int154(@NotNull String hex) {
        return int256(hex).withSize(154);
    }

    @NotNull
    public static EthInt int156(@NotNull String hex) {
        return int256(hex).withSize(156);
    }

    @NotNull
    public static EthInt int158(@NotNull String hex) {
        return int256(hex).withSize(158);
    }

    @NotNull
    public static EthInt int160(@NotNull String hex) {
        return int256(hex).withSize(160);
    }

    @NotNull
    public static EthInt int162(@NotNull String hex) {
        return int256(hex).withSize(162);
    }

    @NotNull
    public static EthInt int164(@NotNull String hex) {
        return int256(hex).withSize(164);
    }

    @NotNull
    public static EthInt int166(@NotNull String hex) {
        return int256(hex).withSize(166);
    }

    @NotNull
    public static EthInt int168(@NotNull String hex) {
        return int256(hex).withSize(168);
    }

    @NotNull
    public static EthInt int170(@NotNull String hex) {
        return int256(hex).withSize(170);
    }

    @NotNull
    public static EthInt int172(@NotNull String hex) {
        return int256(hex).withSize(172);
    }

    @NotNull
    public static EthInt int174(@NotNull String hex) {
        return int256(hex).withSize(174);
    }

    @NotNull
    public static EthInt int176(@NotNull String hex) {
        return int256(hex).withSize(176);
    }

    @NotNull
    public static EthInt int178(@NotNull String hex) {
        return int256(hex).withSize(178);
    }

    @NotNull
    public static EthInt int180(@NotNull String hex) {
        return int256(hex).withSize(180);
    }

    @NotNull
    public static EthInt int182(@NotNull String hex) {
        return int256(hex).withSize(182);
    }

    @NotNull
    public static EthInt int184(@NotNull String hex) {
        return int256(hex).withSize(184);
    }

    @NotNull
    public static EthInt int186(@NotNull String hex) {
        return int256(hex).withSize(186);
    }

    @NotNull
    public static EthInt int188(@NotNull String hex) {
        return int256(hex).withSize(188);
    }

    @NotNull
    public static EthInt int190(@NotNull String hex) {
        return int256(hex).withSize(190);
    }

    @NotNull
    public static EthInt int192(@NotNull String hex) {
        return int256(hex).withSize(192);
    }

    @NotNull
    public static EthInt int194(@NotNull String hex) {
        return int256(hex).withSize(194);
    }

    @NotNull
    public static EthInt int196(@NotNull String hex) {
        return int256(hex).withSize(196);
    }

    @NotNull
    public static EthInt int198(@NotNull String hex) {
        return int256(hex).withSize(198);
    }

    @NotNull
    public static EthInt int200(@NotNull String hex) {
        return int256(hex).withSize(200);
    }

    @NotNull
    public static EthInt int202(@NotNull String hex) {
        return int256(hex).withSize(202);
    }

    @NotNull
    public static EthInt int204(@NotNull String hex) {
        return int256(hex).withSize(204);
    }

    @NotNull
    public static EthInt int206(@NotNull String hex) {
        return int256(hex).withSize(206);
    }

    @NotNull
    public static EthInt int208(@NotNull String hex) {
        return int256(hex).withSize(208);
    }

    @NotNull
    public static EthInt int210(@NotNull String hex) {
        return int256(hex).withSize(210);
    }

    @NotNull
    public static EthInt int212(@NotNull String hex) {
        return int256(hex).withSize(212);
    }

    @NotNull
    public static EthInt int214(@NotNull String hex) {
        return int256(hex).withSize(214);
    }

    @NotNull
    public static EthInt int216(@NotNull String hex) {
        return int256(hex).withSize(216);
    }

    @NotNull
    public static EthInt int218(@NotNull String hex) {
        return int256(hex).withSize(218);
    }

    @NotNull
    public static EthInt int220(@NotNull String hex) {
        return int256(hex).withSize(220);
    }

    @NotNull
    public static EthInt int222(@NotNull String hex) {
        return int256(hex).withSize(222);
    }

    @NotNull
    public static EthInt int224(@NotNull String hex) {
        return int256(hex).withSize(224);
    }

    @NotNull
    public static EthInt int226(@NotNull String hex) {
        return int256(hex).withSize(226);
    }

    @NotNull
    public static EthInt int228(@NotNull String hex) {
        return int256(hex).withSize(228);
    }

    @NotNull
    public static EthInt int230(@NotNull String hex) {
        return int256(hex).withSize(230);
    }

    @NotNull
    public static EthInt int232(@NotNull String hex) {
        return int256(hex).withSize(232);
    }

    @NotNull
    public static EthInt int234(@NotNull String hex) {
        return int256(hex).withSize(234);
    }

    @NotNull
    public static EthInt int236(@NotNull String hex) {
        return int256(hex).withSize(236);
    }

    @NotNull
    public static EthInt int238(@NotNull String hex) {
        return int256(hex).withSize(238);
    }

    @NotNull
    public static EthInt int240(@NotNull String hex) {
        return int256(hex).withSize(240);
    }

    @NotNull
    public static EthInt int242(@NotNull String hex) {
        return int256(hex).withSize(242);
    }

    @NotNull
    public static EthInt int244(@NotNull String hex) {
        return int256(hex).withSize(244);
    }

    @NotNull
    public static EthInt int246(@NotNull String hex) {
        return int256(hex).withSize(246);
    }

    @NotNull
    public static EthInt int248(@NotNull String hex) {
        return int256(hex).withSize(248);
    }

    @NotNull
    public static EthInt int250(@NotNull String hex) {
        return int256(hex).withSize(250);
    }

    @NotNull
    public static EthInt int252(@NotNull String hex) {
        return int256(hex).withSize(252);
    }

    @NotNull
    public static EthInt int254(@NotNull String hex) {
        return int256(hex).withSize(254);
    }

}
