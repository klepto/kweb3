package dev.klepto.kweb3.core.ethereum.abi;

import dev.klepto.kweb3.core.ethereum.abi.descriptor.TypeDescriptor;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthTuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents ABI decoder for converting between string and ethereum data types.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface AbiDecoder {

    /**
     * Decodes given ABI string into ethereum values contained within {@link EthTuple}. Returns <code>null</code> if
     * given ABI string contains no data.
     *
     * @param abi        the ABI data string
     * @param descriptor the type description of given string
     * @return a tuple containing decoded values or <code>null</code> if ABI string contained no data
     */
    @Nullable
    EthTuple decode(@NotNull String abi, @NotNull TypeDescriptor descriptor);

}
