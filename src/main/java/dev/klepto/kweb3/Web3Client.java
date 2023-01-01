package dev.klepto.kweb3;

import dev.klepto.kweb3.abi.type.Address;
import dev.klepto.kweb3.chain.Chain;
import dev.klepto.kweb3.contract.Contract;
import dev.klepto.kweb3.net.EthClient;
import dev.klepto.kweb3.net.web3j.Web3jClient;
import dev.klepto.kweb3.util.multicall.MulticallContract;
import dev.klepto.kweb3.util.multicall.MulticallBuilder;

import static dev.klepto.kweb3.abi.type.util.Types.address;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Web3Client extends EthClient {

    static Web3Client create(Chain chain) {
        return create(chain, null);
    }

    static Web3Client create(Chain chain, String privateKey) {
        return new Web3jClient(chain, privateKey);
    }

    default <T extends Contract> T contract(Class<T> type, String address) {
        return contract(type, address(address));
    }

    <T extends Contract> T contract(Class<T> type, Address address);

    void setMulticallContract(MulticallContract contract);

    MulticallBuilder multicall();

}
