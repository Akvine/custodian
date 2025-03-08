package ru.akvine.custodian.admin.controllers.rest.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.custodian.admin.controllers.rest.dto.app.*;
import ru.akvine.custodian.admin.controllers.rest.utils.SecurityHelper;
import ru.akvine.custodian.core.services.domain.AppBean;
import ru.akvine.custodian.core.services.dto.app.AppCreate;
import ru.akvine.custodian.core.services.dto.app.AppDelete;
import ru.akvine.custodian.core.services.dto.app.AppUpdate;
import ru.akvine.custodian.core.utils.Asserts;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppConverter {
    private final SecurityHelper securityHelper;

    public AppListResponse convertToAppListResponse(List<AppBean> appBeans) {
        Asserts.isNotNull(appBeans, "appBeans is null");
        return new AppListResponse()
                .setCount(appBeans.size())
                .setApps(appBeans.stream().map(this::buildAppDto).toList());
    }

    public AppCreate convertToAppCreate(AppCreateRequest request) {
        Asserts.isNotNull(request, "appCreateRequest is null");
        return new AppCreate()
                .setClientUuid(securityHelper.getCurrentUser().getUuid())
                .setTitle(request.getTitle().toLowerCase().trim())
                .setDescription(request.getDescription());
    }

    public AppUpdate convertToAppUpdate(AppUpdateRequest request) {
        Asserts.isNotNull(request, "appUpdateRequest is null");
        return new AppUpdate()
                .setClientId(securityHelper.getCurrentUser().getId())
                .setAppTitle(request.getTitle())
                .setNewTitle(request.getNewTitle())
                .setNewDescription(request.getNewDescription());
    }

    public AppDelete convertToAppDelete(String title) {
        Asserts.isNotNull(title, "title is null");
        return new AppDelete()
                .setAppTitle(title)
                .setClientId(securityHelper.getCurrentUser().getId());
    }

    public AppCreateResponse convertToAppCreateResponse(AppBean appBean) {
        Asserts.isNotNull(appBean, "appBean is null");
        return new AppCreateResponse().setApp(buildAppDto(appBean));
    }

    private AppDto buildAppDto(AppBean appBean) {
        return new AppDto()
                .setTitle(appBean.getTitle())
                .setDescription(appBean.getDescription());
    }
}
