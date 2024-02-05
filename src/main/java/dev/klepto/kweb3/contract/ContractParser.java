package dev.klepto.kweb3.contract;

import dev.klepto.kweb3.abi.descriptor.TypeDescriptor;

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
    TypeDescriptor parseParametersType(Method method);

    /**
     * Parses contract return type description for a given contract interface method.
     *
     * @param method the contract interface method
     * @return the contract return type description
     */
    TypeDescriptor parseReturnType(Method method);

}
