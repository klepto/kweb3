package dev.klepto.kweb3.contract;

import dev.klepto.kweb3.Web3Client;
import dev.klepto.kweb3.type.Address;

/**
 * Default interface for all blockchain contracts. Contains utility methods that apply to all contracts.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Contract {

    Address getAddress();

    Web3Client getClient();

}
