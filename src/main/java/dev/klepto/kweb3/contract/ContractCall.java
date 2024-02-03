package dev.klepto.kweb3.contract;

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
}
