package dev.klepto.kweb3.contracts;

import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.contract.Web3Contract;
import dev.klepto.kweb3.core.contract.annotation.View;
import dev.klepto.kweb3.core.contract.type.EthStructContainer;
import dev.klepto.kweb3.core.type.EthAddress;
import dev.klepto.kweb3.core.type.EthArray;
import dev.klepto.kweb3.core.type.EthBool;
import dev.klepto.kweb3.core.type.EthBytes;

/**
 * Implementation of <a href="https://github.com/mds1/multicall">Multicall3</a> smart contract.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Multicall3 extends Web3Contract {

    /**
     * Aggregates all calls into a single request and returns an array containing {@link Result results} of each call.
     *
     * @param calls an ethereum array containing calls for aggregation
     * @return an ethereum array containing result of each call
     */
    @View
    Web3Result<EthArray<Result>> aggregate3(EthArray<Call> calls);

    /**
     * Contains a single smart contract call request. If <code>allowFailure</code> is set to <code>false</code>, failure
     * of this call will cause entire aggregation request to fail.
     *
     * @param target       the target contract address
     * @param allowFailure marks that aggregation request is allowed to continue upon failure of this call
     * @param callData     the call-data contained in ethereum bytes
     */
    record Call(EthAddress target, EthBool allowFailure, EthBytes callData) implements EthStructContainer {
    }

    /**
     * Contains result of a single smart contract call.
     *
     * @param success    true if call was successful
     * @param returnData ethereum bytes containing return data
     */
    record Result(EthBytes success, EthBytes returnData) implements EthStructContainer {
    }

}
