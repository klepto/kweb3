package dev.klepto.kweb3.type;

/**
 * Represents an ethereum data type contains size parameter. Shared between {@link EthUint}, {@link EthBytes} and
 * {@link EthBytes}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthSizedType extends EthType {

    /**
     * Size parameter of ethereum data type. For {@link EthUint} and {@link EthInt} it represents bit-size, for
     * {@link EthBytes} it represents byte-size.
     *
     * @return the size parameter of ethereum data type
     */
    int size();

}
