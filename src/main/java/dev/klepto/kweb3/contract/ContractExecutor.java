package dev.klepto.kweb3.contract;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

/**
 * Encodes and executes appropriate blockchain transactions and returns the result for any given contract method.
 * Encoding/decoding is inferred from method and parameter types and annotations.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@RequiredArgsConstructor
public class ContractExecutor {

    public Object execute(ContractProxy proxy, Method method, Object[] args) {
        // TODO: Implement.
        return null;
    }

}
