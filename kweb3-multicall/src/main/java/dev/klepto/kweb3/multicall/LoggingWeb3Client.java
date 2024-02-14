package dev.klepto.kweb3.multicall;

import com.google.common.collect.ImmutableList;
import dev.klepto.kweb3.core.Web3Client;
import dev.klepto.kweb3.core.Web3Network;
import dev.klepto.kweb3.core.contract.ContractCall;
import dev.klepto.kweb3.core.contract.DefaultContractExecutor;
import dev.klepto.kweb3.core.contract.Web3Contract;
import dev.klepto.kweb3.core.type.EthAddress;
import lombok.val;
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
     * Binds contract of a given type to an empty {@link EthAddress}. Since this client is offline, in many use-cases
     * underlying smart contract address does not need to be specified.
     *
     * @param type the contract type, must be an interface extending Contract
     * @return the contract instance for logging blockchain transactions
     */
    @NotNull
    public <T extends Web3Contract> T contract(@NotNull Class<T> type) {
        return contract(type, EthAddress.ZERO);
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
     * Contains information about smart contract call.
     *
     * @param contractAddress the smart contract address
     * @param call            the smart contract interface call
     * @param data            the smart contract call data
     */
    public record Log(EthAddress contractAddress, ContractCall call, String data) {
    }

    /**
     * A mock executor that logs incoming contract call and returns <code>null</code>.
     */
    private class LoggingContractExecutor extends DefaultContractExecutor {

        @Override
        public Object execute(@NotNull ContractCall call) {
            val address = call.proxy().getAddress();
            val data = call.function().signature() + encode(call);
            logs.add(new Log(address, call, data));
            return null;
        }

    }

}
