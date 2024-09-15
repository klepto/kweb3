package dev.klepto.kweb3.core.contract;

import dev.klepto.kweb3.core.ethereum.type.EthValue;
import lombok.val;

import java.util.Arrays;

/**
 * Represents a contract interface method call.
 *
 * @param proxy    the contract proxy that is making this request
 * @param function the contract function that is being called
 * @param args     the arguments that contract interface was called with
 */
public record ContractCall(ContractProxy proxy,
                           ContractFunction function,
                           Object[] args) {

    @Override
    public String toString() {
        val contractAddress = proxy.address().toHexString();
        val contractName = proxy.type().getSimpleName();
        val functionName = function.name();
        val ethereumArgs = Arrays.stream(args)
                .filter(EthValue.class::isInstance)
                .map(Object::toString)
                .toList();
        val argsString = String.join(", ", ethereumArgs);
        return String.format("%s(%s).%s(%s)", contractName, contractAddress, functionName, argsString);
    }

}
