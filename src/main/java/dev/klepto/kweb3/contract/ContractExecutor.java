package dev.klepto.kweb3.contract;

import dev.klepto.kweb3.Web3Result;
import lombok.val;

/**
 * Represents a contract executor. This interface is used to enable different implementations of contract proxy
 * execution to fit API user's needs, such as implementing asynchronous execution with virtual threads or coroutines,
 * custom error handling, request logging, etc. For default implementation, see {@link DefaultContractExecutor}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface ContractExecutor {

    /**
     * Executes a contract function from start to finish. This includes encoding, making RPC request and decoding the
     * response. Called by {@link ContractProxy} when user interacts with a {@link Web3Contract} interface.
     *
     * @param call the contract interface method call
     * @return the contract function execution result, the type must match return type of the interface method
     */
    default Object execute(ContractCall call) {
        val data = encode(call);
        val result = request(call, data);
        return decode(call, result);
    }

    /**
     * Encodes contract function parameters into ABI-compatible string for this contract call. Must be called by
     * {@link ContractExecutor#execute(ContractCall)} before making an RPC request.
     *
     * @param call the contract interface method call
     * @return the ABI-compatible string containing request data
     */
    String encode(ContractCall call);

    /**
     * Creates an RPC request with given data for this contract call. Must be called by
     * {@link ContractExecutor#execute(ContractCall)} after encoding contract call data.
     *
     * @param call the contract interface method call
     * @param data the ABI-compatible call data
     * @return the result of the RPC call
     */
    Web3Result<String> request(ContractCall call, String data);

    /**
     * Decodes an RPC response to interface method return type.  Must be returned by
     * {@link ContractExecutor#execute(ContractCall)} after making an RPC request.
     *
     * @param call   the contract interface method call
     * @param result the result of the RPC call
     * @return the decoded object that matches the contract interface method
     */
    Object decode(ContractCall call, Web3Result<String> result);

}
