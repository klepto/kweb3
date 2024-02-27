package dev.klepto.kweb3.core.type;

import dev.klepto.kweb3.core.util.Hex;
import lombok.With;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * Represents ethereum <code>int</code> data type.
 *
 * @param size  the size in bits of this <code>int</code>, from 1 to 256
 * @param value the integer value
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public record EthInt(int size, BigInteger value) implements EthNumericType, EthSizedType {

    /**
     * Zero int constant.
     */
    public static final EthInt ZERO = int256(0);

    public EthInt {
        require(size >= value.bitLength(), "int{} cannot fit value: {}", size, value);
    }

    @Override
    public String toString() {
        return "int" + size + "(" + value + ")";
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /* Solidity style int initializers */
    @NotNull
    public static EthInt int256(@NotNull BigInteger value) {
        return new EthInt(256, value);
    }

    @NotNull
    public static EthInt int256(long value) {
        return int256(BigInteger.valueOf(value));
    }

    @NotNull
    public static EthInt int256(@NotNull String hex) {
        require(Hex.isValid(hex), "Malformed hex string: {}", hex);
        return int256(Hex.toBigInteger(hex));
    }

    /* Smaller size solidity int initializers */
    @NotNull
    public static EthInt int8(long value) {
        return int256(value).withSize(8);
    }

    @NotNull
    public static EthInt int10(long value) {
        return int256(value).withSize(10);
    }

    @NotNull
    public static EthInt int12(long value) {
        return int256(value).withSize(12);
    }

    @NotNull
    public static EthInt int14(long value) {
        return int256(value).withSize(14);
    }

    @NotNull
    public static EthInt int16(long value) {
        return int256(value).withSize(16);
    }

    @NotNull
    public static EthInt int18(long value) {
        return int256(value).withSize(18);
    }

    @NotNull
    public static EthInt int20(long value) {
        return int256(value).withSize(20);
    }

    @NotNull
    public static EthInt int22(long value) {
        return int256(value).withSize(22);
    }

    @NotNull
    public static EthInt int24(long value) {
        return int256(value).withSize(24);
    }

    @NotNull
    public static EthInt int26(long value) {
        return int256(value).withSize(26);
    }

    @NotNull
    public static EthInt int28(long value) {
        return int256(value).withSize(28);
    }

    @NotNull
    public static EthInt int30(long value) {
        return int256(value).withSize(30);
    }

    @NotNull
    public static EthInt int32(long value) {
        return int256(value).withSize(32);
    }

    @NotNull
    public static EthInt int34(long value) {
        return int256(value).withSize(34);
    }

    @NotNull
    public static EthInt int36(long value) {
        return int256(value).withSize(36);
    }

    @NotNull
    public static EthInt int38(long value) {
        return int256(value).withSize(38);
    }

    @NotNull
    public static EthInt int40(long value) {
        return int256(value).withSize(40);
    }

    @NotNull
    public static EthInt int42(long value) {
        return int256(value).withSize(42);
    }

    @NotNull
    public static EthInt int44(long value) {
        return int256(value).withSize(44);
    }

    @NotNull
    public static EthInt int46(long value) {
        return int256(value).withSize(46);
    }

    @NotNull
    public static EthInt int48(long value) {
        return int256(value).withSize(48);
    }

    @NotNull
    public static EthInt int50(long value) {
        return int256(value).withSize(50);
    }

    @NotNull
    public static EthInt int52(long value) {
        return int256(value).withSize(52);
    }

    @NotNull
    public static EthInt int54(long value) {
        return int256(value).withSize(54);
    }

    @NotNull
    public static EthInt int56(long value) {
        return int256(value).withSize(56);
    }

    @NotNull
    public static EthInt int58(long value) {
        return int256(value).withSize(58);
    }

    @NotNull
    public static EthInt int60(long value) {
        return int256(value).withSize(60);
    }

    @NotNull
    public static EthInt int62(long value) {
        return int256(value).withSize(62);
    }

    @NotNull
    public static EthInt int64(long value) {
        return int256(value).withSize(64);
    }

    @NotNull
    public static EthInt int66(long value) {
        return int256(value).withSize(66);
    }

    @NotNull
    public static EthInt int68(long value) {
        return int256(value).withSize(68);
    }

    @NotNull
    public static EthInt int70(long value) {
        return int256(value).withSize(70);
    }

    @NotNull
    public static EthInt int72(long value) {
        return int256(value).withSize(72);
    }

    @NotNull
    public static EthInt int74(long value) {
        return int256(value).withSize(74);
    }

    @NotNull
    public static EthInt int76(long value) {
        return int256(value).withSize(76);
    }

    @NotNull
    public static EthInt int78(long value) {
        return int256(value).withSize(78);
    }

    @NotNull
    public static EthInt int80(long value) {
        return int256(value).withSize(80);
    }

    @NotNull
    public static EthInt int82(long value) {
        return int256(value).withSize(82);
    }

    @NotNull
    public static EthInt int84(long value) {
        return int256(value).withSize(84);
    }

    @NotNull
    public static EthInt int86(long value) {
        return int256(value).withSize(86);
    }

    @NotNull
    public static EthInt int88(long value) {
        return int256(value).withSize(88);
    }

    @NotNull
    public static EthInt int90(long value) {
        return int256(value).withSize(90);
    }

    @NotNull
    public static EthInt int92(long value) {
        return int256(value).withSize(92);
    }

    @NotNull
    public static EthInt int94(long value) {
        return int256(value).withSize(94);
    }

    @NotNull
    public static EthInt int96(long value) {
        return int256(value).withSize(96);
    }

    @NotNull
    public static EthInt int98(long value) {
        return int256(value).withSize(98);
    }

    @NotNull
    public static EthInt int100(long value) {
        return int256(value).withSize(100);
    }

    @NotNull
    public static EthInt int102(long value) {
        return int256(value).withSize(102);
    }

    @NotNull
    public static EthInt int104(long value) {
        return int256(value).withSize(104);
    }

    @NotNull
    public static EthInt int106(long value) {
        return int256(value).withSize(106);
    }

    @NotNull
    public static EthInt int108(long value) {
        return int256(value).withSize(108);
    }

    @NotNull
    public static EthInt int110(long value) {
        return int256(value).withSize(110);
    }

    @NotNull
    public static EthInt int112(long value) {
        return int256(value).withSize(112);
    }

    @NotNull
    public static EthInt int114(long value) {
        return int256(value).withSize(114);
    }

    @NotNull
    public static EthInt int116(long value) {
        return int256(value).withSize(116);
    }

    @NotNull
    public static EthInt int118(long value) {
        return int256(value).withSize(118);
    }

    @NotNull
    public static EthInt int120(long value) {
        return int256(value).withSize(120);
    }

    @NotNull
    public static EthInt int122(long value) {
        return int256(value).withSize(122);
    }

    @NotNull
    public static EthInt int124(long value) {
        return int256(value).withSize(124);
    }

    @NotNull
    public static EthInt int126(long value) {
        return int256(value).withSize(126);
    }

    @NotNull
    public static EthInt int128(long value) {
        return int256(value).withSize(128);
    }

    @NotNull
    public static EthInt int130(long value) {
        return int256(value).withSize(130);
    }

    @NotNull
    public static EthInt int132(long value) {
        return int256(value).withSize(132);
    }

    @NotNull
    public static EthInt int134(long value) {
        return int256(value).withSize(134);
    }

    @NotNull
    public static EthInt int136(long value) {
        return int256(value).withSize(136);
    }

    @NotNull
    public static EthInt int138(long value) {
        return int256(value).withSize(138);
    }

    @NotNull
    public static EthInt int140(long value) {
        return int256(value).withSize(140);
    }

    @NotNull
    public static EthInt int142(long value) {
        return int256(value).withSize(142);
    }

    @NotNull
    public static EthInt int144(long value) {
        return int256(value).withSize(144);
    }

    @NotNull
    public static EthInt int146(long value) {
        return int256(value).withSize(146);
    }

    @NotNull
    public static EthInt int148(long value) {
        return int256(value).withSize(148);
    }

    @NotNull
    public static EthInt int150(long value) {
        return int256(value).withSize(150);
    }

    @NotNull
    public static EthInt int152(long value) {
        return int256(value).withSize(152);
    }

    @NotNull
    public static EthInt int154(long value) {
        return int256(value).withSize(154);
    }

    @NotNull
    public static EthInt int156(long value) {
        return int256(value).withSize(156);
    }

    @NotNull
    public static EthInt int158(long value) {
        return int256(value).withSize(158);
    }

    @NotNull
    public static EthInt int160(long value) {
        return int256(value).withSize(160);
    }

    @NotNull
    public static EthInt int162(long value) {
        return int256(value).withSize(162);
    }

    @NotNull
    public static EthInt int164(long value) {
        return int256(value).withSize(164);
    }

    @NotNull
    public static EthInt int166(long value) {
        return int256(value).withSize(166);
    }

    @NotNull
    public static EthInt int168(long value) {
        return int256(value).withSize(168);
    }

    @NotNull
    public static EthInt int170(long value) {
        return int256(value).withSize(170);
    }

    @NotNull
    public static EthInt int172(long value) {
        return int256(value).withSize(172);
    }

    @NotNull
    public static EthInt int174(long value) {
        return int256(value).withSize(174);
    }

    @NotNull
    public static EthInt int176(long value) {
        return int256(value).withSize(176);
    }

    @NotNull
    public static EthInt int178(long value) {
        return int256(value).withSize(178);
    }

    @NotNull
    public static EthInt int180(long value) {
        return int256(value).withSize(180);
    }

    @NotNull
    public static EthInt int182(long value) {
        return int256(value).withSize(182);
    }

    @NotNull
    public static EthInt int184(long value) {
        return int256(value).withSize(184);
    }

    @NotNull
    public static EthInt int186(long value) {
        return int256(value).withSize(186);
    }

    @NotNull
    public static EthInt int188(long value) {
        return int256(value).withSize(188);
    }

    @NotNull
    public static EthInt int190(long value) {
        return int256(value).withSize(190);
    }

    @NotNull
    public static EthInt int192(long value) {
        return int256(value).withSize(192);
    }

    @NotNull
    public static EthInt int194(long value) {
        return int256(value).withSize(194);
    }

    @NotNull
    public static EthInt int196(long value) {
        return int256(value).withSize(196);
    }

    @NotNull
    public static EthInt int198(long value) {
        return int256(value).withSize(198);
    }

    @NotNull
    public static EthInt int200(long value) {
        return int256(value).withSize(200);
    }

    @NotNull
    public static EthInt int202(long value) {
        return int256(value).withSize(202);
    }

    @NotNull
    public static EthInt int204(long value) {
        return int256(value).withSize(204);
    }

    @NotNull
    public static EthInt int206(long value) {
        return int256(value).withSize(206);
    }

    @NotNull
    public static EthInt int208(long value) {
        return int256(value).withSize(208);
    }

    @NotNull
    public static EthInt int210(long value) {
        return int256(value).withSize(210);
    }

    @NotNull
    public static EthInt int212(long value) {
        return int256(value).withSize(212);
    }

    @NotNull
    public static EthInt int214(long value) {
        return int256(value).withSize(214);
    }

    @NotNull
    public static EthInt int216(long value) {
        return int256(value).withSize(216);
    }

    @NotNull
    public static EthInt int218(long value) {
        return int256(value).withSize(218);
    }

    @NotNull
    public static EthInt int220(long value) {
        return int256(value).withSize(220);
    }

    @NotNull
    public static EthInt int222(long value) {
        return int256(value).withSize(222);
    }

    @NotNull
    public static EthInt int224(long value) {
        return int256(value).withSize(224);
    }

    @NotNull
    public static EthInt int226(long value) {
        return int256(value).withSize(226);
    }

    @NotNull
    public static EthInt int228(long value) {
        return int256(value).withSize(228);
    }

    @NotNull
    public static EthInt int230(long value) {
        return int256(value).withSize(230);
    }

    @NotNull
    public static EthInt int232(long value) {
        return int256(value).withSize(232);
    }

    @NotNull
    public static EthInt int234(long value) {
        return int256(value).withSize(234);
    }

    @NotNull
    public static EthInt int236(long value) {
        return int256(value).withSize(236);
    }

    @NotNull
    public static EthInt int238(long value) {
        return int256(value).withSize(238);
    }

    @NotNull
    public static EthInt int240(long value) {
        return int256(value).withSize(240);
    }

    @NotNull
    public static EthInt int242(long value) {
        return int256(value).withSize(242);
    }

    @NotNull
    public static EthInt int244(long value) {
        return int256(value).withSize(244);
    }

    @NotNull
    public static EthInt int246(long value) {
        return int256(value).withSize(246);
    }

    @NotNull
    public static EthInt int248(long value) {
        return int256(value).withSize(248);
    }

    @NotNull
    public static EthInt int250(long value) {
        return int256(value).withSize(250);
    }

    @NotNull
    public static EthInt int252(long value) {
        return int256(value).withSize(252);
    }

    @NotNull
    public static EthInt int254(long value) {
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
