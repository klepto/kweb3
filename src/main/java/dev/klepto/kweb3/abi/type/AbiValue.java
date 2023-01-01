package dev.klepto.kweb3.abi.type;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface AbiValue {

    Object getValue();

    default AbiType getType() {
        return new AbiType(getClass());
    }

}