package dev.klepto.kweb3.type;

import lombok.Getter;

import java.util.List;

/**
 * Represents Struct solidity type.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public class Struct extends SolidityType<Struct, List<SolidityType<?, ?>>> {

    private final List<SolidityType<?, ?>> value;

    public Struct(List<SolidityType<?, ?>> value) {
        this.value = value;
    }

//    private static String generateEncodedName(List<SolidityType<?, ?>> values) {
//        val builder = new StringBuilder();
//        builder.append("(");
//        for (var i = 0; i < values.size(); i++) {
//            val value = values.get(i);
//            if (i > 0) {
//                builder.append(",");
//            }
//            builder.append(value.getEncodedName());
//        }
//        builder.append(")");
//        return builder.toString();
//    }

}
