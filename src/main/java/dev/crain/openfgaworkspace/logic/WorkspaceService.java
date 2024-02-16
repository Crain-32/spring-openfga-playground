package dev.crain.openfgaworkspace.logic;

import dev.openfga.sdk.api.client.OpenFgaClient;
import dev.openfga.sdk.api.client.model.ClientGetStoreResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final OpenFgaClient openFgaClient;

    @PreAuthorize("@openFga.check(authentication, 'user', @openFgaProperties.getDefaultStoreId(), 'stores', 'view')")
    public ClientGetStoreResponse getStoreInformation() {
        ClientGetStoreResponse response;
        try {
            response = openFgaClient.getStore().get();
        } catch (Exception e) {
            log.error("Failed to query the store? {}", e.getMessage(), e);
            response = null;
        }
        return response;
    }
}
