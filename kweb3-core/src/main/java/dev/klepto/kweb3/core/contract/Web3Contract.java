package dev.klepto.kweb3.core.contract;

import dev.klepto.kweb3.core.Web3Client;
import dev.klepto.kweb3.core.config.Web3Network;
import dev.klepto.kweb3.core.type.EthAddress;
import org.jetbrains.annotations.NotNull;

/**
 * Main interface for all smart contracts.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Web3Contract {

    /**
     * Returns the client that initialized this smart contract and is responsible for its execution.
     *
     * @return the client that that is responsible for this smart contract
     */
    @NotNull
    Web3Client getClient();

    /**
     * Returns the network that this smart contract is deployed on.
     *
     * @return the network that this smart contract is deployed on
     */
    default Web3Network getNetwork() {
        return getClient().getNetwork();
    }

    /**
     * Returns smart contract's blockchain address.
     *
     * @return the smart contract address
     */
    @NotNull
    EthAddress getAddress();

    /**
     * Returns original contract class. The default {@link Class#getClass()} can get over-shadowed by various contract
     * implementations such as {@link java.lang.reflect.Proxy} using {@link java.lang.reflect.InvocationHandler}. This
     * method is to be used instead to get actual contract type.
     *
     * @return the actual contract class
     */
    @NotNull
    Class<? extends Web3Contract> getContractClass();

    /**
     * Returns instance of {@link ContractProxy} associated with this smart contract interface.
     *
     * @return the contract proxy associated with this smart contract
     */
    ContractProxy getProxy();

}
