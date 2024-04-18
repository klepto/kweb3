package dev.klepto.kweb3.core.chain.auth;

import dev.klepto.kweb3.core.chain.Web3Endpoint;

/**
 * Implements an authorization method for a {@link Web3Endpoint}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Authorization {

    /**
     * Returns an authorized version of {@link Web3Endpoint}.
     *
     * @param endpoint the endpoint to authorize
     * @return the authorized endpoint
     */
    Web3Endpoint authorize(Web3Endpoint endpoint);

}
