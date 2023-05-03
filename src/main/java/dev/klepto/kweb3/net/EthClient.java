package dev.klepto.kweb3.net;

import dev.klepto.kweb3.Web3Request;
import dev.klepto.kweb3.Web3Response;
import dev.klepto.kweb3.abi.type.AbiType;
import dev.klepto.kweb3.abi.type.Address;
import dev.klepto.kweb3.abi.type.Tuple;
import dev.klepto.kweb3.abi.type.Uint;
import dev.klepto.kweb3.chain.Chain;
import dev.klepto.kweb3.gas.GasFeeProvider;

import java.util.List;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthClient {

    Address getAddress();

    Chain getChain();

    Uint balanceOf(Address address);

    Uint estimateGas(Web3Request request);

    Web3Response send(Web3Request request);

    GasFeeProvider getGasFeeProvider();

    void setGasFeeProvider(GasFeeProvider provider);

    Uint getGasPrice();

    void setGasPrice(Uint gasPrice);

    Uint getGasLimit();

    void setGasLimit(Uint gasLimit);

    List<Web3Request> getLogs(Runnable runnable);

    String abiEncode(Web3Request request);

    String abiEncode(Tuple value, AbiType type);

    Tuple abiDecode(String abi, AbiType type);

    boolean isLogging();

}
