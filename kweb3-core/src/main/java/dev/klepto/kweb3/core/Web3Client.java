package dev.klepto.kweb3.core;

import dev.klepto.kweb3.core.config.Web3Network;
import dev.klepto.kweb3.core.contract.ContractProxies;
import dev.klepto.kweb3.core.contract.Web3Contract;
import dev.klepto.kweb3.core.rpc.RpcClient;
import dev.klepto.kweb3.core.type.EthAddress;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import static dev.klepto.kweb3.core.type.EthAddress.address;

/**
 * Web3Client implementation.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public class Web3Client {

    private final Web3Network network;
    private final RpcClient rpc;
    private final ContractProxies contracts;
    private final EthAddress address;

    /**
     * Creates a new Web3Client instance with a given network configuration.
     *
     * @param network the network configuration
     */
    public Web3Client(@NotNull Web3Network network) {
        this.network = network;
        this.rpc = RpcClient.create(network.endpoints()[0]);
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

}
