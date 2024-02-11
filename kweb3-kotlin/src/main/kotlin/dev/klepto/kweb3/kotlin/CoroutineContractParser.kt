package dev.klepto.kweb3.kotlin

import dev.klepto.kweb3.core.contract.DefaultContractParser
import dev.klepto.unreflect.UnreflectType
import java.lang.reflect.Method
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.kotlinFunction

/**
 * Contract function parser implementation supporting coroutine (suspend)
 * signatures.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
class CoroutineContractParser : DefaultContractParser() {

    /**
     * Parse return type using kotlin reflection. Using JVM reflection
     * will cause issues if contract function contains suspend keyword as
     * compiler will modify its signature. Kotlin's reflection hides this
     * information and represents function as it is written in the source code.
     *
     * @param method the contract interface method
     * @return the contract return type description
     */
    override fun parseReturnType(method: Method): UnreflectType {
        // If it's not a kotlin contract implementation, parse normally.
        val kotlinFunction = method.kotlinFunction ?: return super.parseReturnType(method)
        return UnreflectType.of(kotlinFunction.returnType.javaType)
    }

}