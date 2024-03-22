package dev.klepto.kweb3.kotlin

import dev.klepto.kweb3.core.Web3Client
import dev.klepto.kweb3.core.config.Web3Network
import dev.klepto.kweb3.core.contract.log.ContractCallLog
import dev.klepto.kweb3.core.contract.log.LoggingContractExecutor
import dev.klepto.kweb3.core.contract.log.LoggingContractExecutor.LoggingException

/**
 * [Web3Client] implementation that support suspend functions by use of
 * [CoroutineContractParser] and [CoroutineContractExecutor].
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
class CoroutineWeb3Client(
    network: Web3Network,
) : Web3Client(network) {

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
        val logger = LoggingContractExecutor()
        executor.withInterceptor(logger) {
            calls.forEach {
                try {
                    it()
                } catch (cause: LoggingException) {
                    // Ignore
                }
            }
        }
        return logger.logs
    }

}
