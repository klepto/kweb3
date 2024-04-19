package dev.klepto.kweb3.core;

import dev.klepto.kweb3.core.chain.Web3Chain;
import dev.klepto.kweb3.core.chain.Web3Endpoint;
import dev.klepto.kweb3.core.contract.ContractProxyProvider;
import dev.klepto.kweb3.core.contract.Web3Contract;
import dev.klepto.kweb3.core.ethereum.EthereumClient;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;

import static dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress.address;

/**
 * Web3Client implementation.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public class Web3Client implements Closeable {

    private final EthAddress address;
    private final @Delegate EthereumClient ethereum;
    private final ContractProxyProvider contracts = new ContractProxyProvider(this);

    /**
     * Creates a new client instance for the given endpoints.
     *
     * @param endpoints the endpoints this client connects to
     */
    public Web3Client(@NotNull Web3Endpoint... endpoints) {
        this.address = EthAddress.ZERO;
        this.ethereum = new EthereumClient(endpoints);
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
        ethereum.close();
    }


}
