package ru.akvine.custodian.admin.controllers.rest.converters;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.custodian.admin.controllers.rest.dto.app.AppCreateRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.app.AppCreateResponse;
import ru.akvine.custodian.admin.controllers.rest.dto.app.AppDto;
import ru.akvine.custodian.admin.controllers.rest.dto.app.AppListResponse;
import ru.akvine.custodian.admin.controllers.rest.utils.SecurityHelper;
import ru.akvine.custodian.core.services.domain.AppBean;
import ru.akvine.custodian.core.services.dto.app.AppCreate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppConverter {
    private final SecurityHelper securityHelper;

    public AppListResponse convertToAppListResponse(List<AppBean> appBeans) {
        Preconditions.checkNotNull(appBeans, "appBeans is null");
        return new AppListResponse()
                .setCount(appBeans.size())
                .setApps(appBeans.stream().map(this::buildAppDto).toList());
    }

    public AppCreate convertToAppCreate(AppCreateRequest request) {
        Preconditions.checkNotNull(request, "appCreateRequest is null");
        return new AppCreate()
                .setClientUuid(securityHelper.getCurrentUser().getUuid())
                .setTitle(request.getTitle().toLowerCase().trim())
                .setDescription(request.getDescription());
    }

    public AppCreateResponse convertToAppCreateResponse(AppBean appBean) {
        Preconditions.checkNotNull(appBean, "appBean is null");
        return new AppCreateResponse().setApp(buildAppDto(appBean));
    }

    private AppDto buildAppDto(AppBean appBean) {
        return new AppDto()
                .setTitle(appBean.getTitle())
                .setDescription(appBean.getDescription());
    }
}
