package dev.klepto.kweb3.contract;

import dev.klepto.kweb3.abi.descriptor.TypeDescriptor;
import dev.klepto.unreflect.UnreflectType;

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
    ContractFunction parseFunction(Method method);

    /**
     * Parses contract parameters type description for a given contract interface method.
     *
     * @param method the contract interface method
     * @return the contract parameters type description
     */
    TypeDescriptor parseParametersTypeDescriptor(Method method);

    /**
     * Parses contract return type description for a given contract interface method.
     *
     * @param method the contract interface method
     * @return the contract return type description
     */
    TypeDescriptor parseReturnTypeDescriptor(Method method);

    /**
     * Parses contract JVM return type for a given contract interface method.
     *
     * @param method the contract interface method
     * @return the contract JVM return type, could be ethereum data type or a container for struct or tuple
     */
    UnreflectType parseReturnType(Method method);

}
