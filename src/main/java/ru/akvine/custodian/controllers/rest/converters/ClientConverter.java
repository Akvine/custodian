package ru.akvine.custodian.controllers.rest.converters;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import ru.akvine.custodian.controllers.rest.dto.client.ClientCreateRequest;
import ru.akvine.custodian.controllers.rest.dto.client.ClientCreateResponse;
import ru.akvine.custodian.controllers.rest.dto.client.ClientDto;
import ru.akvine.custodian.services.domain.ClientBean;
import ru.akvine.custodian.services.dto.client.ClientCreate;

@Component
public class ClientConverter {
    public ClientCreate convertToClientCreate(ClientCreateRequest request) {
        Preconditions.checkNotNull(request, "clientCreateRequest is null");
        return new ClientCreate()
                .setAge(request.getAge())
                .setEmail(request.getEmail())
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName());
    }

    public ClientCreateResponse convertToClientCreateResponse(ClientBean clientBean) {
        Preconditions.checkNotNull(clientBean, "clientBean is null");
        return new ClientCreateResponse().setClient(buildClientDto(clientBean));
    }

    private ClientDto buildClientDto(ClientBean clientBean) {
        return new ClientDto()
                .setUuid(clientBean.getUuid())
                .setAge(clientBean.getAge())
                .setFirstName(clientBean.getFirstName())
                .setLastName(clientBean.getLastName())
                .setEmail(clientBean.getEmail());
    }
}
