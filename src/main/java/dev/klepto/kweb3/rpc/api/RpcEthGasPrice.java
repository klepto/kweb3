package dev.klepto.kweb3.rpc.api;

/**
 * Implementation of Ethereum RPC API <code>eth_gasPrice</code> method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcEthGasPrice {

    /**
     * Returns the current gas price in wei.
     *
     * @return the response object
     */
    Response ethGasPrice();

    /**
     * Represents <code>eth_gasPrice</code> response object.
     *
     * @param result the hexadecimal value of the current gas price in wei
     */
    record Response(String result) {
    }

}
