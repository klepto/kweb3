package dev.klepto.kweb3.kotlin.oneinch.multicall

import dev.klepto.kweb3.core.type.EthType

/**
 * Extensions for [OneInchMulticall].
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
object OneInchMulticallExtensions {

    /**
     * Creates a [MulticallExecutor] with given [calls] and immediately
     * executes it.
     *
     * @param calls one or more calls to be executed by multicall executor
     * @return a list containing call results
     */
    suspend fun <T> OneInchMulticall.execute(vararg calls: suspend () -> T): List<T?> {
        return build(*calls).execute()
    }

    /**
     * Creates a [MulticallExecutor] with given [calls].
     *
     * @param calls one or more calls to be executed by multicall executor
     * @return instance of multicall executor containing given calls
     */
    fun <T> OneInchMulticall.build(vararg calls: suspend () -> T): MulticallExecutor<T> {
        return MulticallExecutor(this, calls.asList())
    }

    /**
     * Creates an empty [MulticallBuilder] of [EthType] type.
     *
     * @return new instance of multicall builder
     */
    fun OneInchMulticall.builder(): MulticallBuilder<EthType> {
        return MulticallBuilder(this)
    }

    /**
     * Creates a [MulticallBuilder] by inferring from [MulticallBuilder.call]
     * calls in a given code [block].
     *
     * @return a new instace of type-bound multicall builder
     */
    fun <T> OneInchMulticall.builder(block: (MulticallBuilder<T>.() -> MulticallBuilder<T>)): MulticallBuilder<T> {
        return MulticallBuilder<T>(this).apply { block.invoke(this) }
    }

}