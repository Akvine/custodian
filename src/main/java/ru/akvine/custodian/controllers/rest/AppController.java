package ru.akvine.custodian.controllers.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.custodian.controllers.rest.converters.AppConverter;
import ru.akvine.custodian.controllers.rest.dto.app.AppCreateRequest;
import ru.akvine.custodian.controllers.rest.dto.common.Response;
import ru.akvine.custodian.controllers.rest.meta.AppControllerMeta;
import ru.akvine.custodian.services.AppService;
import ru.akvine.custodian.services.domain.AppBean;
import ru.akvine.custodian.services.dto.app.AppCreate;

@RestController
@RequiredArgsConstructor
public class AppController implements AppControllerMeta {
    private final AppConverter appConverter;
    private final AppService appService;

    @Override
    public Response create(@RequestBody @Valid AppCreateRequest request) {
        AppCreate appCreate = appConverter.convertToAppCreate(request);
        AppBean createdAppBean = appService.create(appCreate);
        return appConverter.convertToAppCreateResponse(createdAppBean);
    }
}
