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
 * Container for <code>ethereum uint</code> value.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public class EthUint extends Number implements EthValue, EthNumericValue<EthUint>, EthSizedValue {

    /**
     * Zero <code>uint</code> constant.
     */
    public static final EthUint ZERO = uint256(BigInteger.ZERO);

    /**
     * One <code>uint</code> constant.
     */
    public static final EthUint ONE = uint256(BigInteger.ONE);

    /**
     * Two <code>uint</code> constant.
     */
    public static final EthUint TWO = uint256(BigInteger.TWO);

    /**
     * Ten <code>uint</code> constant.
     */
    public static final EthUint TEN = uint256(BigInteger.TEN);

    private final int size;

    @NotNull
    private final BigInteger value;

    /**
     * Creates a new instance of <code>ethereum uint</code> with given size and value.
     *
     * @param size  the size in bits of this <code>ethereum uint</code>, from 1 to 256
     * @param value the positive big integer value
     */
    public EthUint(int size, @NotNull BigInteger value) {
        require(size >= value.bitLength(), "uint{} cannot fit value: {}", size, value);
        require(value.signum() >= 0, "uint{} cannot be negative: {}", size, value);
        this.size = size;
        this.value = value;
    }

    /**
     * Returns {@link BigInteger} value that represents this <code>ethereum uint</code>.
     *
     * @return the big integer value of this <code>ethereum uint</code>
     */
    @Override
    public @NotNull BigInteger value() {
        return value;
    }

    /**
     * Returns the size in bits of this <code>ethereum uint</code>.
     *
     * @return the size in bits of this <code>ethereum uint</code>
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Increments this <code>ethereum uint</code> value by one.
     *
     * @return a new instance of <code>ethereum uint</code> with the incremented value
     */
    public EthUint inc() {
        return plus(1);
    }

    /**
     * Decrements this <code>ethereum uint</code> value by one.
     *
     * @return a new instance of <code>ethereum uint</code> with the decremented value
     */
    public EthUint dec() {
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
     * Performs modular exponentiation on this {@link EthUint} instance with the specified exponent,
     * using a modulus of <code>2^{@link EthUint#size}</code> This method is particularly useful for cryptographic operations
     * where calculations need to wrap around at the boundary of <code>2^{@link EthUint#size}</code> to prevent integer overflow
     * and ensure values remain within the {@link EthUint#size} bit limit.
     *
     * @param exponent The {@link EthUint} representing the exponent to which this value is raised.
     * @return A new {@link EthUint} object representing <code>this^exponent % 2^{@link EthUint#size}</code>.
     */
    public @NotNull EthUint pow(@NotNull EthUint exponent) {
        // BigInteger.ONE.shiftLeft(size) computes 2^size, providing the modulus for the operation.
        BigInteger modulus = BigInteger.ONE.shiftLeft(size);
        return withValue(value.modPow(exponent.value(), modulus));
    }

    /**
     * Returns string representation of this <code>ethereum uint</code>.
     *
     * @return string representation of this <code>ethereum uint</code>
     */
    @Override
    public String toString() {
        return "uint" + size + "(" + value + ")";
    }

    /**
     * Returns hash code of this <code>ethereum uint</code>.
     *
     * @return hash code of this <code>ethereum uint</code>
     */
    @Override
    public int hashCode() {
        return Objects.hash(size, value);
    }

    /**
     * Arithmetic equals method for <code>ethereum uint</code> values.
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
     * Compares this <code>ethereum uint</code> to the specified object.
     *
     * @param object the object to compare with
     * @return true if the objects are the same; false otherwise
     */
    public boolean matches(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!(object instanceof EthUint other)) {
            return false;
        }
        return size == other.size && value.equals(other.value);
    }

    /* Solidity style static initializers */
    @NotNull
    public static EthUint uint256(@NotNull Number value) {
        return new EthUint(256, EthNumericValue.parseBigInteger(value));
    }

    @NotNull
    public static EthUint uint256(@NotNull String hex) {
        return uint256(Hex.toUnsignedBigInteger(hex));
    }

    @NotNull
    public static EthUint uint8(@NotNull Number value) {
        return uint256(value).withSize(8);
    }

    @NotNull
    public static EthUint uint10(@NotNull Number value) {
        return uint256(value).withSize(10);
    }

    @NotNull
    public static EthUint uint12(@NotNull Number value) {
        return uint256(value).withSize(12);
    }

    @NotNull
    public static EthUint uint14(@NotNull Number value) {
        return uint256(value).withSize(14);
    }

    @NotNull
    public static EthUint uint16(@NotNull Number value) {
        return uint256(value).withSize(16);
    }

    @NotNull
    public static EthUint uint18(@NotNull Number value) {
        return uint256(value).withSize(18);
    }

    @NotNull
    public static EthUint uint20(@NotNull Number value) {
        return uint256(value).withSize(20);
    }

    @NotNull
    public static EthUint uint22(@NotNull Number value) {
        return uint256(value).withSize(22);
    }

    @NotNull
    public static EthUint uint24(@NotNull Number value) {
        return uint256(value).withSize(24);
    }

    @NotNull
    public static EthUint uint26(@NotNull Number value) {
        return uint256(value).withSize(26);
    }

    @NotNull
    public static EthUint uint28(@NotNull Number value) {
        return uint256(value).withSize(28);
    }

    @NotNull
    public static EthUint uint30(@NotNull Number value) {
        return uint256(value).withSize(30);
    }

    @NotNull
    public static EthUint uint32(@NotNull Number value) {
        return uint256(value).withSize(32);
    }

    @NotNull
    public static EthUint uint34(@NotNull Number value) {
        return uint256(value).withSize(34);
    }

    @NotNull
    public static EthUint uint36(@NotNull Number value) {
        return uint256(value).withSize(36);
    }

    @NotNull
    public static EthUint uint38(@NotNull Number value) {
        return uint256(value).withSize(38);
    }

    @NotNull
    public static EthUint uint40(@NotNull Number value) {
        return uint256(value).withSize(40);
    }

    @NotNull
    public static EthUint uint42(@NotNull Number value) {
        return uint256(value).withSize(42);
    }

    @NotNull
    public static EthUint uint44(@NotNull Number value) {
        return uint256(value).withSize(44);
    }

    @NotNull
    public static EthUint uint46(@NotNull Number value) {
        return uint256(value).withSize(46);
    }

    @NotNull
    public static EthUint uint48(@NotNull Number value) {
        return uint256(value).withSize(48);
    }

    @NotNull
    public static EthUint uint50(@NotNull Number value) {
        return uint256(value).withSize(50);
    }

    @NotNull
    public static EthUint uint52(@NotNull Number value) {
        return uint256(value).withSize(52);
    }

    @NotNull
    public static EthUint uint54(@NotNull Number value) {
        return uint256(value).withSize(54);
    }

    @NotNull
    public static EthUint uint56(@NotNull Number value) {
        return uint256(value).withSize(56);
    }

    @NotNull
    public static EthUint uint58(@NotNull Number value) {
        return uint256(value).withSize(58);
    }

    @NotNull
    public static EthUint uint60(@NotNull Number value) {
        return uint256(value).withSize(60);
    }

    @NotNull
    public static EthUint uint62(@NotNull Number value) {
        return uint256(value).withSize(62);
    }

    @NotNull
    public static EthUint uint64(@NotNull Number value) {
        return uint256(value).withSize(64);
    }

    @NotNull
    public static EthUint uint66(@NotNull Number value) {
        return uint256(value).withSize(66);
    }

    @NotNull
    public static EthUint uint68(@NotNull Number value) {
        return uint256(value).withSize(68);
    }

    @NotNull
    public static EthUint uint70(@NotNull Number value) {
        return uint256(value).withSize(70);
    }

    @NotNull
    public static EthUint uint72(@NotNull Number value) {
        return uint256(value).withSize(72);
    }

    @NotNull
    public static EthUint uint74(@NotNull Number value) {
        return uint256(value).withSize(74);
    }

    @NotNull
    public static EthUint uint76(@NotNull Number value) {
        return uint256(value).withSize(76);
    }

    @NotNull
    public static EthUint uint78(@NotNull Number value) {
        return uint256(value).withSize(78);
    }

    @NotNull
    public static EthUint uint80(@NotNull Number value) {
        return uint256(value).withSize(80);
    }

    @NotNull
    public static EthUint uint82(@NotNull Number value) {
        return uint256(value).withSize(82);
    }

    @NotNull
    public static EthUint uint84(@NotNull Number value) {
        return uint256(value).withSize(84);
    }

    @NotNull
    public static EthUint uint86(@NotNull Number value) {
        return uint256(value).withSize(86);
    }

    @NotNull
    public static EthUint uint88(@NotNull Number value) {
        return uint256(value).withSize(88);
    }

    @NotNull
    public static EthUint uint90(@NotNull Number value) {
        return uint256(value).withSize(90);
    }

    @NotNull
    public static EthUint uint92(@NotNull Number value) {
        return uint256(value).withSize(92);
    }

    @NotNull
    public static EthUint uint94(@NotNull Number value) {
        return uint256(value).withSize(94);
    }

    @NotNull
    public static EthUint uint96(@NotNull Number value) {
        return uint256(value).withSize(96);
    }

    @NotNull
    public static EthUint uint98(@NotNull Number value) {
        return uint256(value).withSize(98);
    }

    @NotNull
    public static EthUint uint100(@NotNull Number value) {
        return uint256(value).withSize(100);
    }

    @NotNull
    public static EthUint uint102(@NotNull Number value) {
        return uint256(value).withSize(102);
    }

    @NotNull
    public static EthUint uint104(@NotNull Number value) {
        return uint256(value).withSize(104);
    }

    @NotNull
    public static EthUint uint106(@NotNull Number value) {
        return uint256(value).withSize(106);
    }

    @NotNull
    public static EthUint uint108(@NotNull Number value) {
        return uint256(value).withSize(108);
    }

    @NotNull
    public static EthUint uint110(@NotNull Number value) {
        return uint256(value).withSize(110);
    }

    @NotNull
    public static EthUint uint112(@NotNull Number value) {
        return uint256(value).withSize(112);
    }

    @NotNull
    public static EthUint uint114(@NotNull Number value) {
        return uint256(value).withSize(114);
    }

    @NotNull
    public static EthUint uint116(@NotNull Number value) {
        return uint256(value).withSize(116);
    }

    @NotNull
    public static EthUint uint118(@NotNull Number value) {
        return uint256(value).withSize(118);
    }

    @NotNull
    public static EthUint uint120(@NotNull Number value) {
        return uint256(value).withSize(120);
    }

    @NotNull
    public static EthUint uint122(@NotNull Number value) {
        return uint256(value).withSize(122);
    }

    @NotNull
    public static EthUint uint124(@NotNull Number value) {
        return uint256(value).withSize(124);
    }

    @NotNull
    public static EthUint uint126(@NotNull Number value) {
        return uint256(value).withSize(126);
    }

    @NotNull
    public static EthUint uint128(@NotNull Number value) {
        return uint256(value).withSize(128);
    }

    @NotNull
    public static EthUint uint130(@NotNull Number value) {
        return uint256(value).withSize(130);
    }

    @NotNull
    public static EthUint uint132(@NotNull Number value) {
        return uint256(value).withSize(132);
    }

    @NotNull
    public static EthUint uint134(@NotNull Number value) {
        return uint256(value).withSize(134);
    }

    @NotNull
    public static EthUint uint136(@NotNull Number value) {
        return uint256(value).withSize(136);
    }

    @NotNull
    public static EthUint uint138(@NotNull Number value) {
        return uint256(value).withSize(138);
    }

    @NotNull
    public static EthUint uint140(@NotNull Number value) {
        return uint256(value).withSize(140);
    }

    @NotNull
    public static EthUint uint142(@NotNull Number value) {
        return uint256(value).withSize(142);
    }

    @NotNull
    public static EthUint uint144(@NotNull Number value) {
        return uint256(value).withSize(144);
    }

    @NotNull
    public static EthUint uint146(@NotNull Number value) {
        return uint256(value).withSize(146);
    }

    @NotNull
    public static EthUint uint148(@NotNull Number value) {
        return uint256(value).withSize(148);
    }

    @NotNull
    public static EthUint uint150(@NotNull Number value) {
        return uint256(value).withSize(150);
    }

    @NotNull
    public static EthUint uint152(@NotNull Number value) {
        return uint256(value).withSize(152);
    }

    @NotNull
    public static EthUint uint154(@NotNull Number value) {
        return uint256(value).withSize(154);
    }

    @NotNull
    public static EthUint uint156(@NotNull Number value) {
        return uint256(value).withSize(156);
    }

    @NotNull
    public static EthUint uint158(@NotNull Number value) {
        return uint256(value).withSize(158);
    }

    @NotNull
    public static EthUint uint160(@NotNull Number value) {
        return uint256(value).withSize(160);
    }

    @NotNull
    public static EthUint uint162(@NotNull Number value) {
        return uint256(value).withSize(162);
    }

    @NotNull
    public static EthUint uint164(@NotNull Number value) {
        return uint256(value).withSize(164);
    }

    @NotNull
    public static EthUint uint166(@NotNull Number value) {
        return uint256(value).withSize(166);
    }

    @NotNull
    public static EthUint uint168(@NotNull Number value) {
        return uint256(value).withSize(168);
    }

    @NotNull
    public static EthUint uint170(@NotNull Number value) {
        return uint256(value).withSize(170);
    }

    @NotNull
    public static EthUint uint172(@NotNull Number value) {
        return uint256(value).withSize(172);
    }

    @NotNull
    public static EthUint uint174(@NotNull Number value) {
        return uint256(value).withSize(174);
    }

    @NotNull
    public static EthUint uint176(@NotNull Number value) {
        return uint256(value).withSize(176);
    }

    @NotNull
    public static EthUint uint178(@NotNull Number value) {
        return uint256(value).withSize(178);
    }

    @NotNull
    public static EthUint uint180(@NotNull Number value) {
        return uint256(value).withSize(180);
    }

    @NotNull
    public static EthUint uint182(@NotNull Number value) {
        return uint256(value).withSize(182);
    }

    @NotNull
    public static EthUint uint184(@NotNull Number value) {
        return uint256(value).withSize(184);
    }

    @NotNull
    public static EthUint uint186(@NotNull Number value) {
        return uint256(value).withSize(186);
    }

    @NotNull
    public static EthUint uint188(@NotNull Number value) {
        return uint256(value).withSize(188);
    }

    @NotNull
    public static EthUint uint190(@NotNull Number value) {
        return uint256(value).withSize(190);
    }

    @NotNull
    public static EthUint uint192(@NotNull Number value) {
        return uint256(value).withSize(192);
    }

    @NotNull
    public static EthUint uint194(@NotNull Number value) {
        return uint256(value).withSize(194);
    }

    @NotNull
    public static EthUint uint196(@NotNull Number value) {
        return uint256(value).withSize(196);
    }

    @NotNull
    public static EthUint uint198(@NotNull Number value) {
        return uint256(value).withSize(198);
    }

    @NotNull
    public static EthUint uint200(@NotNull Number value) {
        return uint256(value).withSize(200);
    }

    @NotNull
    public static EthUint uint202(@NotNull Number value) {
        return uint256(value).withSize(202);
    }

    @NotNull
    public static EthUint uint204(@NotNull Number value) {
        return uint256(value).withSize(204);
    }

    @NotNull
    public static EthUint uint206(@NotNull Number value) {
        return uint256(value).withSize(206);
    }

    @NotNull
    public static EthUint uint208(@NotNull Number value) {
        return uint256(value).withSize(208);
    }

    @NotNull
    public static EthUint uint210(@NotNull Number value) {
        return uint256(value).withSize(210);
    }

    @NotNull
    public static EthUint uint212(@NotNull Number value) {
        return uint256(value).withSize(212);
    }

    @NotNull
    public static EthUint uint214(@NotNull Number value) {
        return uint256(value).withSize(214);
    }

    @NotNull
    public static EthUint uint216(@NotNull Number value) {
        return uint256(value).withSize(216);
    }

    @NotNull
    public static EthUint uint218(@NotNull Number value) {
        return uint256(value).withSize(218);
    }

    @NotNull
    public static EthUint uint220(@NotNull Number value) {
        return uint256(value).withSize(220);
    }

    @NotNull
    public static EthUint uint222(@NotNull Number value) {
        return uint256(value).withSize(222);
    }

    @NotNull
    public static EthUint uint224(@NotNull Number value) {
        return uint256(value).withSize(224);
    }

    @NotNull
    public static EthUint uint226(@NotNull Number value) {
        return uint256(value).withSize(226);
    }

    @NotNull
    public static EthUint uint228(@NotNull Number value) {
        return uint256(value).withSize(228);
    }

    @NotNull
    public static EthUint uint230(@NotNull Number value) {
        return uint256(value).withSize(230);
    }

    @NotNull
    public static EthUint uint232(@NotNull Number value) {
        return uint256(value).withSize(232);
    }

    @NotNull
    public static EthUint uint234(@NotNull Number value) {
        return uint256(value).withSize(234);
    }

    @NotNull
    public static EthUint uint236(@NotNull Number value) {
        return uint256(value).withSize(236);
    }

    @NotNull
    public static EthUint uint238(@NotNull Number value) {
        return uint256(value).withSize(238);
    }

    @NotNull
    public static EthUint uint240(@NotNull Number value) {
        return uint256(value).withSize(240);
    }

    @NotNull
    public static EthUint uint242(@NotNull Number value) {
        return uint256(value).withSize(242);
    }

    @NotNull
    public static EthUint uint244(@NotNull Number value) {
        return uint256(value).withSize(244);
    }

    @NotNull
    public static EthUint uint246(@NotNull Number value) {
        return uint256(value).withSize(246);
    }

    @NotNull
    public static EthUint uint248(@NotNull Number value) {
        return uint256(value).withSize(248);
    }

    @NotNull
    public static EthUint uint250(@NotNull Number value) {
        return uint256(value).withSize(250);
    }

    @NotNull
    public static EthUint uint252(@NotNull Number value) {
        return uint256(value).withSize(252);
    }

    @NotNull
    public static EthUint uint254(@NotNull Number value) {
        return uint256(value).withSize(254);
    }

    @NotNull
    public static EthUint uint8(@NotNull String hex) {
        return uint256(hex).withSize(8);
    }

    @NotNull
    public static EthUint uint10(@NotNull String hex) {
        return uint256(hex).withSize(10);
    }

    @NotNull
    public static EthUint uint12(@NotNull String hex) {
        return uint256(hex).withSize(12);
    }

    @NotNull
    public static EthUint uint14(@NotNull String hex) {
        return uint256(hex).withSize(14);
    }

    @NotNull
    public static EthUint uint16(@NotNull String hex) {
        return uint256(hex).withSize(16);
    }

    @NotNull
    public static EthUint uint18(@NotNull String hex) {
        return uint256(hex).withSize(18);
    }

    @NotNull
    public static EthUint uint20(@NotNull String hex) {
        return uint256(hex).withSize(20);
    }

    @NotNull
    public static EthUint uint22(@NotNull String hex) {
        return uint256(hex).withSize(22);
    }

    @NotNull
    public static EthUint uint24(@NotNull String hex) {
        return uint256(hex).withSize(24);
    }

    @NotNull
    public static EthUint uint26(@NotNull String hex) {
        return uint256(hex).withSize(26);
    }

    @NotNull
    public static EthUint uint28(@NotNull String hex) {
        return uint256(hex).withSize(28);
    }

    @NotNull
    public static EthUint uint30(@NotNull String hex) {
        return uint256(hex).withSize(30);
    }

    @NotNull
    public static EthUint uint32(@NotNull String hex) {
        return uint256(hex).withSize(32);
    }

    @NotNull
    public static EthUint uint34(@NotNull String hex) {
        return uint256(hex).withSize(34);
    }

    @NotNull
    public static EthUint uint36(@NotNull String hex) {
        return uint256(hex).withSize(36);
    }

    @NotNull
    public static EthUint uint38(@NotNull String hex) {
        return uint256(hex).withSize(38);
    }

    @NotNull
    public static EthUint uint40(@NotNull String hex) {
        return uint256(hex).withSize(40);
    }

    @NotNull
    public static EthUint uint42(@NotNull String hex) {
        return uint256(hex).withSize(42);
    }

    @NotNull
    public static EthUint uint44(@NotNull String hex) {
        return uint256(hex).withSize(44);
    }

    @NotNull
    public static EthUint uint46(@NotNull String hex) {
        return uint256(hex).withSize(46);
    }

    @NotNull
    public static EthUint uint48(@NotNull String hex) {
        return uint256(hex).withSize(48);
    }

    @NotNull
    public static EthUint uint50(@NotNull String hex) {
        return uint256(hex).withSize(50);
    }

    @NotNull
    public static EthUint uint52(@NotNull String hex) {
        return uint256(hex).withSize(52);
    }

    @NotNull
    public static EthUint uint54(@NotNull String hex) {
        return uint256(hex).withSize(54);
    }

    @NotNull
    public static EthUint uint56(@NotNull String hex) {
        return uint256(hex).withSize(56);
    }

    @NotNull
    public static EthUint uint58(@NotNull String hex) {
        return uint256(hex).withSize(58);
    }

    @NotNull
    public static EthUint uint60(@NotNull String hex) {
        return uint256(hex).withSize(60);
    }

    @NotNull
    public static EthUint uint62(@NotNull String hex) {
        return uint256(hex).withSize(62);
    }

    @NotNull
    public static EthUint uint64(@NotNull String hex) {
        return uint256(hex).withSize(64);
    }

    @NotNull
    public static EthUint uint66(@NotNull String hex) {
        return uint256(hex).withSize(66);
    }

    @NotNull
    public static EthUint uint68(@NotNull String hex) {
        return uint256(hex).withSize(68);
    }

    @NotNull
    public static EthUint uint70(@NotNull String hex) {
        return uint256(hex).withSize(70);
    }

    @NotNull
    public static EthUint uint72(@NotNull String hex) {
        return uint256(hex).withSize(72);
    }

    @NotNull
    public static EthUint uint74(@NotNull String hex) {
        return uint256(hex).withSize(74);
    }

    @NotNull
    public static EthUint uint76(@NotNull String hex) {
        return uint256(hex).withSize(76);
    }

    @NotNull
    public static EthUint uint78(@NotNull String hex) {
        return uint256(hex).withSize(78);
    }

    @NotNull
    public static EthUint uint80(@NotNull String hex) {
        return uint256(hex).withSize(80);
    }

    @NotNull
    public static EthUint uint82(@NotNull String hex) {
        return uint256(hex).withSize(82);
    }

    @NotNull
    public static EthUint uint84(@NotNull String hex) {
        return uint256(hex).withSize(84);
    }

    @NotNull
    public static EthUint uint86(@NotNull String hex) {
        return uint256(hex).withSize(86);
    }

    @NotNull
    public static EthUint uint88(@NotNull String hex) {
        return uint256(hex).withSize(88);
    }

    @NotNull
    public static EthUint uint90(@NotNull String hex) {
        return uint256(hex).withSize(90);
    }

    @NotNull
    public static EthUint uint92(@NotNull String hex) {
        return uint256(hex).withSize(92);
    }

    @NotNull
    public static EthUint uint94(@NotNull String hex) {
        return uint256(hex).withSize(94);
    }

    @NotNull
    public static EthUint uint96(@NotNull String hex) {
        return uint256(hex).withSize(96);
    }

    @NotNull
    public static EthUint uint98(@NotNull String hex) {
        return uint256(hex).withSize(98);
    }

    @NotNull
    public static EthUint uint100(@NotNull String hex) {
        return uint256(hex).withSize(100);
    }

    @NotNull
    public static EthUint uint102(@NotNull String hex) {
        return uint256(hex).withSize(102);
    }

    @NotNull
    public static EthUint uint104(@NotNull String hex) {
        return uint256(hex).withSize(104);
    }

    @NotNull
    public static EthUint uint106(@NotNull String hex) {
        return uint256(hex).withSize(106);
    }

    @NotNull
    public static EthUint uint108(@NotNull String hex) {
        return uint256(hex).withSize(108);
    }

    @NotNull
    public static EthUint uint110(@NotNull String hex) {
        return uint256(hex).withSize(110);
    }

    @NotNull
    public static EthUint uint112(@NotNull String hex) {
        return uint256(hex).withSize(112);
    }

    @NotNull
    public static EthUint uint114(@NotNull String hex) {
        return uint256(hex).withSize(114);
    }

    @NotNull
    public static EthUint uint116(@NotNull String hex) {
        return uint256(hex).withSize(116);
    }

    @NotNull
    public static EthUint uint118(@NotNull String hex) {
        return uint256(hex).withSize(118);
    }

    @NotNull
    public static EthUint uint120(@NotNull String hex) {
        return uint256(hex).withSize(120);
    }

    @NotNull
    public static EthUint uint122(@NotNull String hex) {
        return uint256(hex).withSize(122);
    }

    @NotNull
    public static EthUint uint124(@NotNull String hex) {
        return uint256(hex).withSize(124);
    }

    @NotNull
    public static EthUint uint126(@NotNull String hex) {
        return uint256(hex).withSize(126);
    }

    @NotNull
    public static EthUint uint128(@NotNull String hex) {
        return uint256(hex).withSize(128);
    }

    @NotNull
    public static EthUint uint130(@NotNull String hex) {
        return uint256(hex).withSize(130);
    }

    @NotNull
    public static EthUint uint132(@NotNull String hex) {
        return uint256(hex).withSize(132);
    }

    @NotNull
    public static EthUint uint134(@NotNull String hex) {
        return uint256(hex).withSize(134);
    }

    @NotNull
    public static EthUint uint136(@NotNull String hex) {
        return uint256(hex).withSize(136);
    }

    @NotNull
    public static EthUint uint138(@NotNull String hex) {
        return uint256(hex).withSize(138);
    }

    @NotNull
    public static EthUint uint140(@NotNull String hex) {
        return uint256(hex).withSize(140);
    }

    @NotNull
    public static EthUint uint142(@NotNull String hex) {
        return uint256(hex).withSize(142);
    }

    @NotNull
    public static EthUint uint144(@NotNull String hex) {
        return uint256(hex).withSize(144);
    }

    @NotNull
    public static EthUint uint146(@NotNull String hex) {
        return uint256(hex).withSize(146);
    }

    @NotNull
    public static EthUint uint148(@NotNull String hex) {
        return uint256(hex).withSize(148);
    }

    @NotNull
    public static EthUint uint150(@NotNull String hex) {
        return uint256(hex).withSize(150);
    }

    @NotNull
    public static EthUint uint152(@NotNull String hex) {
        return uint256(hex).withSize(152);
    }

    @NotNull
    public static EthUint uint154(@NotNull String hex) {
        return uint256(hex).withSize(154);
    }

    @NotNull
    public static EthUint uint156(@NotNull String hex) {
        return uint256(hex).withSize(156);
    }

    @NotNull
    public static EthUint uint158(@NotNull String hex) {
        return uint256(hex).withSize(158);
    }

    @NotNull
    public static EthUint uint160(@NotNull String hex) {
        return uint256(hex).withSize(160);
    }

    @NotNull
    public static EthUint uint162(@NotNull String hex) {
        return uint256(hex).withSize(162);
    }

    @NotNull
    public static EthUint uint164(@NotNull String hex) {
        return uint256(hex).withSize(164);
    }

    @NotNull
    public static EthUint uint166(@NotNull String hex) {
        return uint256(hex).withSize(166);
    }

    @NotNull
    public static EthUint uint168(@NotNull String hex) {
        return uint256(hex).withSize(168);
    }

    @NotNull
    public static EthUint uint170(@NotNull String hex) {
        return uint256(hex).withSize(170);
    }

    @NotNull
    public static EthUint uint172(@NotNull String hex) {
        return uint256(hex).withSize(172);
    }

    @NotNull
    public static EthUint uint174(@NotNull String hex) {
        return uint256(hex).withSize(174);
    }

    @NotNull
    public static EthUint uint176(@NotNull String hex) {
        return uint256(hex).withSize(176);
    }

    @NotNull
    public static EthUint uint178(@NotNull String hex) {
        return uint256(hex).withSize(178);
    }

    @NotNull
    public static EthUint uint180(@NotNull String hex) {
        return uint256(hex).withSize(180);
    }

    @NotNull
    public static EthUint uint182(@NotNull String hex) {
        return uint256(hex).withSize(182);
    }

    @NotNull
    public static EthUint uint184(@NotNull String hex) {
        return uint256(hex).withSize(184);
    }

    @NotNull
    public static EthUint uint186(@NotNull String hex) {
        return uint256(hex).withSize(186);
    }

    @NotNull
    public static EthUint uint188(@NotNull String hex) {
        return uint256(hex).withSize(188);
    }

    @NotNull
    public static EthUint uint190(@NotNull String hex) {
        return uint256(hex).withSize(190);
    }

    @NotNull
    public static EthUint uint192(@NotNull String hex) {
        return uint256(hex).withSize(192);
    }

    @NotNull
    public static EthUint uint194(@NotNull String hex) {
        return uint256(hex).withSize(194);
    }

    @NotNull
    public static EthUint uint196(@NotNull String hex) {
        return uint256(hex).withSize(196);
    }

    @NotNull
    public static EthUint uint198(@NotNull String hex) {
        return uint256(hex).withSize(198);
    }

    @NotNull
    public static EthUint uint200(@NotNull String hex) {
        return uint256(hex).withSize(200);
    }

    @NotNull
    public static EthUint uint202(@NotNull String hex) {
        return uint256(hex).withSize(202);
    }

    @NotNull
    public static EthUint uint204(@NotNull String hex) {
        return uint256(hex).withSize(204);
    }

    @NotNull
    public static EthUint uint206(@NotNull String hex) {
        return uint256(hex).withSize(206);
    }

    @NotNull
    public static EthUint uint208(@NotNull String hex) {
        return uint256(hex).withSize(208);
    }

    @NotNull
    public static EthUint uint210(@NotNull String hex) {
        return uint256(hex).withSize(210);
    }

    @NotNull
    public static EthUint uint212(@NotNull String hex) {
        return uint256(hex).withSize(212);
    }

    @NotNull
    public static EthUint uint214(@NotNull String hex) {
        return uint256(hex).withSize(214);
    }

    @NotNull
    public static EthUint uint216(@NotNull String hex) {
        return uint256(hex).withSize(216);
    }

    @NotNull
    public static EthUint uint218(@NotNull String hex) {
        return uint256(hex).withSize(218);
    }

    @NotNull
    public static EthUint uint220(@NotNull String hex) {
        return uint256(hex).withSize(220);
    }

    @NotNull
    public static EthUint uint222(@NotNull String hex) {
        return uint256(hex).withSize(222);
    }

    @NotNull
    public static EthUint uint224(@NotNull String hex) {
        return uint256(hex).withSize(224);
    }

    @NotNull
    public static EthUint uint226(@NotNull String hex) {
        return uint256(hex).withSize(226);
    }

    @NotNull
    public static EthUint uint228(@NotNull String hex) {
        return uint256(hex).withSize(228);
    }

    @NotNull
    public static EthUint uint230(@NotNull String hex) {
        return uint256(hex).withSize(230);
    }

    @NotNull
    public static EthUint uint232(@NotNull String hex) {
        return uint256(hex).withSize(232);
    }

    @NotNull
    public static EthUint uint234(@NotNull String hex) {
        return uint256(hex).withSize(234);
    }

    @NotNull
    public static EthUint uint236(@NotNull String hex) {
        return uint256(hex).withSize(236);
    }

    @NotNull
    public static EthUint uint238(@NotNull String hex) {
        return uint256(hex).withSize(238);
    }

    @NotNull
    public static EthUint uint240(@NotNull String hex) {
        return uint256(hex).withSize(240);
    }

    @NotNull
    public static EthUint uint242(@NotNull String hex) {
        return uint256(hex).withSize(242);
    }

    @NotNull
    public static EthUint uint244(@NotNull String hex) {
        return uint256(hex).withSize(244);
    }

    @NotNull
    public static EthUint uint246(@NotNull String hex) {
        return uint256(hex).withSize(246);
    }

    @NotNull
    public static EthUint uint248(@NotNull String hex) {
        return uint256(hex).withSize(248);
    }

    @NotNull
    public static EthUint uint250(@NotNull String hex) {
        return uint256(hex).withSize(250);
    }

    @NotNull
    public static EthUint uint252(@NotNull String hex) {
        return uint256(hex).withSize(252);
    }

    @NotNull
    public static EthUint uint254(@NotNull String hex) {
        return uint256(hex).withSize(254);
    }

}
