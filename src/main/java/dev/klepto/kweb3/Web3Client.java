package dev.klepto.kweb3;

import dev.klepto.kweb3.contract.Contract;
import dev.klepto.kweb3.contract.Contracts;
import dev.klepto.kweb3.rpc.RpcApi;
import dev.klepto.kweb3.rpc.RpcApiClient;
import dev.klepto.kweb3.type.EthAddress;
import lombok.Getter;

import static dev.klepto.kweb3.type.EthAddress.address;

/**
 * Web3Client implementation.
 */
@Getter
public class Web3Client {

    private final Web3Network network;
    private final RpcApi rpc;
    private final Contracts contracts;

    public Web3Client(Web3Network network) {
        this.network = network;
        this.rpc = new RpcApiClient(network);
        this.contracts = new Contracts(this);
    }

    /**
     * Binds contract of a given type to a blockchain address for seamless transacting.
     *
     * @param type    the contract type, must be an interface extending Contract
     * @param address the blockchain address of the contract
     * @return the contract instance for direct blockchain transactions
     */
    public <T extends Contract> T contract(Class<T> type, EthAddress address) {
        return contracts.createProxy(type, address);
    }

    /**
     * Binds contract of a given type to a blockchain address string for seamless transacting.
     *
     * @param type    the contract type, must be an interface extending Contract
     * @param address the blockchain address string of the contract
     * @return the contract instance for direct blockchain transactions
     */
    public <T extends Contract> T contract(Class<T> type, String address) {
        return contract(type, address(address));
    }

}
