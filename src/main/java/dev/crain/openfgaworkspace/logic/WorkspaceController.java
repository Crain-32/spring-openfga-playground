package dev.crain.openfgaworkspace.logic;

import dev.openfga.sdk.api.client.model.ClientGetStoreResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@RestController
@RequestMapping("/rest")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService service;
    @GetMapping("/openfga/store")
    public ClientGetStoreResponse getStoreInformation() {
        try {
            return service.getStoreInformation();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}
