package dev.klepto.kweb3.multicall;

import com.google.common.collect.ImmutableList;
import dev.klepto.kweb3.core.Web3Client;
import dev.klepto.kweb3.core.Web3Network;
import dev.klepto.kweb3.core.contract.ContractCall;
import dev.klepto.kweb3.core.contract.DefaultContractExecutor;
import dev.klepto.kweb3.core.type.EthAddress;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * An offline {@link Web3Client} implementation that returns empty results and records contract call-data logs.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class LoggingWeb3Client extends Web3Client {

    private final Queue<Log> logs = new ConcurrentLinkedQueue<>();

    public LoggingWeb3Client() {
        super(new Web3Network.Basic("", 0));
        getContracts().setExecutor(new LoggingContractExecutor());
    }

    /**
     * Returns copy of call-data logs recorded by this client.
     *
     * @return a list of call-data logs
     */
    public List<Log> getLogs() {
        return ImmutableList.copyOf(logs);
    }

    /**
     * Contains information about ethereum contract call.
     *
     * @param contractAddress the ethereum contract address
     * @param data            the contract call-data
     */
    public record Log(EthAddress contractAddress, String data) {
    }

    /**
     * A mock executor that logs incoming contract call and returns <code>null</code>.
     */
    private class LoggingContractExecutor extends DefaultContractExecutor {

        @Override
        public Object execute(@NotNull ContractCall call) {
            logs.add(new Log(call.proxy().getAddress(), call.function().signature() + encode(call)));
            return null;
        }

    }

}
