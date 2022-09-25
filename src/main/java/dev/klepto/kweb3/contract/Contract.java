package dev.klepto.kweb3.contract;

import dev.klepto.kweb3.type.Address;

/**
 * Default interface for all blockchain contracts. Contains {@link Contract#getAddress()} method that applies to
 * all contracts.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Contract {

    Address getAddress();

}
