package dev.klepto.kweb3.kotlin.multicall

import dev.klepto.kweb3.core.Web3Result
import dev.klepto.kweb3.core.contract.ContractCall
import dev.klepto.kweb3.core.contract.Web3Contract
import dev.klepto.kweb3.core.contract.log.LoggingContractExecutor
import dev.klepto.kweb3.core.ethereum.type.EthValue
import dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress
import dev.klepto.kweb3.core.ethereum.type.primitive.EthBytes
import dev.klepto.kweb3.core.util.Hex
import dev.klepto.kweb3.kotlin.CoroutineContractExecutor
import dev.klepto.kweb3.kotlin.CoroutineWeb3Client
import kotlinx.coroutines.*
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED

/**
 * @author Augustinas R. <http://github.com/klepto>
 */
@Suppress("UNCHECKED_CAST")
class MulticallExecutor<T : EthValue>(
    private val multicall: MulticallContract,
    private val calls: List<Call<Web3Contract>>,
    private val batchSize: Int,
    private var allowFailure: Boolean
) {

    /**
     * Executes all calls in the queue and returns a list of their results. The
     * results are ordered in the same way as the calls. If [allowFailure] is
     * set `true`, results may contain `null`, indicating a failed call.
     *
     * @return a list of results from each call
     */
    suspend fun execute(): List<T?> {
        require(batchSize > 0) { "Batch size must be positive." }
        val results = mutableListOf<Deferred<List<T?>>>()
        val scope = CoroutineScope(Dispatchers.IO)
        calls.chunked(batchSize).forEach { batch ->
            results += scope.async { executeBatch(batch) }
        }
        return results.awaitAll().flatten()
    }

    /**
     * Dispatches a single batch of calls and returns a list of their results.
     *
     * @param batch the list of calls to be dispatched
     * @return a list of results from each call
     */
    private suspend fun executeBatch(batch: List<Call<Web3Contract>>): List<T?> {
        val encodedCalls = encodeCalls(batch)
        val results = multicall.execute(allowFailure, encodedCalls)
        return decodeResults(batch, results)
    }

    /**
     * Encodes all calls in the queue and returns a list of their encoded
     * [MulticallContract.Call] objects.
     *
     * @param calls the list of calls to be encoded
     * @return a list of encoded calls
     */
    private suspend fun encodeCalls(calls: List<Call<Web3Contract>>): List<MulticallContract.Call> {
        val client = CoroutineWeb3Client()
        val scope = CoroutineScope(Dispatchers.Unconfined)
        val logger = LoggingInterceptor()
        client.contracts.executor = logger
        calls.forEach { call ->
            val contract = client.contract(call.contractType, call.contractAddress)
            scope.launch {
                call.call(contract)
            }
        }
        scope.cancel()
        return logger.logs.map {
            MulticallContract.Call(it.transaction.to, it.transaction.data)
        }
    }

    /**
     * Decodes all results in the queue and returns a list of their decoded
     * responses.
     *
     * @param calls the list of calls to be decoded
     * @param results the list of results to be decoded
     * @return a list of decoded results
     */
    private suspend fun decodeResults(
        calls: List<Call<Web3Contract>>,
        results: List<EthBytes?>
    ): List<T?> {
        val client = CoroutineWeb3Client()
        val result = mutableListOf<T?>()
        results.forEachIndexed { index, data ->
            if (data == null) {
                result.add(null)
                return@forEachIndexed
            }

            val byteArray = data.toByteArray()
            if (byteArray.isEmpty()) {
                result.add(null)
                return@forEachIndexed
            }


            val call = calls[index]
            val value = Hex.toHex(byteArray)
            val interceptor = ConstantResultInterceptor(value)
            client.contracts.executor = interceptor
            val contract = client.contract(call.contractType, call.contractAddress)

            if (allowFailure) {
                try {
                    result.add(call.call(contract) as T)
                } catch (e: Exception) {
                    result.add(null)
                }
            } else {
                result.add(call.call(contract) as T)
            }
        }
        return result
    }

    /**
     * A contract call that is to be executed with the multicall.
     */
    data class Call<C : Web3Contract>(
        val contractType: Class<C>,
        val contractAddress: EthAddress,
        val call: suspend C.() -> EthValue
    )

    /**
     * A [LoggingContractExecutor] interceptor that marks execution coroutine
     * as suspended after logging a request.
     */
    class LoggingInterceptor : LoggingContractExecutor() {
        override fun request(
            call: ContractCall,
            data: String,
        ): Web3Result<String> {
            appendLog(call, data)
            return Web3Result()
        }

        override fun decode(
            call: ContractCall,
            result: Web3Result<String>,
        ): Any {
            return COROUTINE_SUSPENDED
        }
    }

    /**
     * A [CoroutineContractExecutor] interceptor that returns a constant value
     * as a web3 result.
     *
     * @param value the value to be returned as a result
     */
    private class ConstantResultInterceptor(private val value: String) : CoroutineContractExecutor() {
        override fun request(call: ContractCall, data: String): Web3Result<String> {
            val result = Web3Result<String>()
            result.complete(value)
            return result
        }
    }
}