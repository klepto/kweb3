package dev.klepto.kweb3;


import dev.klepto.kweb3.chain.Chain;
import dev.klepto.kweb3.contract.Contract;
import dev.klepto.kweb3.contract.ContractProxy;
import dev.klepto.kweb3.gas.GasFeeProvider;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.sized.Uint256;
import dev.klepto.kweb3.util.number.Numeric;
import dev.klepto.kweb3.web3j.Web3jClient;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Web3Client {

    default <T extends Contract> T getContract(Class<T> type, Address address) {
        return (T) Proxy.newProxyInstance(
                Web3Client.class.getClassLoader(),
                new Class[]{type},
                new ContractProxy(this, Numeric.toAddress(address))
        );
    }

    Uint256 balanceOf(Address address);

    Web3Response send(Web3Request request);

    List<Object> abiDecode(String abi, List<Class<?>> types);

    String abiEncode(Web3Request request);

    List<String> abiEncode(Runnable runnable);

    Uint256 estimateGas(Web3Request request);

    List<Uint256> estimateGas(Runnable runnable);

    Address getAddress();

    List<Web3Request> getLogs(Runnable runnable);

    void setGasFeeProvider(GasFeeProvider provider);

    static Web3Client createClient(Web3Wallet wallet, Chain chain) {
        return new Web3jClient(chain.getRpcUrls()[0], chain.getChainId(), wallet.getPrivateKey());
    }

}
