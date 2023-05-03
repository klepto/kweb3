package dev.klepto.kweb3.abi.type;

import java.util.List;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Struct extends Tuple {
    
    public Struct(List<Object> value) {
        super(value);
    }

    @Override
    public String toString() {
        return "struct" + toValueString();
    }

}
