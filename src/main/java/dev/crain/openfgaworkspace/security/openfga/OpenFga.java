package dev.crain.openfgaworkspace.security.openfga;

import dev.openfga.sdk.api.client.OpenFgaClient;
import dev.openfga.sdk.api.client.model.ClientCheckRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenFga {

    private final OpenFgaClient openFgaClient;
    private static final String TUPLE = "%s:%s";

    public boolean check(Authentication authentication, String userType, String objectId, String objectType, String relationship) {
        log.info("Building request");
        var checkRequest = new ClientCheckRequest()
                .user(TUPLE.formatted(userType, authentication.getName()))
                .relation(relationship)
                ._object(TUPLE.formatted(objectType, objectId));
        try {
            log.info("Authorization Check worked?");
            return Boolean.TRUE.equals( // IntelliJ wants to be a bit more verbose than I'd normally be.
                    openFgaClient.check(checkRequest).get().getAllowed()
            );
        } catch (Exception e) {
            log.error("Failed to check Authorization, check your connection and configuration. {} ", e.getMessage(), e);
            return false;
        }
    }
}
