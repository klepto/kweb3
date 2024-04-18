package dev.klepto.kweb3.core;

import dev.klepto.kweb3.core.chain.Web3Chain;
import dev.klepto.kweb3.core.chain.Web3Endpoint;
import dev.klepto.kweb3.core.contract.ContractProxies;
import dev.klepto.kweb3.core.contract.Web3Contract;
import dev.klepto.kweb3.core.ethereum.EthereumClient;
import dev.klepto.kweb3.core.ethereum.rpc.RpcClient;
import dev.klepto.kweb3.core.type.EthAddress;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.util.Arrays;
import java.util.List;

import static dev.klepto.kweb3.core.type.EthAddress.address;

/**
 * Web3Client implementation.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public class Web3Client implements Closeable {

    private final Web3Chain chain;
    private final List<Web3Endpoint> endpoints;
    private final RpcClient rpc;
    private final EthereumClient ethereum;
    private final ContractProxies contracts;
    private final EthAddress address;

    /**
     * Creates a new client instance for the given endpoints.
     *
     * @param endpoints the endpoints this client connects to
     */
    public Web3Client(@NotNull Web3Endpoint... endpoints) {
        this.chain = endpoints[0].chain();
        this.endpoints = Arrays.asList(endpoints);
        this.rpc = new RpcClient(this.endpoints);
        this.ethereum = new EthereumClient(rpc);
        this.contracts = new ContractProxies(this);
        this.address = EthAddress.ZERO;
    }

    /**
     * Binds contract of a given type to a blockchain address for seamless transacting.
     *
     * @param type    the contract type, must be an interface extending Contract
     * @param address the blockchain address of the contract
     * @return the contract instance for direct blockchain transactions
     */
    @NotNull
    public <T extends Web3Contract> T contract(@NotNull Class<T> type, @NotNull EthAddress address) {
        return contracts.createProxy(type, address);
    }

    /**
     * Binds contract of a given type to a blockchain address string for seamless transacting.
     *
     * @param type    the contract type, must be an interface extending Contract
     * @param address the blockchain address string of the contract
     * @return the contract instance for direct blockchain transactions
     */
    @NotNull
    public <T extends Web3Contract> T contract(@NotNull Class<T> type, @NotNull String address) {
        return contract(type, address(address));
    }

    /**
     * Closes the client and releases all resources.
     */
    @Override
    public void close() {
        rpc.close();
    }

}
