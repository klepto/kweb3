package dev.klepto.kweb3.core.contract;

import com.google.common.collect.ImmutableList;
import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.type.EthAddress;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Implementation {@link ContractExecutor} that logs incoming requests and never completes a response. This executor
 * does not make any RPC or IO requests.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class LoggingContractExecutor extends DefaultContractExecutor {

    private final Queue<Log> logs = new ConcurrentLinkedQueue<>();

    @Override
    public @NotNull Web3Result<String> request(@NotNull ContractCall call, @NotNull String data) {
        val address = call.proxy().getAddress();
        logs.add(new Log(address, call, call.function().signature() + data));
        return new Web3Result<>();
    }

    /**
     * Returns copy of call-data logs recorded by this interceptor.
     *
     * @return a list of call-data logs
     */
    public List<Log> getLogs() {
        return ImmutableList.copyOf(logs);
    }

    /**
     * Contains information about smart contract call.
     *
     * @param contractAddress the smart contract address
     * @param call            the smart contract interface call
     * @param data            the smart contract call data
     */
    public record Log(@NotNull EthAddress contractAddress, @NotNull ContractCall call, @NotNull String data) {
    }

}