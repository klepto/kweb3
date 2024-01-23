package dev.klepto.kweb3.type;

import dev.klepto.kweb3.util.Hex;
import lombok.With;

import java.math.BigInteger;

import static dev.klepto.kweb3.error.Conditions.require;

/**
 * Represents ethereum <code>int</code> data type.
 *
 * @param size  the size in bits of this <code>int</code>, from 1 to 256
 * @param value the integer value
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public record EthInt(int size, BigInteger value) implements EthNumericType {

    public EthInt {
        require(size >= value.bitLength(), "int{} cannot fit value: {}", size, value);
    }

    /* Solidity style int initializers */
    public static EthInt int256(BigInteger value) {
        return new EthInt(256, value);
    }

    public static EthInt int256(long value) {
        return int256(BigInteger.valueOf(value));
    }

    public static EthInt int256(String hex) {
        require(Hex.isValid(hex), "Malformed hex string: {}", hex);
        return int256(Hex.toBigInteger(hex));
    }

    /* Smaller size solidity int initializers */
    public static EthInt int8(long value) {
        return int256(value).withSize(8);
    }

    public static EthInt int10(long value) {
        return int256(value).withSize(10);
    }

    public static EthInt int12(long value) {
        return int256(value).withSize(12);
    }

    public static EthInt int14(long value) {
        return int256(value).withSize(14);
    }

    public static EthInt int16(long value) {
        return int256(value).withSize(16);
    }

    public static EthInt int18(long value) {
        return int256(value).withSize(18);
    }

    public static EthInt int20(long value) {
        return int256(value).withSize(20);
    }

    public static EthInt int22(long value) {
        return int256(value).withSize(22);
    }

    public static EthInt int24(long value) {
        return int256(value).withSize(24);
    }

    public static EthInt int26(long value) {
        return int256(value).withSize(26);
    }

    public static EthInt int28(long value) {
        return int256(value).withSize(28);
    }

    public static EthInt int30(long value) {
        return int256(value).withSize(30);
    }

    public static EthInt int32(long value) {
        return int256(value).withSize(32);
    }

    public static EthInt int34(long value) {
        return int256(value).withSize(34);
    }

    public static EthInt int36(long value) {
        return int256(value).withSize(36);
    }

    public static EthInt int38(long value) {
        return int256(value).withSize(38);
    }

    public static EthInt int40(long value) {
        return int256(value).withSize(40);
    }

    public static EthInt int42(long value) {
        return int256(value).withSize(42);
    }

    public static EthInt int44(long value) {
        return int256(value).withSize(44);
    }

    public static EthInt int46(long value) {
        return int256(value).withSize(46);
    }

    public static EthInt int48(long value) {
        return int256(value).withSize(48);
    }

    public static EthInt int50(long value) {
        return int256(value).withSize(50);
    }

    public static EthInt int52(long value) {
        return int256(value).withSize(52);
    }

    public static EthInt int54(long value) {
        return int256(value).withSize(54);
    }

    public static EthInt int56(long value) {
        return int256(value).withSize(56);
    }

    public static EthInt int58(long value) {
        return int256(value).withSize(58);
    }

    public static EthInt int60(long value) {
        return int256(value).withSize(60);
    }

    public static EthInt int62(long value) {
        return int256(value).withSize(62);
    }

    public static EthInt int64(long value) {
        return int256(value).withSize(64);
    }

    public static EthInt int66(long value) {
        return int256(value).withSize(66);
    }

    public static EthInt int68(long value) {
        return int256(value).withSize(68);
    }

    public static EthInt int70(long value) {
        return int256(value).withSize(70);
    }

    public static EthInt int72(long value) {
        return int256(value).withSize(72);
    }

    public static EthInt int74(long value) {
        return int256(value).withSize(74);
    }

    public static EthInt int76(long value) {
        return int256(value).withSize(76);
    }

    public static EthInt int78(long value) {
        return int256(value).withSize(78);
    }

    public static EthInt int80(long value) {
        return int256(value).withSize(80);
    }

    public static EthInt int82(long value) {
        return int256(value).withSize(82);
    }

    public static EthInt int84(long value) {
        return int256(value).withSize(84);
    }

    public static EthInt int86(long value) {
        return int256(value).withSize(86);
    }

    public static EthInt int88(long value) {
        return int256(value).withSize(88);
    }

    public static EthInt int90(long value) {
        return int256(value).withSize(90);
    }

    public static EthInt int92(long value) {
        return int256(value).withSize(92);
    }

    public static EthInt int94(long value) {
        return int256(value).withSize(94);
    }

    public static EthInt int96(long value) {
        return int256(value).withSize(96);
    }

    public static EthInt int98(long value) {
        return int256(value).withSize(98);
    }

    public static EthInt int100(long value) {
        return int256(value).withSize(100);
    }

    public static EthInt int102(long value) {
        return int256(value).withSize(102);
    }

    public static EthInt int104(long value) {
        return int256(value).withSize(104);
    }

    public static EthInt int106(long value) {
        return int256(value).withSize(106);
    }

    public static EthInt int108(long value) {
        return int256(value).withSize(108);
    }

    public static EthInt int110(long value) {
        return int256(value).withSize(110);
    }

    public static EthInt int112(long value) {
        return int256(value).withSize(112);
    }

    public static EthInt int114(long value) {
        return int256(value).withSize(114);
    }

    public static EthInt int116(long value) {
        return int256(value).withSize(116);
    }

    public static EthInt int118(long value) {
        return int256(value).withSize(118);
    }

    public static EthInt int120(long value) {
        return int256(value).withSize(120);
    }

    public static EthInt int122(long value) {
        return int256(value).withSize(122);
    }

    public static EthInt int124(long value) {
        return int256(value).withSize(124);
    }

    public static EthInt int126(long value) {
        return int256(value).withSize(126);
    }

    public static EthInt int128(long value) {
        return int256(value).withSize(128);
    }

    public static EthInt int130(long value) {
        return int256(value).withSize(130);
    }

    public static EthInt int132(long value) {
        return int256(value).withSize(132);
    }

    public static EthInt int134(long value) {
        return int256(value).withSize(134);
    }

    public static EthInt int136(long value) {
        return int256(value).withSize(136);
    }

    public static EthInt int138(long value) {
        return int256(value).withSize(138);
    }

    public static EthInt int140(long value) {
        return int256(value).withSize(140);
    }

    public static EthInt int142(long value) {
        return int256(value).withSize(142);
    }

    public static EthInt int144(long value) {
        return int256(value).withSize(144);
    }

    public static EthInt int146(long value) {
        return int256(value).withSize(146);
    }

    public static EthInt int148(long value) {
        return int256(value).withSize(148);
    }

    public static EthInt int150(long value) {
        return int256(value).withSize(150);
    }

    public static EthInt int152(long value) {
        return int256(value).withSize(152);
    }

    public static EthInt int154(long value) {
        return int256(value).withSize(154);
    }

    public static EthInt int156(long value) {
        return int256(value).withSize(156);
    }

    public static EthInt int158(long value) {
        return int256(value).withSize(158);
    }

    public static EthInt int160(long value) {
        return int256(value).withSize(160);
    }

    public static EthInt int162(long value) {
        return int256(value).withSize(162);
    }

    public static EthInt int164(long value) {
        return int256(value).withSize(164);
    }

    public static EthInt int166(long value) {
        return int256(value).withSize(166);
    }

    public static EthInt int168(long value) {
        return int256(value).withSize(168);
    }

    public static EthInt int170(long value) {
        return int256(value).withSize(170);
    }

    public static EthInt int172(long value) {
        return int256(value).withSize(172);
    }

    public static EthInt int174(long value) {
        return int256(value).withSize(174);
    }

    public static EthInt int176(long value) {
        return int256(value).withSize(176);
    }

    public static EthInt int178(long value) {
        return int256(value).withSize(178);
    }

    public static EthInt int180(long value) {
        return int256(value).withSize(180);
    }

    public static EthInt int182(long value) {
        return int256(value).withSize(182);
    }

    public static EthInt int184(long value) {
        return int256(value).withSize(184);
    }

    public static EthInt int186(long value) {
        return int256(value).withSize(186);
    }

    public static EthInt int188(long value) {
        return int256(value).withSize(188);
    }

    public static EthInt int190(long value) {
        return int256(value).withSize(190);
    }

    public static EthInt int192(long value) {
        return int256(value).withSize(192);
    }

    public static EthInt int194(long value) {
        return int256(value).withSize(194);
    }

    public static EthInt int196(long value) {
        return int256(value).withSize(196);
    }

    public static EthInt int198(long value) {
        return int256(value).withSize(198);
    }

    public static EthInt int200(long value) {
        return int256(value).withSize(200);
    }

    public static EthInt int202(long value) {
        return int256(value).withSize(202);
    }

    public static EthInt int204(long value) {
        return int256(value).withSize(204);
    }

    public static EthInt int206(long value) {
        return int256(value).withSize(206);
    }

    public static EthInt int208(long value) {
        return int256(value).withSize(208);
    }

    public static EthInt int210(long value) {
        return int256(value).withSize(210);
    }

    public static EthInt int212(long value) {
        return int256(value).withSize(212);
    }

    public static EthInt int214(long value) {
        return int256(value).withSize(214);
    }

    public static EthInt int216(long value) {
        return int256(value).withSize(216);
    }

    public static EthInt int218(long value) {
        return int256(value).withSize(218);
    }

    public static EthInt int220(long value) {
        return int256(value).withSize(220);
    }

    public static EthInt int222(long value) {
        return int256(value).withSize(222);
    }

    public static EthInt int224(long value) {
        return int256(value).withSize(224);
    }

    public static EthInt int226(long value) {
        return int256(value).withSize(226);
    }

    public static EthInt int228(long value) {
        return int256(value).withSize(228);
    }

    public static EthInt int230(long value) {
        return int256(value).withSize(230);
    }

    public static EthInt int232(long value) {
        return int256(value).withSize(232);
    }

    public static EthInt int234(long value) {
        return int256(value).withSize(234);
    }

    public static EthInt int236(long value) {
        return int256(value).withSize(236);
    }

    public static EthInt int238(long value) {
        return int256(value).withSize(238);
    }

    public static EthInt int240(long value) {
        return int256(value).withSize(240);
    }

    public static EthInt int242(long value) {
        return int256(value).withSize(242);
    }

    public static EthInt int244(long value) {
        return int256(value).withSize(244);
    }

    public static EthInt int246(long value) {
        return int256(value).withSize(246);
    }

    public static EthInt int248(long value) {
        return int256(value).withSize(248);
    }

    public static EthInt int250(long value) {
        return int256(value).withSize(250);
    }

    public static EthInt int252(long value) {
        return int256(value).withSize(252);
    }

    public static EthInt int254(long value) {
        return int256(value).withSize(254);
    }

    public static EthInt int8(String hex) {
        return int256(hex).withSize(8);
    }

    public static EthInt int10(String hex) {
        return int256(hex).withSize(10);
    }

    public static EthInt int12(String hex) {
        return int256(hex).withSize(12);
    }

    public static EthInt int14(String hex) {
        return int256(hex).withSize(14);
    }

    public static EthInt int16(String hex) {
        return int256(hex).withSize(16);
    }

    public static EthInt int18(String hex) {
        return int256(hex).withSize(18);
    }

    public static EthInt int20(String hex) {
        return int256(hex).withSize(20);
    }

    public static EthInt int22(String hex) {
        return int256(hex).withSize(22);
    }

    public static EthInt int24(String hex) {
        return int256(hex).withSize(24);
    }

    public static EthInt int26(String hex) {
        return int256(hex).withSize(26);
    }

    public static EthInt int28(String hex) {
        return int256(hex).withSize(28);
    }

    public static EthInt int30(String hex) {
        return int256(hex).withSize(30);
    }

    public static EthInt int32(String hex) {
        return int256(hex).withSize(32);
    }

    public static EthInt int34(String hex) {
        return int256(hex).withSize(34);
    }

    public static EthInt int36(String hex) {
        return int256(hex).withSize(36);
    }

    public static EthInt int38(String hex) {
        return int256(hex).withSize(38);
    }

    public static EthInt int40(String hex) {
        return int256(hex).withSize(40);
    }

    public static EthInt int42(String hex) {
        return int256(hex).withSize(42);
    }

    public static EthInt int44(String hex) {
        return int256(hex).withSize(44);
    }

    public static EthInt int46(String hex) {
        return int256(hex).withSize(46);
    }

    public static EthInt int48(String hex) {
        return int256(hex).withSize(48);
    }

    public static EthInt int50(String hex) {
        return int256(hex).withSize(50);
    }

    public static EthInt int52(String hex) {
        return int256(hex).withSize(52);
    }

    public static EthInt int54(String hex) {
        return int256(hex).withSize(54);
    }

    public static EthInt int56(String hex) {
        return int256(hex).withSize(56);
    }

    public static EthInt int58(String hex) {
        return int256(hex).withSize(58);
    }

    public static EthInt int60(String hex) {
        return int256(hex).withSize(60);
    }

    public static EthInt int62(String hex) {
        return int256(hex).withSize(62);
    }

    public static EthInt int64(String hex) {
        return int256(hex).withSize(64);
    }

    public static EthInt int66(String hex) {
        return int256(hex).withSize(66);
    }

    public static EthInt int68(String hex) {
        return int256(hex).withSize(68);
    }

    public static EthInt int70(String hex) {
        return int256(hex).withSize(70);
    }

    public static EthInt int72(String hex) {
        return int256(hex).withSize(72);
    }

    public static EthInt int74(String hex) {
        return int256(hex).withSize(74);
    }

    public static EthInt int76(String hex) {
        return int256(hex).withSize(76);
    }

    public static EthInt int78(String hex) {
        return int256(hex).withSize(78);
    }

    public static EthInt int80(String hex) {
        return int256(hex).withSize(80);
    }

    public static EthInt int82(String hex) {
        return int256(hex).withSize(82);
    }

    public static EthInt int84(String hex) {
        return int256(hex).withSize(84);
    }

    public static EthInt int86(String hex) {
        return int256(hex).withSize(86);
    }

    public static EthInt int88(String hex) {
        return int256(hex).withSize(88);
    }

    public static EthInt int90(String hex) {
        return int256(hex).withSize(90);
    }

    public static EthInt int92(String hex) {
        return int256(hex).withSize(92);
    }

    public static EthInt int94(String hex) {
        return int256(hex).withSize(94);
    }

    public static EthInt int96(String hex) {
        return int256(hex).withSize(96);
    }

    public static EthInt int98(String hex) {
        return int256(hex).withSize(98);
    }

    public static EthInt int100(String hex) {
        return int256(hex).withSize(100);
    }

    public static EthInt int102(String hex) {
        return int256(hex).withSize(102);
    }

    public static EthInt int104(String hex) {
        return int256(hex).withSize(104);
    }

    public static EthInt int106(String hex) {
        return int256(hex).withSize(106);
    }

    public static EthInt int108(String hex) {
        return int256(hex).withSize(108);
    }

    public static EthInt int110(String hex) {
        return int256(hex).withSize(110);
    }

    public static EthInt int112(String hex) {
        return int256(hex).withSize(112);
    }

    public static EthInt int114(String hex) {
        return int256(hex).withSize(114);
    }

    public static EthInt int116(String hex) {
        return int256(hex).withSize(116);
    }

    public static EthInt int118(String hex) {
        return int256(hex).withSize(118);
    }

    public static EthInt int120(String hex) {
        return int256(hex).withSize(120);
    }

    public static EthInt int122(String hex) {
        return int256(hex).withSize(122);
    }

    public static EthInt int124(String hex) {
        return int256(hex).withSize(124);
    }

    public static EthInt int126(String hex) {
        return int256(hex).withSize(126);
    }

    public static EthInt int128(String hex) {
        return int256(hex).withSize(128);
    }

    public static EthInt int130(String hex) {
        return int256(hex).withSize(130);
    }

    public static EthInt int132(String hex) {
        return int256(hex).withSize(132);
    }

    public static EthInt int134(String hex) {
        return int256(hex).withSize(134);
    }

    public static EthInt int136(String hex) {
        return int256(hex).withSize(136);
    }

    public static EthInt int138(String hex) {
        return int256(hex).withSize(138);
    }

    public static EthInt int140(String hex) {
        return int256(hex).withSize(140);
    }

    public static EthInt int142(String hex) {
        return int256(hex).withSize(142);
    }

    public static EthInt int144(String hex) {
        return int256(hex).withSize(144);
    }

    public static EthInt int146(String hex) {
        return int256(hex).withSize(146);
    }

    public static EthInt int148(String hex) {
        return int256(hex).withSize(148);
    }

    public static EthInt int150(String hex) {
        return int256(hex).withSize(150);
    }

    public static EthInt int152(String hex) {
        return int256(hex).withSize(152);
    }

    public static EthInt int154(String hex) {
        return int256(hex).withSize(154);
    }

    public static EthInt int156(String hex) {
        return int256(hex).withSize(156);
    }

    public static EthInt int158(String hex) {
        return int256(hex).withSize(158);
    }

    public static EthInt int160(String hex) {
        return int256(hex).withSize(160);
    }

    public static EthInt int162(String hex) {
        return int256(hex).withSize(162);
    }

    public static EthInt int164(String hex) {
        return int256(hex).withSize(164);
    }

    public static EthInt int166(String hex) {
        return int256(hex).withSize(166);
    }

    public static EthInt int168(String hex) {
        return int256(hex).withSize(168);
    }

    public static EthInt int170(String hex) {
        return int256(hex).withSize(170);
    }

    public static EthInt int172(String hex) {
        return int256(hex).withSize(172);
    }

    public static EthInt int174(String hex) {
        return int256(hex).withSize(174);
    }

    public static EthInt int176(String hex) {
        return int256(hex).withSize(176);
    }

    public static EthInt int178(String hex) {
        return int256(hex).withSize(178);
    }

    public static EthInt int180(String hex) {
        return int256(hex).withSize(180);
    }

    public static EthInt int182(String hex) {
        return int256(hex).withSize(182);
    }

    public static EthInt int184(String hex) {
        return int256(hex).withSize(184);
    }

    public static EthInt int186(String hex) {
        return int256(hex).withSize(186);
    }

    public static EthInt int188(String hex) {
        return int256(hex).withSize(188);
    }

    public static EthInt int190(String hex) {
        return int256(hex).withSize(190);
    }

    public static EthInt int192(String hex) {
        return int256(hex).withSize(192);
    }

    public static EthInt int194(String hex) {
        return int256(hex).withSize(194);
    }

    public static EthInt int196(String hex) {
        return int256(hex).withSize(196);
    }

    public static EthInt int198(String hex) {
        return int256(hex).withSize(198);
    }

    public static EthInt int200(String hex) {
        return int256(hex).withSize(200);
    }

    public static EthInt int202(String hex) {
        return int256(hex).withSize(202);
    }

    public static EthInt int204(String hex) {
        return int256(hex).withSize(204);
    }

    public static EthInt int206(String hex) {
        return int256(hex).withSize(206);
    }

    public static EthInt int208(String hex) {
        return int256(hex).withSize(208);
    }

    public static EthInt int210(String hex) {
        return int256(hex).withSize(210);
    }

    public static EthInt int212(String hex) {
        return int256(hex).withSize(212);
    }

    public static EthInt int214(String hex) {
        return int256(hex).withSize(214);
    }

    public static EthInt int216(String hex) {
        return int256(hex).withSize(216);
    }

    public static EthInt int218(String hex) {
        return int256(hex).withSize(218);
    }

    public static EthInt int220(String hex) {
        return int256(hex).withSize(220);
    }

    public static EthInt int222(String hex) {
        return int256(hex).withSize(222);
    }

    public static EthInt int224(String hex) {
        return int256(hex).withSize(224);
    }

    public static EthInt int226(String hex) {
        return int256(hex).withSize(226);
    }

    public static EthInt int228(String hex) {
        return int256(hex).withSize(228);
    }

    public static EthInt int230(String hex) {
        return int256(hex).withSize(230);
    }

    public static EthInt int232(String hex) {
        return int256(hex).withSize(232);
    }

    public static EthInt int234(String hex) {
        return int256(hex).withSize(234);
    }

    public static EthInt int236(String hex) {
        return int256(hex).withSize(236);
    }

    public static EthInt int238(String hex) {
        return int256(hex).withSize(238);
    }

    public static EthInt int240(String hex) {
        return int256(hex).withSize(240);
    }

    public static EthInt int242(String hex) {
        return int256(hex).withSize(242);
    }

    public static EthInt int244(String hex) {
        return int256(hex).withSize(244);
    }

    public static EthInt int246(String hex) {
        return int256(hex).withSize(246);
    }

    public static EthInt int248(String hex) {
        return int256(hex).withSize(248);
    }

    public static EthInt int250(String hex) {
        return int256(hex).withSize(250);
    }

    public static EthInt int252(String hex) {
        return int256(hex).withSize(252);
    }

    public static EthInt int254(String hex) {
        return int256(hex).withSize(254);
    }

}
