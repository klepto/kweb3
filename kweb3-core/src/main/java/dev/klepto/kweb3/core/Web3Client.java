package dev.klepto.kweb3.core;

import dev.klepto.kweb3.core.chain.Web3Chain;
import dev.klepto.kweb3.core.chain.Web3Endpoint;
import dev.klepto.kweb3.core.contract.*;
import dev.klepto.kweb3.core.ethereum.EthereumClient;
import dev.klepto.kweb3.core.ethereum.rpc.RpcClientListener;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;

import static dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress.address;

/**
 * Web3Client implementation.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public class Web3Client implements Closeable {

    private final @NotNull ContractProvider contractProvider;
    private final @NotNull ContractExecutor contractExecutor;
    private final @NotNull ContractParser contractParser;

    private final @NotNull EthAddress address;
    private final @Delegate EthereumClient ethereum;

    /**
     * Creates a new client instance
     *
     * @param contractProvider the contract provider
     * @param contractExecutor the contract executor
     * @param contractParser   the contract parser
     * @param endpoint         the endpoints this client connects to
     * @param listener         the listener for the client
     */
    public Web3Client(@NotNull ContractProvider contractProvider,
                      @NotNull ContractExecutor contractExecutor,
                      @NotNull ContractParser contractParser,
                      @NotNull Web3Endpoint endpoint,
                      @Nullable RpcClientListener listener) {
        this.contractProvider = contractProvider;
        this.contractExecutor = contractExecutor;
        this.contractParser = contractParser;
        this.address = EthAddress.ZERO;
        this.ethereum = new EthereumClient(endpoint, listener);
    }

    public Web3Client(@NotNull ContractProvider contractProvider,
                      @NotNull ContractExecutor contractExecutor,
                      @NotNull ContractParser contractParser,
                      @NotNull Web3Endpoint endpoint) {
        this(contractProvider, contractExecutor, contractParser, endpoint, null);
    }

    /**
     * Returns the {@link Web3Endpoint} that the client currently connects to.
     *
     * @return the current endpoint of the client
     */
    public Web3Endpoint getEndpoint() {
        return ethereum.getRpc().endpoint();
    }

    /**
     * Returns the {@link Web3Chain} of this client.
     *
     * @return the web3 chain of this client
     */
    public Web3Chain getChain() {
        return getEndpoint().chain();
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
        return contractProvider.provide(this, type, address);
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
        ethereum.close();
    }


}
