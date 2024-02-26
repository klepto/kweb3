package dev.klepto.kweb3.kotlin


import dev.klepto.kweb3.core.Web3Client
import dev.klepto.kweb3.core.config.Web3Network

/**
 * [Web3Client] implementation that support suspend functions by use of
 * [CoroutineContractParser] and [CoroutineContractExecutor].
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
class CoroutineWeb3Client(network: Web3Network) : Web3Client(network) {

    init {
        contracts.executor = CoroutineContractExecutor()
        contracts.parser = CoroutineContractParser()
    }

}