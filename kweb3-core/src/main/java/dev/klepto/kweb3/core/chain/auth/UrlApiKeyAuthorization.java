package dev.klepto.kweb3.core.chain.auth;

import dev.klepto.kweb3.core.chain.Web3Endpoint;
import lombok.RequiredArgsConstructor;
import lombok.val;

import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * Authorizes the usage of a {@link Web3Endpoint} by providing an API key in the URL.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@RequiredArgsConstructor
public class UrlApiKeyAuthorization implements Authorization {
    /**
     * The token in the URL that will be replaced with the actual API key.
     */
    public static final String TOKEN = "{API_KEY}";

    private final transient String apiKey;

    /**
     * Authorizes the {@link Web3Endpoint} by replacing {@link UrlApiKeyAuthorization#TOKEN} part of the URL with the
     * actual API key. Throws an exception if the URL does not contain the token.
     *
     * @param endpoint the endpoint to authorize
     * @return the authorized endpoint
     */
    @Override
    public Web3Endpoint authorize(Web3Endpoint endpoint) {
        val url = endpoint.url();
        require(url.contains(TOKEN), "Endpoint URL does not contain API key token: {}", TOKEN);
        val authorizedUrl = url.replace(TOKEN, apiKey);
        return endpoint.toBuilder().url(authorizedUrl).build();
    }

}
