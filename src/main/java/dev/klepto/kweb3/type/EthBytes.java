package dev.klepto.kweb3.type;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Bytes;
import dev.klepto.kweb3.util.Hex;
import lombok.With;
import lombok.val;

import static dev.klepto.kweb3.error.Conditions.require;

/**
 * Represents ethereum <code>bytes</code> data type.
 *
 * @param size  the size in bytes, from 1 to 32, -1 to indicate dynamic size
 * @param value the byte array in a form of immutable list
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public record EthBytes(int size, ImmutableList<Byte> value) implements EthSizedType {

    public EthBytes {
        require(
                size == -1 || size == value.size(),
                "bytes{} cannot contain {} bytes",
                size, value.size()
        );
    }

    @Override
    public String toString() {
        val sizeString = size > 0 ? size : "";
        return "bytes" + sizeString + "(" + Hex.toHex(toByteArray()) + ")";
    }


    /**
     * Converts this value to mutable <code>byte</code> array.
     *
     * @return a mutable array of bytes
     */
    public byte[] toByteArray() {
        return Bytes.toArray(value);
    }

    /* Solidity style bytes initializers */
    public static EthBytes bytes(byte[] value) {
        return new EthBytes(-1, ImmutableList.copyOf(Bytes.asList(value)));
    }

    public static EthBytes bytes1(byte[] value) {
        return bytes(value).withSize(1);
    }

    public static EthBytes bytes2(byte[] value) {
        return bytes(value).withSize(2);
    }

    public static EthBytes bytes3(byte[] value) {
        return bytes(value).withSize(3);
    }

    public static EthBytes bytes4(byte[] value) {
        return bytes(value).withSize(4);
    }

    public static EthBytes bytes5(byte[] value) {
        return bytes(value).withSize(5);
    }

    public static EthBytes bytes6(byte[] value) {
        return bytes(value).withSize(6);
    }

    public static EthBytes bytes7(byte[] value) {
        return bytes(value).withSize(7);
    }

    public static EthBytes bytes8(byte[] value) {
        return bytes(value).withSize(8);
    }

    public static EthBytes bytes9(byte[] value) {
        return bytes(value).withSize(9);
    }

    public static EthBytes bytes10(byte[] value) {
        return bytes(value).withSize(10);
    }

    public static EthBytes bytes11(byte[] value) {
        return bytes(value).withSize(11);
    }

    public static EthBytes bytes12(byte[] value) {
        return bytes(value).withSize(12);
    }

    public static EthBytes bytes13(byte[] value) {
        return bytes(value).withSize(13);
    }

    public static EthBytes bytes14(byte[] value) {
        return bytes(value).withSize(14);
    }

    public static EthBytes bytes15(byte[] value) {
        return bytes(value).withSize(15);
    }

    public static EthBytes bytes16(byte[] value) {
        return bytes(value).withSize(16);
    }

    public static EthBytes bytes17(byte[] value) {
        return bytes(value).withSize(17);
    }

    public static EthBytes bytes18(byte[] value) {
        return bytes(value).withSize(18);
    }

    public static EthBytes bytes19(byte[] value) {
        return bytes(value).withSize(19);
    }

    public static EthBytes bytes20(byte[] value) {
        return bytes(value).withSize(20);
    }

    public static EthBytes bytes21(byte[] value) {
        return bytes(value).withSize(21);
    }

    public static EthBytes bytes22(byte[] value) {
        return bytes(value).withSize(22);
    }

    public static EthBytes bytes23(byte[] value) {
        return bytes(value).withSize(23);
    }

    public static EthBytes bytes24(byte[] value) {
        return bytes(value).withSize(24);
    }

    public static EthBytes bytes25(byte[] value) {
        return bytes(value).withSize(25);
    }

    public static EthBytes bytes26(byte[] value) {
        return bytes(value).withSize(26);
    }

    public static EthBytes bytes27(byte[] value) {
        return bytes(value).withSize(27);
    }

    public static EthBytes bytes28(byte[] value) {
        return bytes(value).withSize(28);
    }

    public static EthBytes bytes29(byte[] value) {
        return bytes(value).withSize(29);
    }

    public static EthBytes bytes30(byte[] value) {
        return bytes(value).withSize(30);
    }

    public static EthBytes bytes31(byte[] value) {
        return bytes(value).withSize(31);
    }

    public static EthBytes bytes32(byte[] value) {
        return bytes(value).withSize(32);
    }

    public static EthBytes bytes(String hex) {
        require(Hex.isValid(hex), "Malformed hex string: {}", hex);
        return bytes(Hex.toByteArray(hex));
    }

    public static EthBytes bytes1(String hex) {
        return bytes(hex).withSize(1);
    }

    public static EthBytes bytes2(String hex) {
        return bytes(hex).withSize(2);
    }

    public static EthBytes bytes3(String hex) {
        return bytes(hex).withSize(3);
    }

    public static EthBytes bytes4(String hex) {
        return bytes(hex).withSize(4);
    }

    public static EthBytes bytes5(String hex) {
        return bytes(hex).withSize(5);
    }

    public static EthBytes bytes6(String hex) {
        return bytes(hex).withSize(6);
    }

    public static EthBytes bytes7(String hex) {
        return bytes(hex).withSize(7);
    }

    public static EthBytes bytes8(String hex) {
        return bytes(hex).withSize(8);
    }

    public static EthBytes bytes9(String hex) {
        return bytes(hex).withSize(9);
    }

    public static EthBytes bytes10(String hex) {
        return bytes(hex).withSize(10);
    }

    public static EthBytes bytes11(String hex) {
        return bytes(hex).withSize(11);
    }

    public static EthBytes bytes12(String hex) {
        return bytes(hex).withSize(12);
    }

    public static EthBytes bytes13(String hex) {
        return bytes(hex).withSize(13);
    }

    public static EthBytes bytes14(String hex) {
        return bytes(hex).withSize(14);
    }

    public static EthBytes bytes15(String hex) {
        return bytes(hex).withSize(15);
    }

    public static EthBytes bytes16(String hex) {
        return bytes(hex).withSize(16);
    }

    public static EthBytes bytes17(String hex) {
        return bytes(hex).withSize(17);
    }

    public static EthBytes bytes18(String hex) {
        return bytes(hex).withSize(18);
    }

    public static EthBytes bytes19(String hex) {
        return bytes(hex).withSize(19);
    }

    public static EthBytes bytes20(String hex) {
        return bytes(hex).withSize(20);
    }

    public static EthBytes bytes21(String hex) {
        return bytes(hex).withSize(21);
    }

    public static EthBytes bytes22(String hex) {
        return bytes(hex).withSize(22);
    }

    public static EthBytes bytes23(String hex) {
        return bytes(hex).withSize(23);
    }

    public static EthBytes bytes24(String hex) {
        return bytes(hex).withSize(24);
    }

    public static EthBytes bytes25(String hex) {
        return bytes(hex).withSize(25);
    }

    public static EthBytes bytes26(String hex) {
        return bytes(hex).withSize(26);
    }

    public static EthBytes bytes27(String hex) {
        return bytes(hex).withSize(27);
    }

    public static EthBytes bytes28(String hex) {
        return bytes(hex).withSize(28);
    }

    public static EthBytes bytes29(String hex) {
        return bytes(hex).withSize(29);
    }

    public static EthBytes bytes30(String hex) {
        return bytes(hex).withSize(30);
    }

    public static EthBytes bytes31(String hex) {
        return bytes(hex).withSize(31);
    }

    public static EthBytes bytes32(String hex) {
        return bytes(hex).withSize(32);
    }

}
