package dev.klepto.kweb3.core.contract.type;

import dev.klepto.kweb3.core.type.EthValue;
import dev.klepto.unreflect.UnreflectType;
import lombok.val;

/**
 * Container for empty smart contract function return value.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class EthVoid implements EthValue {

    /**
     * Checks if the given type is a void type.
     *
     * @param type the type to check
     * @return true if the type is void, false otherwise
     */
    public static boolean isVoid(UnreflectType type) {
        if (type.matches(EthVoid.class)) {
            return true;
        }
        
        val name = type.name();
        return name.equals("void")
                || name.equals("java.lang.Void")
                || name.equals("kotlin.Unit");
    }

}
