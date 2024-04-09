package dev.klepto.kweb3.kotlin

import dev.klepto.kweb3.core.Web3Error
import dev.klepto.kweb3.core.Web3Result
import dev.klepto.kweb3.core.contract.ContractCall
import dev.klepto.kweb3.core.contract.ContractExecutor
import dev.klepto.kweb3.core.contract.DefaultContractExecutor
import dev.klepto.kweb3.core.contract.log.LoggingContractExecutor
import dev.klepto.kweb3.kotlin.ContractCallExtensions.continuation
import dev.klepto.kweb3.kotlin.ContractCallExtensions.isSuspending
import dev.klepto.kweb3.kotlin.Web3ResultExtensions.await
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED

/**
 * Contract executor implementation supporting coroutine (suspend)
 * functions.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
class CoroutineContractExecutor : DefaultContractExecutor() {
    val mutex = Mutex(false)
    val mutexContext = AtomicReference<Any>()
    val interceptor = AtomicReference<ContractExecutor>()

    /**
     * Executes a contract function from start to finish with a lock provided
     * by [CoroutineWeb3Client] mutex.
     *
     * @param call the contract interface method call
     * @return the contract function execution result, the type must match
     *     return type of the interface method
     */
    override fun execute(call: ContractCall): Any {
        try {
            if (call.isSuspending()) {
                return ::executeMutexSuspending.call(call, call.continuation())
            }
        } catch (cause: Throwable) {
            throw Web3Error.unwrap(cause)
        }
        return super.execute(call)
    }

    /**
     * Executes smart contract interface call by suspending current coroutine
     * while respecting [mutex] lock.
     *
     * @param call the contract interface method call
     * @return the decoded smart contract result
     */
    suspend fun executeMutexSuspending(call: ContractCall): Any {
        val context = currentCoroutineContext()
        if (mutex.isLocked && mutexContext.get() != context) {
            return mutex.withLock(context) {
                executeSuspending(call)
            }
        }
        return executeSuspending(call)
    }

    /**
     * Executes smart contract interface call by suspending current coroutine
     * until result is received.
     *
     * @param call the contract interface method call
     * @return the decoded smart contract result
     */
    suspend fun executeSuspending(call: ContractCall): Any {
        val interceptor = this.interceptor.get()
        if (interceptor != null) {
            return interceptor.execute(call)
        }

        val data = encode(call)
        val result = request(call, data).await()
        return decodeResult(call, result)
    }

    /**
     * Executes given code block by locking this contract executor to a given
     * [ContractExecutor] interceptor.
     *
     * @param interceptor the interceptor to be used instead of this executor
     * @param block the code block
     */
    suspend fun withInterceptor(
        interceptor: ContractExecutor,
        block: suspend () -> Unit,
    ) {
        mutex.withLock {
            mutexContext.set(currentCoroutineContext())
            val current = this.interceptor.get()
            this.interceptor.set(interceptor)
            block()
            this.interceptor.set(current)
        }
    }

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
}
