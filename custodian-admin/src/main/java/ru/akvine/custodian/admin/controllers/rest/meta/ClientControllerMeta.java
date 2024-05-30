package ru.akvine.custodian.admin.controllers.rest.meta;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.custodian.admin.controllers.rest.dto.client.ClientCreateRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.common.Response;

@RequestMapping(value = "/clients")
public interface ClientControllerMeta {
    @PostMapping(value = "/create")
    Response create(@RequestBody @Valid ClientCreateRequest request);
}
