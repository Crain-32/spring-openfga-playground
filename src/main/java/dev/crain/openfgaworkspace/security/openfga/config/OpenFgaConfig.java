package dev.crain.openfgaworkspace.security.openfga.config;

import dev.openfga.sdk.api.client.OpenFgaClient;
import dev.openfga.sdk.api.configuration.ApiToken;
import dev.openfga.sdk.api.configuration.ClientConfiguration;
import dev.openfga.sdk.api.configuration.ClientCredentials;
import dev.openfga.sdk.api.configuration.Credentials;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
public class OpenFgaConfig {

    @Bean
    public ExecutorService threadPoolExecutor() {
        return Executors.newCachedThreadPool();
    }

    @Bean
    @SneakyThrows // Yes this is messy, but we're testing right now
    @ConditionalOnMissingBean(OpenFgaClient.class)
    public OpenFgaClient openFgaClient(OpenFgaProperties properties) {
        var config = new ClientConfiguration()
                .apiUrl(properties.getUrl())
                .storeId(properties.getDefaultStoreId())
                .authorizationModelId(properties.getAuthorizationModel());
        Credentials credentials;
        // Need some proper handling for no-auth.
        if (properties.getToken() != null) {
            log.debug("Configuring OpenFGA with an API Token.");
            credentials = new Credentials(
                    new ApiToken(properties.getToken().getValue())
            );
        } else if (properties.getAuthZero() != null) {
            log.debug("Configuring OpenFGA with an Auth0 Client");
            var clientProps = properties.getAuthZero();
            credentials = new Credentials(
                    new ClientCredentials()
                            .apiTokenIssuer(clientProps.getIssuer())
                            .apiAudience(clientProps.getAudience())
                            .clientId(clientProps.getClientId())
                            .clientSecret(clientProps.getClientSecret())
            );
        } else if (properties.getOauth() != null) {
            log.debug("Configuring OpenFGA with an OAuth Client");
            var clientProps = properties.getOauth();
            credentials = new Credentials(
                    new ClientCredentials()
                            .apiTokenIssuer(clientProps.getApiTokenIssuer())
                            .scopes(clientProps.getScopes())
                            .clientId(clientProps.getClientId())
                            .clientSecret(clientProps.getClientSecret())
            );
        } else {
            throw new IllegalStateException("Failed to find any Authorization Model for OpenFGA, but OpenFGA is enabled.");
        }
        config.credentials(credentials);
        return new OpenFgaClient(config);
        // Probably want to do a connection/store id check here?
    }
}
