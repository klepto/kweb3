package dev.klepto.kweb3.kotlin

import dev.klepto.kweb3.core.Web3Client
import dev.klepto.kweb3.core.chain.Web3Endpoint
import dev.klepto.kweb3.core.contract.log.ContractCallLog
import kotlinx.coroutines.*

/**
 * [Web3Client] implementation that support suspend functions by use of
 * [CoroutineContractParser] and [CoroutineContractExecutor].
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
class CoroutineWeb3Client(
    vararg endpoints: Web3Endpoint,
) : Web3Client(*endpoints) {
    init {
        contracts.executor = CoroutineContractExecutor()
        contracts.parser = CoroutineContractParser()
    }

    /**
     * Executes a contract call and logs it.
     *
     * @param call the contract call
     * @return the resulting contract call log
     */
    suspend fun <T> log(call: suspend () -> T): ContractCallLog {
        return log(listOf(call)).first()
    }

    /**
     * Executes a list of contract calls and logs them.
     *
     * @param calls the list of contract calls
     * @return the resulting list of contract call logs
     */
    suspend fun <T> log(calls: List<suspend () -> T>): List<ContractCallLog> {
        val executor = contracts.executor as CoroutineContractExecutor
        val logger = CoroutineContractExecutor.LoggingInterceptor()
        val scope = CoroutineScope(Dispatchers.Unconfined)
        executor.withInterceptor(logger) {
            calls.forEach {
                scope.launch {
                    executor.mutexContext.set(currentCoroutineContext())
                    it()
                }
            }
        }
        scope.cancel()
        return logger.logs
    }
}
