package dev.klepto.kweb3.abi.type;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.klepto.kweb3.abi.type.util.Types.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class StructTest {

    @Test
    public void testStruct() {
        val values = List.of(array(address(0)), uint256(0), "");
        val struct = new Struct(values);
        assertEquals(3, struct.size());
        assertEquals("(address[],uint256,string)", struct.getType().toString());

        val structOfStructs = new Struct(List.of((Object) array(struct)));
        assertEquals(1, structOfStructs.size());
        assertEquals("((address[],uint256,string)[])", structOfStructs.getType().toString());
    }

}
