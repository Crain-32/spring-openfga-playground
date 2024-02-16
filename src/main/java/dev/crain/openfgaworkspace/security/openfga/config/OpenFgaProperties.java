package dev.crain.openfgaworkspace.security.openfga.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(value = "spring.security.openfga")
public class OpenFgaProperties {

    private boolean enabled;
    private String url;
    private String defaultStoreId;
    private String authorizationModel; // This should be more dynamic, but we'll avoid that for now.
    private ApiToken token;
    private AuthZeroCredentials authZero;
    private OAuthTwoCredentials oauth;

    @Getter
    @Setter
    public static class ApiToken {
        private String value;
    }

    @Getter
    @Setter
    public static class AuthZeroCredentials {
        private String issuer;
        private String audience;
        private String clientId;
        private String clientSecret;
    }

    @Getter
    @Setter
    public static class OAuthTwoCredentials {
        private String apiTokenIssuer;
        private String scopes;
        private String clientId;
        private String clientSecret;
    }
}
