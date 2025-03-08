package ru.akvine.custodian.admin.controllers.rest.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ru.akvine.common.Response;
import ru.akvine.common.SuccessfulResponse;
import ru.akvine.custodian.admin.controllers.rest.converters.AppConverter;
import ru.akvine.custodian.admin.controllers.rest.dto.app.AppCreateRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.app.AppUpdateRequest;
import ru.akvine.custodian.admin.controllers.rest.meta.AppControllerMeta;
import ru.akvine.custodian.admin.controllers.rest.utils.SecurityHelper;
import ru.akvine.custodian.core.services.AppService;
import ru.akvine.custodian.core.services.domain.AppBean;
import ru.akvine.custodian.core.services.dto.app.AppCreate;
import ru.akvine.custodian.core.services.dto.app.AppDelete;
import ru.akvine.custodian.core.services.dto.app.AppUpdate;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class AppController implements AppControllerMeta {
    private final AppConverter appConverter;
    private final AppService appService;
    private final SecurityHelper securityHelper;

    @Override
    public Response list() {
        long clientId = securityHelper.getCurrentUser().getId();
        List<AppBean> apps = appService.list(clientId);
        return appConverter.convertToAppListResponse(apps);
    }

    @Override
    public Response create(@RequestBody @Valid AppCreateRequest request) {
        AppCreate appCreate = appConverter.convertToAppCreate(request);
        AppBean createdAppBean = appService.create(appCreate);
        return appConverter.convertToAppCreateResponse(createdAppBean);
    }

    @Override
    public Response update(@RequestBody @Valid AppUpdateRequest request) {
        AppUpdate appUpdate = appConverter.convertToAppUpdate(request);
        AppBean updatedApp = appService.update(appUpdate);
        return appConverter.convertToAppListResponse(List.of(updatedApp));
    }

    @Override
    public Response delete(@PathVariable("title") String appTitle) {
        AppDelete appDelete = appConverter.convertToAppDelete(appTitle);
        appService.delete(appDelete);
        return new SuccessfulResponse();
    }
}
