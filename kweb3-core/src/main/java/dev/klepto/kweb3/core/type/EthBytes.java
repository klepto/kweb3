package dev.klepto.kweb3.core.type;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Bytes;
import dev.klepto.kweb3.core.util.Hex;
import lombok.With;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * Container for <code>ethereum bytes</code> value.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public class EthBytes implements EthValue, EthSizedValue {

    /**
     * Empty <code>ethereum bytes</code> constant.
     */
    public static final EthBytes EMPTY = bytes(new byte[0]);

    /**
     * Constant indicating dynamic byte size.
     */
    public static final int DYNAMIC_SIZE = -1;

    @With
    private final int size;

    @NotNull
    private final ImmutableList<Byte> value;

    /**
     * Constructs new <code>ethereum bytes</code> with the specified size and value.
     *
     * @param size  the size of the bytes, <code>-1</code> indicates dynamic size, any other positive value indicates a
     *              fixed size bytes
     * @param value the immutable list containing bytes of the bytes
     */
    public EthBytes(int size, @NotNull ImmutableList<Byte> value) {
        require(
                size == DYNAMIC_SIZE || size >= value.size(),
                "bytes{} cannot contain {} bytes",
                size, value.size()
        );
        this.size = size;
        this.value = value;
    }

    /**
     * Creates a new <code>ethereum bytes</code> container with the specified size. If specified <code>size</code> is
     * larger than the current size, the new bytes will be padded with zeros.
     *
     * @param size the size of the new bytes
     * @return a new <code>ethereum bytes</code> container with the specified size
     */
    @NotNull
    public EthBytes withSize(int size) {
        if (value.size() < size) {
            val currentBytes = toByteArray();
            val newBytes = new byte[size];
            System.arraycopy(currentBytes, 0, newBytes, 0, currentBytes.length);
            return new EthBytes(size, ImmutableList.copyOf(Bytes.asList(newBytes)));
        }

        return new EthBytes(size, value);
    }

    /**
     * Returns the size of this <code>ethereum bytes</code>.
     *
     * @return the size of this <code>ethereum bytes</code>
     */
    public int size() {
        return size;
    }

    /**
     * Returns string representation of this <code>ethereum bytes</code>.
     *
     * @return the string representation of this <code>ethereum bytes</code>.
     */
    @Override
    @NotNull
    public String toString() {
        val sizeString = size > 0 ? size : "";
        return "bytes" + sizeString + "(" + toHex() + ")";
    }

    /**
     * Returns hex string representation of this <code>ethereum bytes</code>.
     *
     * @return the hex string representation of this <code>ethereum bytes</code>
     */
    @NotNull
    public String toHex() {
        return Hex.toHex(toByteArray());
    }

    /**
     * Returns hash code of this <code>ethereum bytes</code>.
     *
     * @return hash code of this <code>ethereum bytes</code>
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Compares this <code>ethereum bytes</code> to the specified object.
     *
     * @param object the object to compare with
     * @return true if the objects are the same; false otherwise
     */
    public boolean equals(@Nullable Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!(object instanceof EthBytes other)) {
            return false;
        }
        return value.equals(other.value);
    }

    /**
     * Converts this <code>ethereum bytes</code> value to mutable <code>byte</code> array.
     *
     * @return a mutable array of bytes
     */
    public byte @NotNull [] toByteArray() {
        return Bytes.toArray(value);
    }

    /* Solidity style static initializers */
    @NotNull
    public static EthBytes bytes(byte @NotNull [] value) {
        return new EthBytes(DYNAMIC_SIZE, ImmutableList.copyOf(Bytes.asList(value)));
    }

    @NotNull
    public static EthBytes bytes1(byte @NotNull [] value) {
        return bytes(value).withSize(1);
    }

    @NotNull
    public static EthBytes bytes2(byte @NotNull [] value) {
        return bytes(value).withSize(2);
    }

    @NotNull
    public static EthBytes bytes3(byte @NotNull [] value) {
        return bytes(value).withSize(3);
    }

    @NotNull
    public static EthBytes bytes4(byte @NotNull [] value) {
        return bytes(value).withSize(4);
    }

    @NotNull
    public static EthBytes bytes5(byte @NotNull [] value) {
        return bytes(value).withSize(5);
    }

    @NotNull
    public static EthBytes bytes6(byte @NotNull [] value) {
        return bytes(value).withSize(6);
    }

    @NotNull
    public static EthBytes bytes7(byte @NotNull [] value) {
        return bytes(value).withSize(7);
    }

    @NotNull
    public static EthBytes bytes8(byte @NotNull [] value) {
        return bytes(value).withSize(8);
    }

    @NotNull
    public static EthBytes bytes9(byte @NotNull [] value) {
        return bytes(value).withSize(9);
    }

    @NotNull
    public static EthBytes bytes10(byte @NotNull [] value) {
        return bytes(value).withSize(10);
    }

    @NotNull
    public static EthBytes bytes11(byte @NotNull [] value) {
        return bytes(value).withSize(11);
    }

    @NotNull
    public static EthBytes bytes12(byte @NotNull [] value) {
        return bytes(value).withSize(12);
    }

    @NotNull
    public static EthBytes bytes13(byte @NotNull [] value) {
        return bytes(value).withSize(13);
    }

    @NotNull
    public static EthBytes bytes14(byte @NotNull [] value) {
        return bytes(value).withSize(14);
    }

    @NotNull
    public static EthBytes bytes15(byte @NotNull [] value) {
        return bytes(value).withSize(15);
    }

    @NotNull
    public static EthBytes bytes16(byte @NotNull [] value) {
        return bytes(value).withSize(16);
    }

    @NotNull
    public static EthBytes bytes17(byte @NotNull [] value) {
        return bytes(value).withSize(17);
    }

    @NotNull
    public static EthBytes bytes18(byte @NotNull [] value) {
        return bytes(value).withSize(18);
    }

    @NotNull
    public static EthBytes bytes19(byte @NotNull [] value) {
        return bytes(value).withSize(19);
    }

    @NotNull
    public static EthBytes bytes20(byte @NotNull [] value) {
        return bytes(value).withSize(20);
    }

    @NotNull
    public static EthBytes bytes21(byte @NotNull [] value) {
        return bytes(value).withSize(21);
    }

    @NotNull
    public static EthBytes bytes22(byte @NotNull [] value) {
        return bytes(value).withSize(22);
    }

    @NotNull
    public static EthBytes bytes23(byte @NotNull [] value) {
        return bytes(value).withSize(23);
    }

    @NotNull
    public static EthBytes bytes24(byte @NotNull [] value) {
        return bytes(value).withSize(24);
    }

    @NotNull
    public static EthBytes bytes25(byte @NotNull [] value) {
        return bytes(value).withSize(25);
    }

    @NotNull
    public static EthBytes bytes26(byte @NotNull [] value) {
        return bytes(value).withSize(26);
    }

    @NotNull
    public static EthBytes bytes27(byte @NotNull [] value) {
        return bytes(value).withSize(27);
    }

    @NotNull
    public static EthBytes bytes28(byte @NotNull [] value) {
        return bytes(value).withSize(28);
    }

    @NotNull
    public static EthBytes bytes29(byte @NotNull [] value) {
        return bytes(value).withSize(29);
    }

    @NotNull
    public static EthBytes bytes30(byte @NotNull [] value) {
        return bytes(value).withSize(30);
    }

    @NotNull
    public static EthBytes bytes31(byte @NotNull [] value) {
        return bytes(value).withSize(31);
    }

    @NotNull
    public static EthBytes bytes32(byte @NotNull [] value) {
        return bytes(value).withSize(32);
    }

    @NotNull
    public static EthBytes bytes(@NotNull String hex) {
        require(Hex.isValid(hex), "Malformed hex string: {}", hex);
        return bytes(Hex.toByteArray(hex));
    }

    @NotNull
    public static EthBytes bytes1(@NotNull String hex) {
        return bytes(hex).withSize(1);
    }

    @NotNull
    public static EthBytes bytes2(@NotNull String hex) {
        return bytes(hex).withSize(2);
    }

    @NotNull
    public static EthBytes bytes3(@NotNull String hex) {
        return bytes(hex).withSize(3);
    }

    @NotNull
    public static EthBytes bytes4(@NotNull String hex) {
        return bytes(hex).withSize(4);
    }

    @NotNull
    public static EthBytes bytes5(@NotNull String hex) {
        return bytes(hex).withSize(5);
    }

    @NotNull
    public static EthBytes bytes6(@NotNull String hex) {
        return bytes(hex).withSize(6);
    }

    @NotNull
    public static EthBytes bytes7(@NotNull String hex) {
        return bytes(hex).withSize(7);
    }

    @NotNull
    public static EthBytes bytes8(@NotNull String hex) {
        return bytes(hex).withSize(8);
    }

    @NotNull
    public static EthBytes bytes9(@NotNull String hex) {
        return bytes(hex).withSize(9);
    }

    @NotNull
    public static EthBytes bytes10(@NotNull String hex) {
        return bytes(hex).withSize(10);
    }

    @NotNull
    public static EthBytes bytes11(@NotNull String hex) {
        return bytes(hex).withSize(11);
    }

    @NotNull
    public static EthBytes bytes12(@NotNull String hex) {
        return bytes(hex).withSize(12);
    }

    @NotNull
    public static EthBytes bytes13(@NotNull String hex) {
        return bytes(hex).withSize(13);
    }

    @NotNull
    public static EthBytes bytes14(@NotNull String hex) {
        return bytes(hex).withSize(14);
    }

    @NotNull
    public static EthBytes bytes15(@NotNull String hex) {
        return bytes(hex).withSize(15);
    }

    @NotNull
    public static EthBytes bytes16(@NotNull String hex) {
        return bytes(hex).withSize(16);
    }

    @NotNull
    public static EthBytes bytes17(@NotNull String hex) {
        return bytes(hex).withSize(17);
    }

    @NotNull
    public static EthBytes bytes18(@NotNull String hex) {
        return bytes(hex).withSize(18);
    }

    @NotNull
    public static EthBytes bytes19(@NotNull String hex) {
        return bytes(hex).withSize(19);
    }

    @NotNull
    public static EthBytes bytes20(@NotNull String hex) {
        return bytes(hex).withSize(20);
    }

    @NotNull
    public static EthBytes bytes21(@NotNull String hex) {
        return bytes(hex).withSize(21);
    }

    @NotNull
    public static EthBytes bytes22(@NotNull String hex) {
        return bytes(hex).withSize(22);
    }

    @NotNull
    public static EthBytes bytes23(@NotNull String hex) {
        return bytes(hex).withSize(23);
    }

    @NotNull
    public static EthBytes bytes24(@NotNull String hex) {
        return bytes(hex).withSize(24);
    }

    @NotNull
    public static EthBytes bytes25(@NotNull String hex) {
        return bytes(hex).withSize(25);
    }

    @NotNull
    public static EthBytes bytes26(@NotNull String hex) {
        return bytes(hex).withSize(26);
    }

    @NotNull
    public static EthBytes bytes27(@NotNull String hex) {
        return bytes(hex).withSize(27);
    }

    @NotNull
    public static EthBytes bytes28(@NotNull String hex) {
        return bytes(hex).withSize(28);
    }

    @NotNull
    public static EthBytes bytes29(@NotNull String hex) {
        return bytes(hex).withSize(29);
    }

    @NotNull
    public static EthBytes bytes30(@NotNull String hex) {
        return bytes(hex).withSize(30);
    }

    @NotNull
    public static EthBytes bytes31(@NotNull String hex) {
        return bytes(hex).withSize(31);
    }

    @NotNull
    public static EthBytes bytes32(@NotNull String hex) {
        return bytes(hex).withSize(32);
    }

}
