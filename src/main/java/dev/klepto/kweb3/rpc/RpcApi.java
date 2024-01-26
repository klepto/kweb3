package dev.klepto.kweb3.rpc;

import dev.klepto.kweb3.rpc.api.EthCall;
import dev.klepto.kweb3.rpc.api.EthEstimateGas;
import dev.klepto.kweb3.rpc.api.EthGasPrice;

/**
 * Represents Ethereum RPC API client implementing all the supported methods.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcApi extends EthCall, EthGasPrice, EthEstimateGas {

}
