package dev.klepto.kweb3.kotlin.multicall

import dev.klepto.kweb3.core.type.EthType
import dev.klepto.kweb3.kotlin.contract.Multicall3

/**
 * Extensions for [Multicall3].
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
object Multicall3Extensions {

    /**
     * Creates a [MulticallExecutor] with given [calls] and immediately
     * executes it.
     *
     * @param calls one or more calls to be executed by multicall executor
     * @return a list containing call results
     */
    suspend fun <T> Multicall3.execute(vararg calls: suspend () -> T): List<T?> {
        return build(*calls).execute()
    }

    /**
     * Creates a [MulticallExecutor] with given [calls].
     *
     * @param calls one or more calls to be executed by multicall executor
     * @return instance of multicall executor containing given calls
     */
    suspend fun <T> Multicall3.build(vararg calls: suspend () -> T): MulticallExecutor<T> {
        return MulticallExecutor(this, calls.asList())
    }

    /**
     * Creates an empty [MulticallBuilder] of [EthType] type.
     *
     * @return new instance of multicall builder
     */
    fun Multicall3.builder(): MulticallBuilder<EthType> {
        return MulticallBuilder(this)
    }

    /**
     * Creates a [MulticallBuilder] by inferring from [MulticallBuilder.call]
     * calls in a given code [block].
     *
     * @return a new instace of type-bound multicall builder
     */
    fun <T> Multicall3.builder(block: (MulticallBuilder<T>.() -> MulticallBuilder<T>)): MulticallBuilder<T> {
        return MulticallBuilder<T>(this).apply { block.invoke(this) }
    }

}