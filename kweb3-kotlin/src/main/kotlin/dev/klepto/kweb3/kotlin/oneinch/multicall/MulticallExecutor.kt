package dev.klepto.kweb3.kotlin.oneinch.multicall

import dev.klepto.kweb3.core.Web3Result
import dev.klepto.kweb3.core.contract.ContractCall
import dev.klepto.kweb3.core.contract.DefaultContractExecutor
import dev.klepto.kweb3.core.contract.LoggingContractExecutor
import dev.klepto.kweb3.core.type.EthArray
import dev.klepto.kweb3.core.type.EthArray.array
import dev.klepto.kweb3.core.type.EthBytes
import dev.klepto.kweb3.core.type.EthBytes.bytes
import dev.klepto.kweb3.core.type.EthUint
import dev.klepto.kweb3.core.type.EthUint.uint256
import dev.klepto.kweb3.core.util.Hex
import dev.klepto.kweb3.kotlin.CoroutineContractExecutor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.math.min

/**
 * A suspending [OneInchMulticall] executor.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
class MulticallExecutor<T>(
    private val contract: OneInchMulticall,
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
     * @param gasLimit the maximum amount of gas to be used for multicall
     *     request, `150_000_000` by default
     * @return a list containing results of all successful and reverted calls
     */
    suspend fun execute(batchSize: Int = 1024, gasLimit: EthUint = uint256(150_000_000)): List<T?> {
        require(batchSize > 0) { "Batch size must be positive." }
        val executor = contract.client.contracts.executor
        require(executor is CoroutineContractExecutor) {
            "${MulticallExecutor::class.simpleName} can only " +
                    "be used from ${CoroutineContractExecutor::class.simpleName} context."
        }

        val results = mutableListOf<T?>()
        val callQueue = LinkedList(calls)
        while (!callQueue.isEmpty()) {
            val batch = callQueue.subList(0, min(batchSize, callQueue.size))
            val result = executeBatch(executor, batch, gasLimit)
            for (i in result.indices) {
                callQueue.pop()
            }
            results.addAll(result)
        }
        return results
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
        batch: List<suspend () -> T>,
        gasLimit: EthUint
    ): List<T?> {
        val encodedCalls = encodeCalls(executor, batch)
        val response = contract.multicallWithGasLimitation(encodedCalls, gasLimit)
        val results = response.results.subList(0, response.lastSuccessIndex.toInt() + 1)
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
    ): EthArray<OneInchMulticall.Call> {
        val logger = LoggingInterceptor()
        val scope = CoroutineScope(Dispatchers.Unconfined)
        executor.withInterceptor(logger) {
            calls.forEach { call ->
                scope.launch { call() }
            }
        }
        scope.cancel()

        val result = logger.logs.map {
            OneInchMulticall.Call(it.contractAddress, bytes(it.data))
        }

        return array(result)
    }

    /**
     * Decodes given list of multicall [results] using
     * [ConstantResultInterceptor].
     *
     * @param executor the coroutine contract executor
     * @param calls a list of calls
     * @param results a list containing call results
     * @return the decoded list of multicall results
     */
    private suspend fun decodeResults(
        executor: CoroutineContractExecutor,
        calls: List<suspend () -> T>,
        results: List<EthBytes>
    ): List<T?> {
        require(calls.size == results.size) {
            "Results size does not match calls size."
        }

        val result = mutableListOf<T?>()
        results.forEachIndexed { index, data ->
            val byteArray = data.toByteArray()
            if (byteArray.isEmpty()) {
                result.add(null)
                return@forEachIndexed
            }

            val value = Hex.toHex(byteArray)
            val interceptor = ConstantResultInterceptor(value)
            executor.withInterceptor(interceptor) {
                result.add(calls[index]())
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