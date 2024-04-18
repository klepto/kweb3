package dev.klepto.kweb3.core.contract;

import dev.klepto.kweb3.core.ethereum.abi.descriptor.TypeDescriptor;
import dev.klepto.unreflect.UnreflectType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 * Parses a contract implementation interface in-order to facilitate JVM to RPC mapping done by
 * {@link ContractExecutor}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface ContractParser {

    /**
     * Parses the contract function representation for a given contract interface method.
     *
     * @param method the contract interface method
     * @return the contract function description
     */
    @NotNull
    ContractFunction parseFunction(@NotNull Method method);

    /**
     * Parses contract parameters type description for a given contract interface method.
     *
     * @param method the contract interface method
     * @return the contract parameters type description
     */
    @NotNull
    TypeDescriptor parseParametersTypeDescriptor(@NotNull Method method);

    /**
     * Parses contract return type description for a given contract interface method.
     *
     * @param method the contract interface method
     * @return the contract return type description
     */
    @NotNull
    TypeDescriptor parseReturnTypeDescriptor(@NotNull Method method);

    /**
     * Parses contract JVM return type for a given contract interface method.
     *
     * @param method the contract interface method
     * @return the contract JVM return type, could be ethereum data type or a container for struct or tuple
     */
    @NotNull
    UnreflectType parseReturnType(@NotNull Method method);

}
