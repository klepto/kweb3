package dev.klepto.kweb3.multicall;

import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.contract.Web3Contract;
import dev.klepto.kweb3.core.type.EthAddress;
import dev.klepto.kweb3.core.type.EthArray;
import dev.klepto.kweb3.core.type.EthBytes;
import dev.klepto.kweb3.core.type.EthUint;

/**
 * Declares a common multicall contract signature to encapsulate most common multicall contract implementations.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface MulticallContract extends Web3Contract {

    /**
     * Executes a multicall request with given parameters. As there is no-way to enforce method implementation for
     * contract proxy interfaces, this method is meant to be overridden using `default` keyword with underlying
     * multicall implementation.
     *
     * @param gasLimit  the gas limit of single call execution
     * @param sizeLimit the size limit of single call execution
     * @param addresses the list of contract addresses
     * @param data      the list of call-data to be sent to given addresses
     * @return an ordered array containing resulting bytes (possibly empty, if call failed) of each call
     */
    Web3Result<EthArray<EthBytes>> execute(
            EthUint gasLimit,
            EthUint sizeLimit,
            EthArray<EthAddress> addresses,
            EthArray<EthBytes> data
    );

    /**
     * Creates a new {@link MulticallBuilder} for this multicall smart contract implementation.
     *
     * @return a new instance of multicall builder
     */
    default MulticallBuilder builder() {
        return new MulticallBuilder(this);
    }

}
