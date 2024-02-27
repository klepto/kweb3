package dev.klepto.kweb3.kotlin.contracts

import dev.klepto.kweb3.core.contract.Web3Contract
import dev.klepto.kweb3.core.contract.annotation.View
import dev.klepto.kweb3.core.contract.type.EthStructContainer
import dev.klepto.kweb3.core.type.EthAddress
import dev.klepto.kweb3.core.type.EthAddress.address
import dev.klepto.kweb3.core.type.EthArray
import dev.klepto.kweb3.core.type.EthBool
import dev.klepto.kweb3.core.type.EthBytes

/**
 * Implementation of [Multicall3](https://github.com/mds1/multicall) smart
 * contract.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
interface Multicall3 : Web3Contract {

    companion object {
        /** The current default address of the Multicall3 smart contract. */
        val ADDRESS = address("0xcA11bde05977b3631167028862bE2a173976CA11")
    }

    /**
     * Aggregates all calls into a single request and returns an array
     * containing [results][Result] of each call.
     *
     * @param calls an ethereum array containing calls for aggregation
     * @return an ethereum array containing result of each call
     */
    @View
    suspend fun aggregate3(calls: EthArray<Call>): EthArray<Result>

    /**
     * Contains a single smart contract call request. If `allowFailure` is set
     * to `false`, failure of this call will cause entire aggregation request
     * to fail.
     *
     * @param target the target contract address
     * @param allowFailure marks that aggregation request is allowed to
     *     continue upon failure of this call
     * @param callData the call-data contained in ethereum bytes
     */
    data class Call(val target: EthAddress, val allowFailure: EthBool, val callData: EthBytes) : EthStructContainer

    /**
     * Contains result of a single smart contract call.
     *
     * @param success true if call was successful
     * @param returnData ethereum bytes containing return data
     */
    data class Result(val success: EthBool, val returnData: EthBytes) : EthStructContainer

}