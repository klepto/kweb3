package dev.klepto.kweb3.kotlin.multicall

import dev.klepto.kweb3.core.contract.Web3Contract
import dev.klepto.kweb3.core.contract.type.EthStructContainer
import dev.klepto.kweb3.core.type.*

/**
 * Declares a common multicall smart contract signature to encapsulate most
 * multicall contract implementations.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
interface MulticallContract : Web3Contract {

    /**
     * Attempts to execute a multicall request with given parameters. For any
     * failed calls, returns empty bytes, for successful calls return bytes
     * containing return data.
     *
     * @param gasLimit the gas limit of single call execution
     * @param sizeLimit the size limit of single call execution
     * @param calls an array of calls to be executed
     * @return an ordered array containing resulting bytes (possibly empty, if
     *     call failed) of each call
     */
    suspend fun tryAggregate(
            gasLimit: EthUint,
            sizeLimit: EthUint,
            calls: EthArray<Call>
    ): EthArray<EthBytes>

    /**
     * Creates a [MulticallExecutor] with given [calls] and immediately
     * executes it.
     *
     * @param calls one or more calls to be executed by multicall executor
     * @return a list containing call results
     */
    suspend fun <T> execute(vararg calls: suspend () -> T): List<T?> {
        return build(*calls).execute()
    }

    /**
     * Creates a [MulticallExecutor] with given [calls].
     *
     * @param calls one or more calls to be executed by multicall executor
     * @return instance of multicall executor containing given calls
     */
    suspend fun <T> build(vararg calls: suspend () -> T): MulticallExecutor<T> {
        return MulticallExecutor(this, calls.asList())
    }

    /**
     * Creates an empty [MulticallBuilder] of [EthType] type.
     *
     * @return new instance of multicall builder
     */
    fun builder(): MulticallBuilder<EthType> {
        return MulticallBuilder(this)
    }

    /**
     * Creates a [MulticallBuilder] by inferring from [MulticallBuilder.call]
     * calls in a given code [block].
     *
     * @return a new instace of type-bound multicall builder
     */
    fun <T> builder(block: (MulticallBuilder<T>.() -> MulticallBuilder<T>)): MulticallBuilder<T> {
        return MulticallBuilder<T>(this).apply { block.invoke(this) }
    }

    /**
     * Represents a single multicall call.
     *
     * @author <a href="http://github.com/klepto">Augustinas R.</a>
     */
    data class Call(val address: EthAddress, val data: EthBytes) : EthStructContainer

}