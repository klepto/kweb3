package dev.klepto.kweb3.rpc;

import dev.klepto.kweb3.rpc.api.RpcEthCall;
import dev.klepto.kweb3.rpc.api.RpcEthEstimateGas;
import dev.klepto.kweb3.rpc.api.RpcEthGasPrice;
import dev.klepto.kweb3.rpc.api.RpcEthSendRawTransaction;

/**
 * Represents Ethereum RPC API client implementing all the supported methods.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcClient extends
        RpcEthCall,
        RpcEthSendRawTransaction,
        RpcEthGasPrice,
        RpcEthEstimateGas {
}
