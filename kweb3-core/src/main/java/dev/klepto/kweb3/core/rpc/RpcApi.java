package dev.klepto.kweb3.core.rpc;

import dev.klepto.kweb3.core.rpc.api.EthGasPrice;
import dev.klepto.kweb3.core.rpc.api.EthCall;
import dev.klepto.kweb3.core.rpc.api.EthEstimateGas;
import dev.klepto.kweb3.core.rpc.api.EthSendRawTransaction;

/**
 * Represents Ethereum RPC API implementing all the supported methods.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcApi extends EthCall, EthGasPrice, EthEstimateGas, EthSendRawTransaction {

    /**
     * Default JSON RPC version.
     */
    String JSON_VERSION = "2.0";

}
