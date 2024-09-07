package ru.akvine.custodian.admin.controllers.rest.meta;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.custodian.admin.controllers.rest.dto.app.AppCreateRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.common.Response;

@RequestMapping(value = "/apps")
public interface AppControllerMeta {
    @GetMapping
    Response list();

    @PostMapping(value = "/create")
    Response create(@RequestBody @Valid AppCreateRequest request);
}
