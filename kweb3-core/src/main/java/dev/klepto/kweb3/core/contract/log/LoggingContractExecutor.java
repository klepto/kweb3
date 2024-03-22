package dev.klepto.kweb3.core.contract.log;

import com.google.common.collect.ImmutableList;
import dev.klepto.kweb3.core.Web3Error;
import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.contract.ContractCall;
import dev.klepto.kweb3.core.contract.ContractExecutor;
import dev.klepto.kweb3.core.contract.DefaultContractExecutor;
import dev.klepto.kweb3.core.type.EthAddress;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static dev.klepto.kweb3.core.type.EthBytes.bytes;

/**
 * Implementation {@link ContractExecutor} that logs incoming requests and fails to complete a response. This executor
 * does not result in any RPC or IO operations.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class LoggingContractExecutor extends DefaultContractExecutor {

    private final Queue<ContractCallLog> logs = new ConcurrentLinkedQueue<>();

    /**
     * Logs the contract call and throws a {@link LoggingException}.
     *
     * @param call the contract interface method call
     * @param data the ABI-compatible call data
     * @return an empty result
     * @throws LoggingException always
     */
    @Override
    public @NotNull Web3Result<String> request(@NotNull ContractCall call, @NotNull String data) {
        appendLog(call, data);
        throw new LoggingException();
    }

    /**
     * Appends a log entry for a contract call.
     *
     * @param call the contract call
     * @param data the contract call data
     */
    public void appendLog(@NotNull ContractCall call, @NotNull String data) {
        logs.add(createLog(call, data));
    }

    /**
     * Creates a log entry for a contract call.
     *
     * @param call the contract call
     * @param data the contract call data
     * @return the resulting contract call log
     */
    public @NotNull ContractCallLog createLog(@NotNull ContractCall call, @NotNull String data) {
        val clientAddress = call.proxy().getClient().getAddress();
        val contractAddress = call.proxy().getAddress();
        val calldata = bytes(call.function().signature() + data);
        val transaction = new ContractCallLog.Transaction(
                clientAddress, contractAddress, null, null, null, null, calldata
        );
        return new ContractCallLog(call, transaction);
    }

    /**
     * Returns copy of call-data logs recorded by this interceptor.
     *
     * @return a list of call-data logs
     */
    public List<ContractCallLog> getLogs() {
        return ImmutableList.copyOf(logs);
    }

    /**
     * Thrown after log was successfully appended in order to avoid blocking IO in blocking {@link ContractExecutor}
     * implementations.
     */
    public static class LoggingException extends RuntimeException {
        /**
         * Prevents stack trace from being filled in order to avoid performance overhead.
         *
         * @return this exception
         */
        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }

}