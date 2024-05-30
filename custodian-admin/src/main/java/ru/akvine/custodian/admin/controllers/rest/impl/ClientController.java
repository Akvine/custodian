package ru.akvine.custodian.admin.controllers.rest.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.custodian.admin.controllers.rest.converters.ClientConverter;
import ru.akvine.custodian.admin.controllers.rest.dto.client.ClientCreateRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.common.Response;
import ru.akvine.custodian.admin.controllers.rest.meta.ClientControllerMeta;
import ru.akvine.custodian.admin.controllers.rest.validators.ClientValidator;
import ru.akvine.custodian.core.services.ClientService;
import ru.akvine.custodian.core.services.domain.ClientBean;
import ru.akvine.custodian.core.services.dto.client.ClientCreate;

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
