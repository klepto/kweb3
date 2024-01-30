package dev.klepto.kweb3.contract;

import dev.klepto.kweb3.Web3Client;
import dev.klepto.kweb3.type.EthAddress;

/**
 * Default interface for all blockchain contracts. Contains utility methods that apply to all contracts.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Web3Contract {

    /**
     * Returns the client that initialized this contract and is responsible for this contract's execution.
     *
     * @return the client that that is responsible for this contract
     */
    Web3Client getClient();

    /**
     * Returns contract's blockchain address.
     *
     * @return the blockchain address
     */
    EthAddress getAddress();

    /**
     * Returns original contract class. The default {@link Class#getClass()} can get over-shadowed by various contract
     * implementations such as {@link java.lang.reflect.Proxy} using {@link java.lang.reflect.InvocationHandler}. This
     * method is to be used instead to get actual contract type.
     *
     * @return the actual contract class
     */
    Class<? extends Web3Contract> getContractClass();

}
