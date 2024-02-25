package dev.klepto.kweb3.kotlin.multicall

import dev.klepto.kweb3.kotlin.contracts.Multicall3

/**
 * A type-bound [Multicall3] builder.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
class MulticallBuilder<T>(
    private val contract: Multicall3,
    private val calls: MutableList<suspend () -> T> = mutableListOf()
) {

    /**
     * Appends a new call to the list of calls associated with builder.
     *
     * @param call the call code
     * @return this builder instance
     */
    fun call(call: suspend () -> T): MulticallBuilder<T> {
        calls.add(call)
        return this
    }

    /**
     * Appends a new calls to the list by supplying range indices to a call
     * function.
     *
     * @param range the range of indices
     * @param call the call code
     * @return this builder instance
     */
    fun callRange(range: IntRange, call: suspend (index: Int) -> T): MulticallBuilder<T> {
        for (i in range) {
            calls.add(suspend { call(i) })
        }
        return this
    }

    /**
     * Creates a new [MulticallExecutor] for list of calls associated with this
     * builder.
     *
     * @return a new instance of multicall executor using calls from this
     *     builder
     */
    fun build(): MulticallExecutor<T> {
        return MulticallExecutor(contract, calls)
    }

    /**
     * Creates a new [MulticallExecutor] for list of calls associated with this
     * builder and executes it.
     *
     * @param batchSize the maximum size of a multicall batch, `1024` by
     *     default
     * @return a list containing call results
     */
    suspend fun execute(batchSize: Int = 1024): List<T?> {
        return build().execute(batchSize)
    }

}