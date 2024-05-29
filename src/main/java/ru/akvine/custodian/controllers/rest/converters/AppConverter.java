package ru.akvine.custodian.controllers.rest.converters;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import ru.akvine.custodian.controllers.rest.dto.app.AppCreateRequest;
import ru.akvine.custodian.controllers.rest.dto.app.AppCreateResponse;
import ru.akvine.custodian.controllers.rest.dto.app.AppDto;
import ru.akvine.custodian.services.domain.AppBean;
import ru.akvine.custodian.services.dto.app.AppCreate;

@Component
public class AppConverter {

    public AppCreate convertToAppCreate(AppCreateRequest request) {
        Preconditions.checkNotNull(request, "appCreateRequest is null");
        return new AppCreate()
                .setClientUuid(request.getClientUuid())
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
