package dev.klepto.kweb3.abi.type;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class AddressTest {

    @Test
    public void testAddress() {
        val value = new BigInteger("980829894800078742339098726298021775428853559041");
        val hex = "ABCDEF0123456789ABCDEF0123456789ABCDEF01";
        val hexWithPrefix = "0xABCDEF0123456789ABCDEF0123456789ABCDEF01";
        val hexChecksum = "0xabCDeF0123456789AbcdEf0123456789aBCDEF01";

        assertEquals(value, new Address(value).getValue());
        assertEquals(value, new Address(hex).getValue());
        assertEquals(value, new Address(hexWithPrefix).getValue());
        assertEquals(value, new Address(hexChecksum).getValue());
        assertEquals(hexChecksum, new Address(value).toHex());
        assertEquals(160, new Address(value).getSize());
    }

}
