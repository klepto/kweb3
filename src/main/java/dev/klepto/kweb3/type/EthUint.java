package dev.klepto.kweb3.type;

import dev.klepto.kweb3.util.Hex;
import lombok.With;

import java.math.BigInteger;

import static dev.klepto.kweb3.error.Conditions.require;

/**
 * Represents ethereum <code>uint</code> data type.
 *
 * @param size  the size in bits of this <code>uint</code>, from 1 to 256
 * @param value the positive integer value
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public record EthUint(int size, BigInteger value) implements EthNumericType {

    public EthUint {
        require(size >= value.bitLength(), "uint{} cannot fit value: {}", size, value);
    }

    @Override
    public String toString() {
        return "uint" + size + "(" + value + ")";
    }

    /* Solidity style uint initializers */
    public static EthUint uint256(BigInteger value) {
        return new EthUint(256, value);
    }

    public static EthUint uint256(long value) {
        return uint256(BigInteger.valueOf(value));
    }

    public static EthUint uint256(String hex) {
        require(Hex.isValid(hex), "Malformed hex string: {}", hex);
        return uint256(Hex.toBigInteger(hex));
    }

    /* Smaller size solidity int initializers */
    public static EthUint uint8(long value) {
        return uint256(value).withSize(8);
    }

    public static EthUint uint10(long value) {
        return uint256(value).withSize(10);
    }

    public static EthUint uint12(long value) {
        return uint256(value).withSize(12);
    }

    public static EthUint uint14(long value) {
        return uint256(value).withSize(14);
    }

    public static EthUint uint16(long value) {
        return uint256(value).withSize(16);
    }

    public static EthUint uint18(long value) {
        return uint256(value).withSize(18);
    }

    public static EthUint uint20(long value) {
        return uint256(value).withSize(20);
    }

    public static EthUint uint22(long value) {
        return uint256(value).withSize(22);
    }

    public static EthUint uint24(long value) {
        return uint256(value).withSize(24);
    }

    public static EthUint uint26(long value) {
        return uint256(value).withSize(26);
    }

    public static EthUint uint28(long value) {
        return uint256(value).withSize(28);
    }

    public static EthUint uint30(long value) {
        return uint256(value).withSize(30);
    }

    public static EthUint uint32(long value) {
        return uint256(value).withSize(32);
    }

    public static EthUint uint34(long value) {
        return uint256(value).withSize(34);
    }

    public static EthUint uint36(long value) {
        return uint256(value).withSize(36);
    }

    public static EthUint uint38(long value) {
        return uint256(value).withSize(38);
    }

    public static EthUint uint40(long value) {
        return uint256(value).withSize(40);
    }

    public static EthUint uint42(long value) {
        return uint256(value).withSize(42);
    }

    public static EthUint uint44(long value) {
        return uint256(value).withSize(44);
    }

    public static EthUint uint46(long value) {
        return uint256(value).withSize(46);
    }

    public static EthUint uint48(long value) {
        return uint256(value).withSize(48);
    }

    public static EthUint uint50(long value) {
        return uint256(value).withSize(50);
    }

    public static EthUint uint52(long value) {
        return uint256(value).withSize(52);
    }

    public static EthUint uint54(long value) {
        return uint256(value).withSize(54);
    }

    public static EthUint uint56(long value) {
        return uint256(value).withSize(56);
    }

    public static EthUint uint58(long value) {
        return uint256(value).withSize(58);
    }

    public static EthUint uint60(long value) {
        return uint256(value).withSize(60);
    }

    public static EthUint uint62(long value) {
        return uint256(value).withSize(62);
    }

    public static EthUint uint64(long value) {
        return uint256(value).withSize(64);
    }

    public static EthUint uint66(long value) {
        return uint256(value).withSize(66);
    }

    public static EthUint uint68(long value) {
        return uint256(value).withSize(68);
    }

    public static EthUint uint70(long value) {
        return uint256(value).withSize(70);
    }

    public static EthUint uint72(long value) {
        return uint256(value).withSize(72);
    }

    public static EthUint uint74(long value) {
        return uint256(value).withSize(74);
    }

    public static EthUint uint76(long value) {
        return uint256(value).withSize(76);
    }

    public static EthUint uint78(long value) {
        return uint256(value).withSize(78);
    }

    public static EthUint uint80(long value) {
        return uint256(value).withSize(80);
    }

    public static EthUint uint82(long value) {
        return uint256(value).withSize(82);
    }

    public static EthUint uint84(long value) {
        return uint256(value).withSize(84);
    }

    public static EthUint uint86(long value) {
        return uint256(value).withSize(86);
    }

    public static EthUint uint88(long value) {
        return uint256(value).withSize(88);
    }

    public static EthUint uint90(long value) {
        return uint256(value).withSize(90);
    }

    public static EthUint uint92(long value) {
        return uint256(value).withSize(92);
    }

    public static EthUint uint94(long value) {
        return uint256(value).withSize(94);
    }

    public static EthUint uint96(long value) {
        return uint256(value).withSize(96);
    }

    public static EthUint uint98(long value) {
        return uint256(value).withSize(98);
    }

    public static EthUint uint100(long value) {
        return uint256(value).withSize(100);
    }

    public static EthUint uint102(long value) {
        return uint256(value).withSize(102);
    }

    public static EthUint uint104(long value) {
        return uint256(value).withSize(104);
    }

    public static EthUint uint106(long value) {
        return uint256(value).withSize(106);
    }

    public static EthUint uint108(long value) {
        return uint256(value).withSize(108);
    }

    public static EthUint uint110(long value) {
        return uint256(value).withSize(110);
    }

    public static EthUint uint112(long value) {
        return uint256(value).withSize(112);
    }

    public static EthUint uint114(long value) {
        return uint256(value).withSize(114);
    }

    public static EthUint uint116(long value) {
        return uint256(value).withSize(116);
    }

    public static EthUint uint118(long value) {
        return uint256(value).withSize(118);
    }

    public static EthUint uint120(long value) {
        return uint256(value).withSize(120);
    }

    public static EthUint uint122(long value) {
        return uint256(value).withSize(122);
    }

    public static EthUint uint124(long value) {
        return uint256(value).withSize(124);
    }

    public static EthUint uint126(long value) {
        return uint256(value).withSize(126);
    }

    public static EthUint uint128(long value) {
        return uint256(value).withSize(128);
    }

    public static EthUint uint130(long value) {
        return uint256(value).withSize(130);
    }

    public static EthUint uint132(long value) {
        return uint256(value).withSize(132);
    }

    public static EthUint uint134(long value) {
        return uint256(value).withSize(134);
    }

    public static EthUint uint136(long value) {
        return uint256(value).withSize(136);
    }

    public static EthUint uint138(long value) {
        return uint256(value).withSize(138);
    }

    public static EthUint uint140(long value) {
        return uint256(value).withSize(140);
    }

    public static EthUint uint142(long value) {
        return uint256(value).withSize(142);
    }

    public static EthUint uint144(long value) {
        return uint256(value).withSize(144);
    }

    public static EthUint uint146(long value) {
        return uint256(value).withSize(146);
    }

    public static EthUint uint148(long value) {
        return uint256(value).withSize(148);
    }

    public static EthUint uint150(long value) {
        return uint256(value).withSize(150);
    }

    public static EthUint uint152(long value) {
        return uint256(value).withSize(152);
    }

    public static EthUint uint154(long value) {
        return uint256(value).withSize(154);
    }

    public static EthUint uint156(long value) {
        return uint256(value).withSize(156);
    }

    public static EthUint uint158(long value) {
        return uint256(value).withSize(158);
    }

    public static EthUint uint160(long value) {
        return uint256(value).withSize(160);
    }

    public static EthUint uint162(long value) {
        return uint256(value).withSize(162);
    }

    public static EthUint uint164(long value) {
        return uint256(value).withSize(164);
    }

    public static EthUint uint166(long value) {
        return uint256(value).withSize(166);
    }

    public static EthUint uint168(long value) {
        return uint256(value).withSize(168);
    }

    public static EthUint uint170(long value) {
        return uint256(value).withSize(170);
    }

    public static EthUint uint172(long value) {
        return uint256(value).withSize(172);
    }

    public static EthUint uint174(long value) {
        return uint256(value).withSize(174);
    }

    public static EthUint uint176(long value) {
        return uint256(value).withSize(176);
    }

    public static EthUint uint178(long value) {
        return uint256(value).withSize(178);
    }

    public static EthUint uint180(long value) {
        return uint256(value).withSize(180);
    }

    public static EthUint uint182(long value) {
        return uint256(value).withSize(182);
    }

    public static EthUint uint184(long value) {
        return uint256(value).withSize(184);
    }

    public static EthUint uint186(long value) {
        return uint256(value).withSize(186);
    }

    public static EthUint uint188(long value) {
        return uint256(value).withSize(188);
    }

    public static EthUint uint190(long value) {
        return uint256(value).withSize(190);
    }

    public static EthUint uint192(long value) {
        return uint256(value).withSize(192);
    }

    public static EthUint uint194(long value) {
        return uint256(value).withSize(194);
    }

    public static EthUint uint196(long value) {
        return uint256(value).withSize(196);
    }

    public static EthUint uint198(long value) {
        return uint256(value).withSize(198);
    }

    public static EthUint uint200(long value) {
        return uint256(value).withSize(200);
    }

    public static EthUint uint202(long value) {
        return uint256(value).withSize(202);
    }

    public static EthUint uint204(long value) {
        return uint256(value).withSize(204);
    }

    public static EthUint uint206(long value) {
        return uint256(value).withSize(206);
    }

    public static EthUint uint208(long value) {
        return uint256(value).withSize(208);
    }

    public static EthUint uint210(long value) {
        return uint256(value).withSize(210);
    }

    public static EthUint uint212(long value) {
        return uint256(value).withSize(212);
    }

    public static EthUint uint214(long value) {
        return uint256(value).withSize(214);
    }

    public static EthUint uint216(long value) {
        return uint256(value).withSize(216);
    }

    public static EthUint uint218(long value) {
        return uint256(value).withSize(218);
    }

    public static EthUint uint220(long value) {
        return uint256(value).withSize(220);
    }

    public static EthUint uint222(long value) {
        return uint256(value).withSize(222);
    }

    public static EthUint uint224(long value) {
        return uint256(value).withSize(224);
    }

    public static EthUint uint226(long value) {
        return uint256(value).withSize(226);
    }

    public static EthUint uint228(long value) {
        return uint256(value).withSize(228);
    }

    public static EthUint uint230(long value) {
        return uint256(value).withSize(230);
    }

    public static EthUint uint232(long value) {
        return uint256(value).withSize(232);
    }

    public static EthUint uint234(long value) {
        return uint256(value).withSize(234);
    }

    public static EthUint uint236(long value) {
        return uint256(value).withSize(236);
    }

    public static EthUint uint238(long value) {
        return uint256(value).withSize(238);
    }

    public static EthUint uint240(long value) {
        return uint256(value).withSize(240);
    }

    public static EthUint uint242(long value) {
        return uint256(value).withSize(242);
    }

    public static EthUint uint244(long value) {
        return uint256(value).withSize(244);
    }

    public static EthUint uint246(long value) {
        return uint256(value).withSize(246);
    }

    public static EthUint uint248(long value) {
        return uint256(value).withSize(248);
    }

    public static EthUint uint250(long value) {
        return uint256(value).withSize(250);
    }

    public static EthUint uint252(long value) {
        return uint256(value).withSize(252);
    }

    public static EthUint uint254(long value) {
        return uint256(value).withSize(254);
    }

    public static EthUint uint8(String hex) {
        return uint256(hex).withSize(8);
    }

    public static EthUint uint10(String hex) {
        return uint256(hex).withSize(10);
    }

    public static EthUint uint12(String hex) {
        return uint256(hex).withSize(12);
    }

    public static EthUint uint14(String hex) {
        return uint256(hex).withSize(14);
    }

    public static EthUint uint16(String hex) {
        return uint256(hex).withSize(16);
    }

    public static EthUint uint18(String hex) {
        return uint256(hex).withSize(18);
    }

    public static EthUint uint20(String hex) {
        return uint256(hex).withSize(20);
    }

    public static EthUint uint22(String hex) {
        return uint256(hex).withSize(22);
    }

    public static EthUint uint24(String hex) {
        return uint256(hex).withSize(24);
    }

    public static EthUint uint26(String hex) {
        return uint256(hex).withSize(26);
    }

    public static EthUint uint28(String hex) {
        return uint256(hex).withSize(28);
    }

    public static EthUint uint30(String hex) {
        return uint256(hex).withSize(30);
    }

    public static EthUint uint32(String hex) {
        return uint256(hex).withSize(32);
    }

    public static EthUint uint34(String hex) {
        return uint256(hex).withSize(34);
    }

    public static EthUint uint36(String hex) {
        return uint256(hex).withSize(36);
    }

    public static EthUint uint38(String hex) {
        return uint256(hex).withSize(38);
    }

    public static EthUint uint40(String hex) {
        return uint256(hex).withSize(40);
    }

    public static EthUint uint42(String hex) {
        return uint256(hex).withSize(42);
    }

    public static EthUint uint44(String hex) {
        return uint256(hex).withSize(44);
    }

    public static EthUint uint46(String hex) {
        return uint256(hex).withSize(46);
    }

    public static EthUint uint48(String hex) {
        return uint256(hex).withSize(48);
    }

    public static EthUint uint50(String hex) {
        return uint256(hex).withSize(50);
    }

    public static EthUint uint52(String hex) {
        return uint256(hex).withSize(52);
    }

    public static EthUint uint54(String hex) {
        return uint256(hex).withSize(54);
    }

    public static EthUint uint56(String hex) {
        return uint256(hex).withSize(56);
    }

    public static EthUint uint58(String hex) {
        return uint256(hex).withSize(58);
    }

    public static EthUint uint60(String hex) {
        return uint256(hex).withSize(60);
    }

    public static EthUint uint62(String hex) {
        return uint256(hex).withSize(62);
    }

    public static EthUint uint64(String hex) {
        return uint256(hex).withSize(64);
    }

    public static EthUint uint66(String hex) {
        return uint256(hex).withSize(66);
    }

    public static EthUint uint68(String hex) {
        return uint256(hex).withSize(68);
    }

    public static EthUint uint70(String hex) {
        return uint256(hex).withSize(70);
    }

    public static EthUint uint72(String hex) {
        return uint256(hex).withSize(72);
    }

    public static EthUint uint74(String hex) {
        return uint256(hex).withSize(74);
    }

    public static EthUint uint76(String hex) {
        return uint256(hex).withSize(76);
    }

    public static EthUint uint78(String hex) {
        return uint256(hex).withSize(78);
    }

    public static EthUint uint80(String hex) {
        return uint256(hex).withSize(80);
    }

    public static EthUint uint82(String hex) {
        return uint256(hex).withSize(82);
    }

    public static EthUint uint84(String hex) {
        return uint256(hex).withSize(84);
    }

    public static EthUint uint86(String hex) {
        return uint256(hex).withSize(86);
    }

    public static EthUint uint88(String hex) {
        return uint256(hex).withSize(88);
    }

    public static EthUint uint90(String hex) {
        return uint256(hex).withSize(90);
    }

    public static EthUint uint92(String hex) {
        return uint256(hex).withSize(92);
    }

    public static EthUint uint94(String hex) {
        return uint256(hex).withSize(94);
    }

    public static EthUint uint96(String hex) {
        return uint256(hex).withSize(96);
    }

    public static EthUint uint98(String hex) {
        return uint256(hex).withSize(98);
    }

    public static EthUint uint100(String hex) {
        return uint256(hex).withSize(100);
    }

    public static EthUint uint102(String hex) {
        return uint256(hex).withSize(102);
    }

    public static EthUint uint104(String hex) {
        return uint256(hex).withSize(104);
    }

    public static EthUint uint106(String hex) {
        return uint256(hex).withSize(106);
    }

    public static EthUint uint108(String hex) {
        return uint256(hex).withSize(108);
    }

    public static EthUint uint110(String hex) {
        return uint256(hex).withSize(110);
    }

    public static EthUint uint112(String hex) {
        return uint256(hex).withSize(112);
    }

    public static EthUint uint114(String hex) {
        return uint256(hex).withSize(114);
    }

    public static EthUint uint116(String hex) {
        return uint256(hex).withSize(116);
    }

    public static EthUint uint118(String hex) {
        return uint256(hex).withSize(118);
    }

    public static EthUint uint120(String hex) {
        return uint256(hex).withSize(120);
    }

    public static EthUint uint122(String hex) {
        return uint256(hex).withSize(122);
    }

    public static EthUint uint124(String hex) {
        return uint256(hex).withSize(124);
    }

    public static EthUint uint126(String hex) {
        return uint256(hex).withSize(126);
    }

    public static EthUint uint128(String hex) {
        return uint256(hex).withSize(128);
    }

    public static EthUint uint130(String hex) {
        return uint256(hex).withSize(130);
    }

    public static EthUint uint132(String hex) {
        return uint256(hex).withSize(132);
    }

    public static EthUint uint134(String hex) {
        return uint256(hex).withSize(134);
    }

    public static EthUint uint136(String hex) {
        return uint256(hex).withSize(136);
    }

    public static EthUint uint138(String hex) {
        return uint256(hex).withSize(138);
    }

    public static EthUint uint140(String hex) {
        return uint256(hex).withSize(140);
    }

    public static EthUint uint142(String hex) {
        return uint256(hex).withSize(142);
    }

    public static EthUint uint144(String hex) {
        return uint256(hex).withSize(144);
    }

    public static EthUint uint146(String hex) {
        return uint256(hex).withSize(146);
    }

    public static EthUint uint148(String hex) {
        return uint256(hex).withSize(148);
    }

    public static EthUint uint150(String hex) {
        return uint256(hex).withSize(150);
    }

    public static EthUint uint152(String hex) {
        return uint256(hex).withSize(152);
    }

    public static EthUint uint154(String hex) {
        return uint256(hex).withSize(154);
    }

    public static EthUint uint156(String hex) {
        return uint256(hex).withSize(156);
    }

    public static EthUint uint158(String hex) {
        return uint256(hex).withSize(158);
    }

    public static EthUint uint160(String hex) {
        return uint256(hex).withSize(160);
    }

    public static EthUint uint162(String hex) {
        return uint256(hex).withSize(162);
    }

    public static EthUint uint164(String hex) {
        return uint256(hex).withSize(164);
    }

    public static EthUint uint166(String hex) {
        return uint256(hex).withSize(166);
    }

    public static EthUint uint168(String hex) {
        return uint256(hex).withSize(168);
    }

    public static EthUint uint170(String hex) {
        return uint256(hex).withSize(170);
    }

    public static EthUint uint172(String hex) {
        return uint256(hex).withSize(172);
    }

    public static EthUint uint174(String hex) {
        return uint256(hex).withSize(174);
    }

    public static EthUint uint176(String hex) {
        return uint256(hex).withSize(176);
    }

    public static EthUint uint178(String hex) {
        return uint256(hex).withSize(178);
    }

    public static EthUint uint180(String hex) {
        return uint256(hex).withSize(180);
    }

    public static EthUint uint182(String hex) {
        return uint256(hex).withSize(182);
    }

    public static EthUint uint184(String hex) {
        return uint256(hex).withSize(184);
    }

    public static EthUint uint186(String hex) {
        return uint256(hex).withSize(186);
    }

    public static EthUint uint188(String hex) {
        return uint256(hex).withSize(188);
    }

    public static EthUint uint190(String hex) {
        return uint256(hex).withSize(190);
    }

    public static EthUint uint192(String hex) {
        return uint256(hex).withSize(192);
    }

    public static EthUint uint194(String hex) {
        return uint256(hex).withSize(194);
    }

    public static EthUint uint196(String hex) {
        return uint256(hex).withSize(196);
    }

    public static EthUint uint198(String hex) {
        return uint256(hex).withSize(198);
    }

    public static EthUint uint200(String hex) {
        return uint256(hex).withSize(200);
    }

    public static EthUint uint202(String hex) {
        return uint256(hex).withSize(202);
    }

    public static EthUint uint204(String hex) {
        return uint256(hex).withSize(204);
    }

    public static EthUint uint206(String hex) {
        return uint256(hex).withSize(206);
    }

    public static EthUint uint208(String hex) {
        return uint256(hex).withSize(208);
    }

    public static EthUint uint210(String hex) {
        return uint256(hex).withSize(210);
    }

    public static EthUint uint212(String hex) {
        return uint256(hex).withSize(212);
    }

    public static EthUint uint214(String hex) {
        return uint256(hex).withSize(214);
    }

    public static EthUint uint216(String hex) {
        return uint256(hex).withSize(216);
    }

    public static EthUint uint218(String hex) {
        return uint256(hex).withSize(218);
    }

    public static EthUint uint220(String hex) {
        return uint256(hex).withSize(220);
    }

    public static EthUint uint222(String hex) {
        return uint256(hex).withSize(222);
    }

    public static EthUint uint224(String hex) {
        return uint256(hex).withSize(224);
    }

    public static EthUint uint226(String hex) {
        return uint256(hex).withSize(226);
    }

    public static EthUint uint228(String hex) {
        return uint256(hex).withSize(228);
    }

    public static EthUint uint230(String hex) {
        return uint256(hex).withSize(230);
    }

    public static EthUint uint232(String hex) {
        return uint256(hex).withSize(232);
    }

    public static EthUint uint234(String hex) {
        return uint256(hex).withSize(234);
    }

    public static EthUint uint236(String hex) {
        return uint256(hex).withSize(236);
    }

    public static EthUint uint238(String hex) {
        return uint256(hex).withSize(238);
    }

    public static EthUint uint240(String hex) {
        return uint256(hex).withSize(240);
    }

    public static EthUint uint242(String hex) {
        return uint256(hex).withSize(242);
    }

    public static EthUint uint244(String hex) {
        return uint256(hex).withSize(244);
    }

    public static EthUint uint246(String hex) {
        return uint256(hex).withSize(246);
    }

    public static EthUint uint248(String hex) {
        return uint256(hex).withSize(248);
    }

    public static EthUint uint250(String hex) {
        return uint256(hex).withSize(250);
    }

    public static EthUint uint252(String hex) {
        return uint256(hex).withSize(252);
    }

    public static EthUint uint254(String hex) {
        return uint256(hex).withSize(254);
    }

}
