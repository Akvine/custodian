package ru.akvine.custodian.admin.controllers.rest.converters;

import org.springframework.stereotype.Component;
import ru.akvine.custodian.admin.controllers.rest.dto.client.ClientCreateRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.client.ClientCreateResponse;
import ru.akvine.custodian.admin.controllers.rest.dto.client.ClientDto;
import ru.akvine.custodian.core.services.domain.ClientModel;
import ru.akvine.custodian.core.services.dto.client.ClientCreate;
import ru.akvine.custodian.core.utils.Asserts;

@Component
public class ClientConverter {
    public ClientCreate convertToClientCreate(ClientCreateRequest request) {
        Asserts.isNotNull(request, "clientCreateRequest is null");
        return new ClientCreate()
                .setAge(request.getAge())
                .setEmail(request.getEmail())
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName());
    }

    public ClientCreateResponse convertToClientCreateResponse(ClientModel clientBean) {
        Asserts.isNotNull(clientBean, "clientBean is null");
        return new ClientCreateResponse().setClient(buildClientDto(clientBean));
    }

    private ClientDto buildClientDto(ClientModel clientBean) {
        return new ClientDto()
                .setUuid(clientBean.getUuid())
                .setAge(clientBean.getAge())
                .setFirstName(clientBean.getFirstName())
                .setLastName(clientBean.getLastName())
                .setEmail(clientBean.getEmail());
    }
}
