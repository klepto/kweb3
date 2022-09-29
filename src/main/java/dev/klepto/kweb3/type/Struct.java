package dev.klepto.kweb3.type;

import lombok.Getter;
import lombok.val;

import java.util.Collections;
import java.util.List;

/**
 * Represents Struct solidity type.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public class Struct extends SolidityType {

    private final List<? extends SolidityType> values;

    public Struct(List<? extends SolidityType> values) {
        super(generateEncodedName(values));
        this.values = values;
    }

    private static String generateEncodedName(List<? extends SolidityType> values) {
        val builder = new StringBuilder();
        builder.append("(");
        for (var i = 0; i < values.size(); i++) {
            val value = values.get(i);
            if (i > 0) {
                builder.append(",");
            }
            builder.append(value.getEncodedName());
        }
        builder.append(")");
        return builder.toString();
    }



}
