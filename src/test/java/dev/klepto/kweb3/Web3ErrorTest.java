package dev.klepto.kweb3;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Web3ErrorTest {

    @Test
    public void testRequire() {
        Web3Error.require(true, "");
        assertThrows(Web3Error.class, () -> Web3Error.require(false, ""));
    }

    @Test
    public void testError() {
        val error = new Web3Error("{} {}", "one", "two");
        assertEquals(error.getMessage(), "one two");
        assertThrows(Web3Error.class, () -> Web3Error.error(""));
    }

}
