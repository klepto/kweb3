package dev.klepto.kweb3.kotlin.oneinch.multicall

import dev.klepto.kweb3.core.contract.Web3Contract
import dev.klepto.kweb3.core.contract.annotation.View
import dev.klepto.kweb3.core.contract.type.EthStructContainer
import dev.klepto.kweb3.core.contract.type.EthTupleContainer
import dev.klepto.kweb3.core.type.EthAddress
import dev.klepto.kweb3.core.type.EthArray
import dev.klepto.kweb3.core.type.EthBytes
import dev.klepto.kweb3.core.type.EthUint

/**
 * Implementation of [1inch Multicall](https://github.com/1inch/multicall)
 * smart contract.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
interface OneInchMulticall : Web3Contract {

    /**
     * Aggregates all [calls] into a single request and returns an array
     * containing result of each call using a given [gasLimit]. If [gasLimit]
     * is reached during execution of calls, returns all the available
     * results up to that point and the index of the last successful call.
     *
     * @return the response tuple containing results and last successful call
     *     index
     */
    @View
    suspend fun multicallWithGasLimitation(calls: EthArray<Call>, gasLimit: EthUint): Response

    /** Container for a single call request. */
    data class Call(val to: EthAddress, val data: EthBytes) : EthStructContainer

    /**
     * Container for response tuple, containing call results and the index of
     * last successful call.
     */
    data class Response(val results: EthArray<EthBytes>, val lastSuccessIndex: EthUint) : EthTupleContainer

}