package ru.akvine.custodian.controllers.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.custodian.controllers.rest.converters.ClientConverter;
import ru.akvine.custodian.controllers.rest.dto.client.ClientCreateRequest;
import ru.akvine.custodian.controllers.rest.dto.common.Response;
import ru.akvine.custodian.controllers.rest.meta.ClientControllerMeta;
import ru.akvine.custodian.controllers.rest.validators.ClientValidator;
import ru.akvine.custodian.services.ClientService;
import ru.akvine.custodian.services.domain.ClientBean;
import ru.akvine.custodian.services.dto.client.ClientCreate;

@RestController
@RequiredArgsConstructor
public class ClientController implements ClientControllerMeta {
    private final ClientValidator clientValidator;
    private final ClientService clientService;
    private final ClientConverter clientConverter;

    @Override
    public Response create(@RequestBody @Valid ClientCreateRequest request) {
        clientValidator.verifyClientCreateRequest(request);
        ClientCreate clientCreate = clientConverter.convertToClientCreate(request);
        ClientBean clientBean = clientService.create(clientCreate);
        return clientConverter.convertToClientCreateResponse(clientBean);
    }
}
