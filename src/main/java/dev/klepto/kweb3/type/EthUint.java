package dev.klepto.kweb3.type;

import dev.klepto.kweb3.util.Hex;
import lombok.With;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

import static dev.klepto.kweb3.util.Conditions.require;

/**
 * Represents ethereum <code>uint</code> data type.
 *
 * @param size  the size in bits of this <code>uint</code>, from 1 to 256
 * @param value the positive integer value
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public record EthUint(int size, BigInteger value) implements EthNumericType, EthSizedType {

    /**
     * Zero uint constant.
     */
    public static final EthUint ZERO = uint256(0);

    public EthUint {
        require(size >= value.bitLength(), "uint{} cannot fit value: {}", size, value);
    }

    @Override
    public String toString() {
        return "uint" + size + "(" + value + ")";
    }

    /* Solidity style uint initializers */
    @NotNull
    public static EthUint uint256(@NotNull BigInteger value) {
        return new EthUint(256, value);
    }

    @NotNull
    public static EthUint uint256(long value) {
        return uint256(BigInteger.valueOf(value));
    }

    @NotNull
    public static EthUint uint256(@NotNull String hex) {
        require(Hex.isValid(hex), "Malformed hex string: {}", hex);
        return uint256(Hex.toBigInteger(hex));
    }

    /* Smaller size solidity int initializers */
    @NotNull
    public static EthUint uint8(long value) {
        return uint256(value).withSize(8);
    }

    @NotNull
    public static EthUint uint10(long value) {
        return uint256(value).withSize(10);
    }

    @NotNull
    public static EthUint uint12(long value) {
        return uint256(value).withSize(12);
    }

    @NotNull
    public static EthUint uint14(long value) {
        return uint256(value).withSize(14);
    }

    @NotNull
    public static EthUint uint16(long value) {
        return uint256(value).withSize(16);
    }

    @NotNull
    public static EthUint uint18(long value) {
        return uint256(value).withSize(18);
    }

    @NotNull
    public static EthUint uint20(long value) {
        return uint256(value).withSize(20);
    }

    @NotNull
    public static EthUint uint22(long value) {
        return uint256(value).withSize(22);
    }

    @NotNull
    public static EthUint uint24(long value) {
        return uint256(value).withSize(24);
    }

    @NotNull
    public static EthUint uint26(long value) {
        return uint256(value).withSize(26);
    }

    @NotNull
    public static EthUint uint28(long value) {
        return uint256(value).withSize(28);
    }

    @NotNull
    public static EthUint uint30(long value) {
        return uint256(value).withSize(30);
    }

    @NotNull
    public static EthUint uint32(long value) {
        return uint256(value).withSize(32);
    }

    @NotNull
    public static EthUint uint34(long value) {
        return uint256(value).withSize(34);
    }

    @NotNull
    public static EthUint uint36(long value) {
        return uint256(value).withSize(36);
    }

    @NotNull
    public static EthUint uint38(long value) {
        return uint256(value).withSize(38);
    }

    @NotNull
    public static EthUint uint40(long value) {
        return uint256(value).withSize(40);
    }

    @NotNull
    public static EthUint uint42(long value) {
        return uint256(value).withSize(42);
    }

    @NotNull
    public static EthUint uint44(long value) {
        return uint256(value).withSize(44);
    }

    @NotNull
    public static EthUint uint46(long value) {
        return uint256(value).withSize(46);
    }

    @NotNull
    public static EthUint uint48(long value) {
        return uint256(value).withSize(48);
    }

    @NotNull
    public static EthUint uint50(long value) {
        return uint256(value).withSize(50);
    }

    @NotNull
    public static EthUint uint52(long value) {
        return uint256(value).withSize(52);
    }

    @NotNull
    public static EthUint uint54(long value) {
        return uint256(value).withSize(54);
    }

    @NotNull
    public static EthUint uint56(long value) {
        return uint256(value).withSize(56);
    }

    @NotNull
    public static EthUint uint58(long value) {
        return uint256(value).withSize(58);
    }

    @NotNull
    public static EthUint uint60(long value) {
        return uint256(value).withSize(60);
    }

    @NotNull
    public static EthUint uint62(long value) {
        return uint256(value).withSize(62);
    }

    @NotNull
    public static EthUint uint64(long value) {
        return uint256(value).withSize(64);
    }

    @NotNull
    public static EthUint uint66(long value) {
        return uint256(value).withSize(66);
    }

    @NotNull
    public static EthUint uint68(long value) {
        return uint256(value).withSize(68);
    }

    @NotNull
    public static EthUint uint70(long value) {
        return uint256(value).withSize(70);
    }

    @NotNull
    public static EthUint uint72(long value) {
        return uint256(value).withSize(72);
    }

    @NotNull
    public static EthUint uint74(long value) {
        return uint256(value).withSize(74);
    }

    @NotNull
    public static EthUint uint76(long value) {
        return uint256(value).withSize(76);
    }

    @NotNull
    public static EthUint uint78(long value) {
        return uint256(value).withSize(78);
    }

    @NotNull
    public static EthUint uint80(long value) {
        return uint256(value).withSize(80);
    }

    @NotNull
    public static EthUint uint82(long value) {
        return uint256(value).withSize(82);
    }

    @NotNull
    public static EthUint uint84(long value) {
        return uint256(value).withSize(84);
    }

    @NotNull
    public static EthUint uint86(long value) {
        return uint256(value).withSize(86);
    }

    @NotNull
    public static EthUint uint88(long value) {
        return uint256(value).withSize(88);
    }

    @NotNull
    public static EthUint uint90(long value) {
        return uint256(value).withSize(90);
    }

    @NotNull
    public static EthUint uint92(long value) {
        return uint256(value).withSize(92);
    }

    @NotNull
    public static EthUint uint94(long value) {
        return uint256(value).withSize(94);
    }

    @NotNull
    public static EthUint uint96(long value) {
        return uint256(value).withSize(96);
    }

    @NotNull
    public static EthUint uint98(long value) {
        return uint256(value).withSize(98);
    }

    @NotNull
    public static EthUint uint100(long value) {
        return uint256(value).withSize(100);
    }

    @NotNull
    public static EthUint uint102(long value) {
        return uint256(value).withSize(102);
    }

    @NotNull
    public static EthUint uint104(long value) {
        return uint256(value).withSize(104);
    }

    @NotNull
    public static EthUint uint106(long value) {
        return uint256(value).withSize(106);
    }

    @NotNull
    public static EthUint uint108(long value) {
        return uint256(value).withSize(108);
    }

    @NotNull
    public static EthUint uint110(long value) {
        return uint256(value).withSize(110);
    }

    @NotNull
    public static EthUint uint112(long value) {
        return uint256(value).withSize(112);
    }

    @NotNull
    public static EthUint uint114(long value) {
        return uint256(value).withSize(114);
    }

    @NotNull
    public static EthUint uint116(long value) {
        return uint256(value).withSize(116);
    }

    @NotNull
    public static EthUint uint118(long value) {
        return uint256(value).withSize(118);
    }

    @NotNull
    public static EthUint uint120(long value) {
        return uint256(value).withSize(120);
    }

    @NotNull
    public static EthUint uint122(long value) {
        return uint256(value).withSize(122);
    }

    @NotNull
    public static EthUint uint124(long value) {
        return uint256(value).withSize(124);
    }

    @NotNull
    public static EthUint uint126(long value) {
        return uint256(value).withSize(126);
    }

    @NotNull
    public static EthUint uint128(long value) {
        return uint256(value).withSize(128);
    }

    @NotNull
    public static EthUint uint130(long value) {
        return uint256(value).withSize(130);
    }

    @NotNull
    public static EthUint uint132(long value) {
        return uint256(value).withSize(132);
    }

    @NotNull
    public static EthUint uint134(long value) {
        return uint256(value).withSize(134);
    }

    @NotNull
    public static EthUint uint136(long value) {
        return uint256(value).withSize(136);
    }

    @NotNull
    public static EthUint uint138(long value) {
        return uint256(value).withSize(138);
    }

    @NotNull
    public static EthUint uint140(long value) {
        return uint256(value).withSize(140);
    }

    @NotNull
    public static EthUint uint142(long value) {
        return uint256(value).withSize(142);
    }

    @NotNull
    public static EthUint uint144(long value) {
        return uint256(value).withSize(144);
    }

    @NotNull
    public static EthUint uint146(long value) {
        return uint256(value).withSize(146);
    }

    @NotNull
    public static EthUint uint148(long value) {
        return uint256(value).withSize(148);
    }

    @NotNull
    public static EthUint uint150(long value) {
        return uint256(value).withSize(150);
    }

    @NotNull
    public static EthUint uint152(long value) {
        return uint256(value).withSize(152);
    }

    @NotNull
    public static EthUint uint154(long value) {
        return uint256(value).withSize(154);
    }

    @NotNull
    public static EthUint uint156(long value) {
        return uint256(value).withSize(156);
    }

    @NotNull
    public static EthUint uint158(long value) {
        return uint256(value).withSize(158);
    }

    @NotNull
    public static EthUint uint160(long value) {
        return uint256(value).withSize(160);
    }

    @NotNull
    public static EthUint uint162(long value) {
        return uint256(value).withSize(162);
    }

    @NotNull
    public static EthUint uint164(long value) {
        return uint256(value).withSize(164);
    }

    @NotNull
    public static EthUint uint166(long value) {
        return uint256(value).withSize(166);
    }

    @NotNull
    public static EthUint uint168(long value) {
        return uint256(value).withSize(168);
    }

    @NotNull
    public static EthUint uint170(long value) {
        return uint256(value).withSize(170);
    }

    @NotNull
    public static EthUint uint172(long value) {
        return uint256(value).withSize(172);
    }

    @NotNull
    public static EthUint uint174(long value) {
        return uint256(value).withSize(174);
    }

    @NotNull
    public static EthUint uint176(long value) {
        return uint256(value).withSize(176);
    }

    @NotNull
    public static EthUint uint178(long value) {
        return uint256(value).withSize(178);
    }

    @NotNull
    public static EthUint uint180(long value) {
        return uint256(value).withSize(180);
    }

    @NotNull
    public static EthUint uint182(long value) {
        return uint256(value).withSize(182);
    }

    @NotNull
    public static EthUint uint184(long value) {
        return uint256(value).withSize(184);
    }

    @NotNull
    public static EthUint uint186(long value) {
        return uint256(value).withSize(186);
    }

    @NotNull
    public static EthUint uint188(long value) {
        return uint256(value).withSize(188);
    }

    @NotNull
    public static EthUint uint190(long value) {
        return uint256(value).withSize(190);
    }

    @NotNull
    public static EthUint uint192(long value) {
        return uint256(value).withSize(192);
    }

    @NotNull
    public static EthUint uint194(long value) {
        return uint256(value).withSize(194);
    }

    @NotNull
    public static EthUint uint196(long value) {
        return uint256(value).withSize(196);
    }

    @NotNull
    public static EthUint uint198(long value) {
        return uint256(value).withSize(198);
    }

    @NotNull
    public static EthUint uint200(long value) {
        return uint256(value).withSize(200);
    }

    @NotNull
    public static EthUint uint202(long value) {
        return uint256(value).withSize(202);
    }

    @NotNull
    public static EthUint uint204(long value) {
        return uint256(value).withSize(204);
    }

    @NotNull
    public static EthUint uint206(long value) {
        return uint256(value).withSize(206);
    }

    @NotNull
    public static EthUint uint208(long value) {
        return uint256(value).withSize(208);
    }

    @NotNull
    public static EthUint uint210(long value) {
        return uint256(value).withSize(210);
    }

    @NotNull
    public static EthUint uint212(long value) {
        return uint256(value).withSize(212);
    }

    @NotNull
    public static EthUint uint214(long value) {
        return uint256(value).withSize(214);
    }

    @NotNull
    public static EthUint uint216(long value) {
        return uint256(value).withSize(216);
    }

    @NotNull
    public static EthUint uint218(long value) {
        return uint256(value).withSize(218);
    }

    @NotNull
    public static EthUint uint220(long value) {
        return uint256(value).withSize(220);
    }

    @NotNull
    public static EthUint uint222(long value) {
        return uint256(value).withSize(222);
    }

    @NotNull
    public static EthUint uint224(long value) {
        return uint256(value).withSize(224);
    }

    @NotNull
    public static EthUint uint226(long value) {
        return uint256(value).withSize(226);
    }

    @NotNull
    public static EthUint uint228(long value) {
        return uint256(value).withSize(228);
    }

    @NotNull
    public static EthUint uint230(long value) {
        return uint256(value).withSize(230);
    }

    @NotNull
    public static EthUint uint232(long value) {
        return uint256(value).withSize(232);
    }

    @NotNull
    public static EthUint uint234(long value) {
        return uint256(value).withSize(234);
    }

    @NotNull
    public static EthUint uint236(long value) {
        return uint256(value).withSize(236);
    }

    @NotNull
    public static EthUint uint238(long value) {
        return uint256(value).withSize(238);
    }

    @NotNull
    public static EthUint uint240(long value) {
        return uint256(value).withSize(240);
    }

    @NotNull
    public static EthUint uint242(long value) {
        return uint256(value).withSize(242);
    }

    @NotNull
    public static EthUint uint244(long value) {
        return uint256(value).withSize(244);
    }

    @NotNull
    public static EthUint uint246(long value) {
        return uint256(value).withSize(246);
    }

    @NotNull
    public static EthUint uint248(long value) {
        return uint256(value).withSize(248);
    }

    @NotNull
    public static EthUint uint250(long value) {
        return uint256(value).withSize(250);
    }

    @NotNull
    public static EthUint uint252(long value) {
        return uint256(value).withSize(252);
    }

    @NotNull
    public static EthUint uint254(long value) {
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
