package dev.klepto.kweb3.kotlin

import dev.klepto.kweb3.core.Web3Result
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Extensions for [Web3Result].
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
object Web3ResultExtensions {

    /**
     * Suspends current coroutine until [Web3Result] completes or throws an
     * exception.
     */
    suspend fun <T> Web3Result<T>.await(): T {
        return suspendCoroutine { continuation ->
            get { continuation.resume(it) }
                    .error { continuation.resumeWithException(it) }
        }
    }

}