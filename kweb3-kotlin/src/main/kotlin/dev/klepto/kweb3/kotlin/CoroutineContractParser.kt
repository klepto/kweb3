package dev.klepto.kweb3.kotlin

import dev.klepto.kweb3.core.abi.descriptor.TypeDescriptor
import dev.klepto.kweb3.core.contract.ContractCodec
import dev.klepto.kweb3.core.contract.DefaultContractParser
import dev.klepto.kweb3.core.contract.annotation.ArraySize
import dev.klepto.kweb3.core.contract.annotation.ValueSize
import dev.klepto.unreflect.UnreflectType
import java.lang.reflect.Method
import kotlin.reflect.full.findAnnotations
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
     * @return the contract JVM return type
     */
    override fun parseReturnType(method: Method): UnreflectType {
        // If it's not a kotlin contract implementation,  parse normally.
        val kotlinFunction = method.kotlinFunction ?: return super.parseReturnType(method)
        return UnreflectType.of(kotlinFunction.returnType.javaType)
    }

    /**
     * Parse return type ABI descriptor using kotlin reflection. Using JVM
     * reflection will cause issues if contract function contains suspend
     * keyword as compiler will modify its signature. Kotlin's reflection hides
     * this information and represents function as it is written in the source
     * code.
     *
     * @param method the contract interface method
     * @return the contract return type description
     */
    override fun parseReturnTypeDescriptor(method: Method): TypeDescriptor {
        val kotlinFunction = method.kotlinFunction ?: return super.parseReturnTypeDescriptor(method)
        val type = UnreflectType.of(kotlinFunction.returnType.javaType)
        val valueSize = kotlinFunction.findAnnotations(ValueSize::class)
                .map(ValueSize::value)
                .getOrNull(0) ?: -1
        val arraySize = kotlinFunction.findAnnotations(ArraySize::class)
                .map(ArraySize::value)
                .getOrNull(0) ?: -1
        return ContractCodec.parseDescriptor(type, valueSize, arraySize)
    }

}