package dev.klepto.kweb3.abi;

import dev.klepto.kweb3.Web3Error;
import dev.klepto.kweb3.abi.type.*;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static dev.klepto.kweb3.abi.type.util.Types.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class HeadlongCodecTest {

    private final HeadlongCodec codec = new HeadlongCodec();

    @Test
    public void testDecodeEmpty() {
        assertNull(codec.decode("", new AbiType(Uint.class)));
    }

    @Test
    public void testDecodeStruct() {
        val abi = "00000000000000000000000000000000000000000000000000000000000000" +
                "20000000000000000000000000000000000000000000000000000000000000000" +
                "10000000000000000000000000000000000000000000000000000000000000001";

        val innerType = new AbiType(Struct.class, List.of(new AbiType(Uint.class, List.of(), false, 256, 0)), true, 0, 0);
        val type = new AbiType(Struct.class, List.of(innerType), false, 0, 0);

        val expected = struct((Object) array(struct(uint256(1))));
        val result = codec.decode(abi, type);
        assertEquals(expected.size(), result.size());
        assertTrue(result.getFirst().getClass().isArray());

        val expectedArray = (Object[]) expected.getFirst();
        val resultArray = (Object[]) result.getFirst();
        assertEquals(expectedArray.length, resultArray.length);
        assertEquals(expectedArray[0].getClass(), resultArray[0].getClass());
        assertEquals(Struct.class, resultArray[0].getClass());

        val expectedStruct = (Struct) expectedArray[0];
        val resultStruct = (Struct) resultArray[0];
        assertIterableEquals(expectedStruct, resultStruct);
    }

    @Test
    public void testDecodeAddress() {
        assertEquals(
                address("0xAb5801a7D398351b8bE11C439e05C5B3259aeC9B"),
                codec.decode(
                        "000000000000000000000000ab5801a7d398351b8be11c439e05c5b3259aec9b",
                        new AbiType(Address.class)
                ).get(0)
        );
        assertEquals(
                address("0xd8dA6BF26964aF9D7eEd9e03E53415D37aA96045"),
                codec.decode(
                        "000000000000000000000000d8da6bf26964af9d7eed9e03e53415d37aa96045",
                        new AbiType(Address.class)
                ).get(0)
        );
    }

    @Test
    public void testDecodeString() {
        assertEquals(
                "this is a test string",
                codec.decode(
                        "00000000000000000000000000000000000000000000000000000000000000" +
                                "20000000000000000000000000000000000000000000000000000000000000" +
                                "0015746869732069732061207465737420737472696e670000000000000000000000",
                        new AbiType(String.class)
                ).get(0)
        );
    }

    @Test
    public void testDecodeBytes() {
        assertArrayEquals(
                new byte[]{0x11, 0x22, 0x33, 0x44, 0x55},
                ((Bytes) codec.decode(
                        "1122334455000000000000000000000000000000000000000000000000000000",
                        new AbiType(Bytes.class).withValueSize(5)
                ).get(0)).getValue()
        );
    }

    @Test
    public void testDecodeInt() {
        assertEquals(
                int256(-123456789),
                codec.decode(
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffff8a432eb",
                        new AbiType(Int.class).withValueSize(256)
                ).get(0)
        );

        assertEquals(
                uint256(123456789),
                codec.decode(
                        "00000000000000000000000000000000000000000000000000000000075bcd15",
                        new AbiType(Uint.class).withValueSize(256)
                ).get(0)
        );
    }

    @Test
    public void testDecodeBoolean() {
        assertTrue(
                (boolean) codec.decode(
                        "0000000000000000000000000000000000000000000000000000000000000001",
                        new AbiType(boolean.class)
                ).get(0)
        );

        assertFalse(
                (boolean) codec.decode(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        new AbiType(boolean.class)
                ).get(0)
        );
    }

    @Test
    public void testEncodeUnsupported() {
        assertThrows(Web3Error.class, () -> codec.encode(struct(new Object()), new AbiType(Uint.class)));
    }

    @Test
    public void testEncodeStruct() {
        assertEquals("00000000000000000000000000000000000000000000000000000000000000" +
                        "20000000000000000000000000000000000000000000000000000000000000000" +
                        "10000000000000000000000000000000000000000000000000000000000000001",
                codec.encode(struct((Object) array(struct(uint256(1)))))
        );
    }

    @Test
    public void testEncodeAddress() {
        val addressA = address("0xAb5801a7D398351b8bE11C439e05C5B3259aeC9B");
        val addressB = address("0xd8dA6BF26964aF9D7eEd9e03E53415D37aA96045");

        assertEquals(
                "000000000000000000000000ab5801a7d398351b8be11c439e05c5b3259aec9b",
                codec.encode(addressA)
        );
        assertEquals(
                "000000000000000000000000d8da6bf26964af9d7eed9e03e53415d37aa96045",
                codec.encode(addressB)
        );

        val addressArray = (Object) array(addressA, addressB);
        assertEquals(
                "00000000000000000000000000000000000000000000000000000000000000" +
                        "20000000000000000000000000000000000000000000000000000000000000000" +
                        "2000000000000000000000000ab5801a7d398351b8be11c439e05c5b3259aec9b" +
                        "000000000000000000000000d8da6bf26964af9d7eed9e03e53415d37aa96045",
                codec.encode(struct(addressArray))
        );
    }

    @Test
    public void testEncodeString() {
        /*
         * Compact font selection text: https://www.w3.org/2001/06/utf-8-test/UTF-8-demo.html
         */
        val data = new byte[]{
                (byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45, (byte) 0x46, (byte) 0x47,
                (byte) 0x48, (byte) 0x49, (byte) 0x4a, (byte) 0x4b, (byte) 0x4c, (byte) 0x4d, (byte) 0x4e,
                (byte) 0x4f, (byte) 0x50, (byte) 0x51, (byte) 0x52, (byte) 0x53, (byte) 0x54, (byte) 0x55,
                (byte) 0x56, (byte) 0x57, (byte) 0x58, (byte) 0x59, (byte) 0x5a, (byte) 0x20, (byte) 0x2f,
                (byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36,
                (byte) 0x37, (byte) 0x38, (byte) 0x39, (byte) 0x61, (byte) 0x62, (byte) 0x63, (byte) 0x64,
                (byte) 0x65, (byte) 0x66, (byte) 0x67, (byte) 0x68, (byte) 0x69, (byte) 0x6a, (byte) 0x6b,
                (byte) 0x6c, (byte) 0x6d, (byte) 0x6e, (byte) 0x6f, (byte) 0x70, (byte) 0x71, (byte) 0x72,
                (byte) 0x73, (byte) 0x74, (byte) 0x75, (byte) 0x76, (byte) 0x77, (byte) 0x78, (byte) 0x79,
                (byte) 0x7a, (byte) 0x20, (byte) 0xc2, (byte) 0xa3, (byte) 0xc2, (byte) 0xa9, (byte) 0xc2,
                (byte) 0xb5, (byte) 0xc3, (byte) 0x80, (byte) 0xc3, (byte) 0x86, (byte) 0xc3, (byte) 0x96,
                (byte) 0xc3, (byte) 0x9e, (byte) 0xc3, (byte) 0x9f, (byte) 0xc3, (byte) 0xa9, (byte) 0xc3,
                (byte) 0xb6, (byte) 0xc3, (byte) 0xbf, (byte) 0xe2, (byte) 0x80, (byte) 0x93, (byte) 0xe2,
                (byte) 0x80, (byte) 0x94, (byte) 0xe2, (byte) 0x80, (byte) 0x98, (byte) 0xe2, (byte) 0x80,
                (byte) 0x9c, (byte) 0xe2, (byte) 0x80, (byte) 0x9d, (byte) 0xe2, (byte) 0x80, (byte) 0x9e,
                (byte) 0xe2, (byte) 0x80, (byte) 0xa0, (byte) 0xe2, (byte) 0x80, (byte) 0xa2, (byte) 0xe2,
                (byte) 0x80, (byte) 0xa6, (byte) 0xe2, (byte) 0x80, (byte) 0xb0, (byte) 0xe2, (byte) 0x84,
                (byte) 0xa2, (byte) 0xc5, (byte) 0x93, (byte) 0xc5, (byte) 0xa0, (byte) 0xc5, (byte) 0xb8,
                (byte) 0xc5, (byte) 0xbe, (byte) 0xe2, (byte) 0x82, (byte) 0xac, (byte) 0x20, (byte) 0xce,
                (byte) 0x91, (byte) 0xce, (byte) 0x92, (byte) 0xce, (byte) 0x93, (byte) 0xce, (byte) 0x94,
                (byte) 0xce, (byte) 0xa9, (byte) 0xce, (byte) 0xb1, (byte) 0xce, (byte) 0xb2, (byte) 0xce,
                (byte) 0xb3, (byte) 0xce, (byte) 0xb4, (byte) 0xcf, (byte) 0x89, (byte) 0x20, (byte) 0xd0,
                (byte) 0x90, (byte) 0xd0, (byte) 0x91, (byte) 0xd0, (byte) 0x92, (byte) 0xd0, (byte) 0x93,
                (byte) 0xd0, (byte) 0x94, (byte) 0xd0, (byte) 0xb0, (byte) 0xd0, (byte) 0xb1, (byte) 0xd0,
                (byte) 0xb2, (byte) 0xd0, (byte) 0xb3, (byte) 0xd0, (byte) 0xb4, (byte) 0xe2, (byte) 0x88,
                (byte) 0x80, (byte) 0xe2, (byte) 0x88, (byte) 0x82, (byte) 0xe2, (byte) 0x88, (byte) 0x88,
                (byte) 0xe2, (byte) 0x84, (byte) 0x9d, (byte) 0xe2, (byte) 0x88, (byte) 0xa7, (byte) 0xe2,
                (byte) 0x88, (byte) 0xaa, (byte) 0xe2, (byte) 0x89, (byte) 0xa1, (byte) 0xe2, (byte) 0x88,
                (byte) 0x9e, (byte) 0x20, (byte) 0xe2, (byte) 0x86, (byte) 0x91, (byte) 0xe2, (byte) 0x86,
                (byte) 0x97, (byte) 0xe2, (byte) 0x86, (byte) 0xa8, (byte) 0xe2, (byte) 0x86, (byte) 0xbb,
                (byte) 0xe2, (byte) 0x87, (byte) 0xa3, (byte) 0x20, (byte) 0xe2, (byte) 0x94, (byte) 0x90,
                (byte) 0xe2, (byte) 0x94, (byte) 0xbc, (byte) 0xe2, (byte) 0x95, (byte) 0x94, (byte) 0xe2,
                (byte) 0x95, (byte) 0x98, (byte) 0xe2, (byte) 0x96, (byte) 0x91, (byte) 0xe2, (byte) 0x96,
                (byte) 0xba, (byte) 0xe2, (byte) 0x98, (byte) 0xba, (byte) 0xe2, (byte) 0x99, (byte) 0x80,
                (byte) 0x20, (byte) 0xef, (byte) 0xac, (byte) 0x81, (byte) 0xef, (byte) 0xbf, (byte) 0xbd,
                (byte) 0xe2, (byte) 0x91, (byte) 0x80, (byte) 0xe2, (byte) 0x82, (byte) 0x82, (byte) 0xe1,
                (byte) 0xbc, (byte) 0xa0, (byte) 0xe1, (byte) 0xb8, (byte) 0x82, (byte) 0xd3, (byte) 0xa5,
                (byte) 0xe1, (byte) 0xba, (byte) 0x84, (byte) 0xc9, (byte) 0x90, (byte) 0xcb, (byte) 0x90,
                (byte) 0xe2, (byte) 0x8d, (byte) 0x8e, (byte) 0xd7, (byte) 0x90, (byte) 0xd4, (byte) 0xb1,
                (byte) 0xe1, (byte) 0x83, (byte) 0x90
        };

        val value = new String(data, StandardCharsets.UTF_8);
        assertEquals(
                "00000000000000000000000000000000000000000000000000000000000000" +
                        "20000000000000000000000000000000000000000000000000000000000000" +
                        "01144142434445464748494a4b4c4d4e4f505152535455565758595a202f30" +
                        "3132333435363738396162636465666768696a6b6c6d6e6f70717273747576" +
                        "7778797a20c2a3c2a9c2b5c380c386c396c39ec39fc3a9c3b6c3bfe28093e2" +
                        "8094e28098e2809ce2809de2809ee280a0e280a2e280a6e280b0e284a2c593" +
                        "c5a0c5b8c5bee282ac20ce91ce92ce93ce94cea9ceb1ceb2ceb3ceb4cf8920" +
                        "d090d091d092d093d094d0b0d0b1d0b2d0b3d0b4e28880e28882e28888e284" +
                        "9de288a7e288aae289a1e2889e20e28691e28697e286a8e286bbe287a320e2" +
                        "9490e294bce29594e29598e29691e296bae298bae2998020efac81efbfbde2" +
                        "9180e28282e1bca0e1b882d3a5e1ba84c990cb90e28d8ed790d4b1e1839000" +
                        "0000000000000000000000",
                codec.encode(struct(value), new AbiType(String.class))
        );
    }

    @Test
    public void testEncodeBytes() {
        val value = bytes("0x1122334455");
        assertEquals(
                "00000000000000000000000000000000000000000000000000000000000000" +
                        "20000000000000000000000000000000000000000000000000000000000000" +
                        "00051122334455000000000000000000000000000000000000000000000000000000",
                codec.encode(value)
        );
        assertEquals(
                "1122334455000000000000000000000000000000000000000000000000000000",
                codec.encode(value.withSize(5))
        );
    }

    @Test
    public void testEncodeInt() {
        assertEquals(
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffbd1a8909",
                codec.encode(int256(-1122334455))
        );
        assertEquals(
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff85",
                codec.encode(int32(-123))
        );
        assertEquals(
                "000000000000000000000000000000000000000000000000000000000000007b",
                codec.encode(int64(123))
        );
    }

    @Test
    public void testEncodeBoolean() {
        assertEquals(
                "0000000000000000000000000000000000000000000000000000000000000001",
                codec.encode(struct(true), new AbiType(boolean.class))
        );
        assertEquals(
                "0000000000000000000000000000000000000000000000000000000000000000",
                codec.encode(struct(false), new AbiType(boolean.class))
        );
    }


}
