package dev.klepto.kweb3.kotlin

import dev.klepto.kweb3.core.chain.endpoint.PublicNode
import dev.klepto.kweb3.core.ethereum.type.EthValue
import dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress
import dev.klepto.kweb3.kotlin.CoroutineWeb3Client.Companion.contract
import dev.klepto.kweb3.kotlin.contracts.Erc20
import dev.klepto.kweb3.kotlin.multicall.MulticallContract.Companion.builder
import dev.klepto.kweb3.kotlin.multicall.contract.Multicall3
import kotlinx.coroutines.runBlocking

private val walletAddress = EthAddress.ZERO

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
fun main() {
    val client = CoroutineWeb3Client(PublicNode.BSC)
    val wbnb = client.contract<Erc20>("0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c")
    runBlocking {
        println(wbnb.name())
        println(wbnb.totalSupply())
        println(wbnb.decimals())
    }

    println()

    val multicall = client.contract<Multicall3>("0xcA11bde05977b3631167028862bE2a173976CA11")




    runBlocking {
        val result = multicall.builder<EthValue>()
            .contract<Erc20>("0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c") {
                call { name() }
                call { totalSupply() }
                call { decimals() }
            }.execute()

        result.forEach(::println)
    }

}