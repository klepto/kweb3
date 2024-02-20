package dev.klepto.kweb3.kotlin.multicall

import dev.klepto.kweb3.core.Web3Result
import dev.klepto.kweb3.core.contract.ContractCall
import dev.klepto.kweb3.core.contract.DefaultContractExecutor
import dev.klepto.kweb3.core.contract.LoggingContractExecutor
import dev.klepto.kweb3.core.type.EthArray
import dev.klepto.kweb3.core.type.EthArray.array
import dev.klepto.kweb3.core.type.EthBool.bool
import dev.klepto.kweb3.core.type.EthBytes
import dev.klepto.kweb3.core.type.EthBytes.bytes
import dev.klepto.kweb3.core.util.Hex
import dev.klepto.kweb3.kotlin.CoroutineContractExecutor
import dev.klepto.kweb3.kotlin.contracts.Multicall3
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED

/**
 * A suspending [Multicall3] executor.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
class MulticallExecutor<T>(
    private val contract: Multicall3,
    private val calls: List<suspend () -> T>
) {

    /**
     * Executes all [calls] through a [Multicall3] smart contract and returns
     * an ordered list containing result of each call. For calls that failed
     * to execute due to internal error, a `null` is preserved at a matching
     * result index.
     *
     * @param batchSize the maximum size of a multicall batch, `1024` by
     *     default
     * @return a list containing results of all successful and reverted calls
     */
    suspend fun execute(batchSize: Int = 1024): List<T?> {
        require(batchSize > 0) { "Batch size must be positive." }
        val executor = contract.client.contracts.executor
        require(executor is CoroutineContractExecutor) {
            "${MulticallExecutor::class.simpleName} can only " +
                    "be used from ${CoroutineContractExecutor::class.simpleName} context."
        }

        val batches = calls.chunked(batchSize)
        return batches.flatMap {
            executeBatch(executor, it)
        }
    }

    /**
     * Executes a single multicall [batch] using provided [Multicall3]
     * contract. Every call in a given [batch] is first encoded and aggregated
     * into a single multicall request, then request is executed and multicall
     * result gets decoded into individual values that get dispatched back to
     * every call in the [batch].
     *
     * @param executor the coroutine executor to be used for this request
     * @param batch a list of smart contract calls
     * @return an ordered list containing decoded multicall results
     */
    private suspend fun executeBatch(
        executor: CoroutineContractExecutor,
        batch: List<suspend () -> T>
    ): List<T?> {
        val encodedCalls = encodeCalls(executor, batch)
        val results = array(contract.aggregate3(encodedCalls).map { it.returnData })
        return decodeResults(executor, batch, results)
    }

    /**
     * Encodes given list of smart contract [calls] using [LoggingInterceptor].
     *
     * @param executor the coroutine contract executor
     * @param calls the list of calls
     * @return an ethereum array of multicall calls
     */
    private suspend fun encodeCalls(
        executor: CoroutineContractExecutor,
        calls: List<suspend () -> T>
    ): EthArray<Multicall3.Call> {
        val logger = LoggingInterceptor()
        val scope = CoroutineScope(Dispatchers.Unconfined)
        executor.withInterceptor(logger) {
            calls.forEach { call ->
                scope.launch { call() }
            }
        }
        scope.cancel()

        val result = logger.logs.map {
            Multicall3.Call(it.contractAddress, bool(true), bytes(it.data))
        }

        return array(result)
    }

    /**
     * Decodes given list of multicall [results] using
     * [ConstantResultInterceptor].
     *
     * @param executor the coroutine contract executor
     * @param calls the list of calls
     * @param results an ethereum array of results
     * @return the decoded list of multicall results
     */
    private suspend fun decodeResults(
        executor: CoroutineContractExecutor,
        calls: List<suspend () -> T>,
        results: EthArray<EthBytes>
    ): List<T?> {
        require(calls.size == results.size) {
            "Results size does not match calls size."
        }

        val result = mutableListOf<T?>()
        calls.forEachIndexed { index, call ->
            val byteArray = results[index].toByteArray()
            if (byteArray.isEmpty()) {
                result.add(null)
                return@forEachIndexed
            }

            val value = Hex.toHex(byteArray)
            val interceptor = ConstantResultInterceptor(value)
            executor.withInterceptor(interceptor) {
                result.add(call())
            }
        }

        return result
    }

    /**
     * A [LoggingContractExecutor] interceptor that marks execution coroutine
     * as suspended after logging a request.
     */
    private class LoggingInterceptor : LoggingContractExecutor() {
        override fun decode(call: ContractCall, result: Web3Result<String>): Any {
            return COROUTINE_SUSPENDED
        }
    }

    /**
     * A [CoroutineContractExecutor] interceptor that returns a constant value
     * as a web3 result.
     *
     * @param value the value to be returned as a result
     */
    private class ConstantResultInterceptor(private val value: String) : DefaultContractExecutor() {
        override fun request(call: ContractCall, data: String): Web3Result<String> {
            val result = Web3Result<String>()
            result.complete(value)
            return result
        }
    }

}