package ru.akvine.custodian.admin.controllers.rest.meta;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.akvine.common.Response;
import ru.akvine.custodian.admin.controllers.rest.dto.app.AppCreateRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.app.AppUpdateRequest;

@RequestMapping(value = "/apps")
public interface AppControllerMeta {
    @GetMapping
    Response list();

    @PostMapping(value = "/create")
    Response create(@RequestBody @Valid AppCreateRequest request);

    @PatchMapping
    Response update(@RequestBody @Valid AppUpdateRequest request);

    @DeleteMapping(value = "/{title}")
    Response delete(@PathVariable("title") String appTitle);
}
