package ru.akvine.custodian.integration.rest.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.common.Response;
import ru.akvine.custodian.core.enums.AccessRights;
import ru.akvine.custodian.core.services.PropertyService;
import ru.akvine.custodian.core.services.domain.AccessTokenModel;
import ru.akvine.custodian.core.services.domain.PropertyModel;
import ru.akvine.custodian.core.services.dto.property.PropertyList;
import ru.akvine.custodian.integration.rest.converters.ApiPropertyConverter;
import ru.akvine.custodian.integration.rest.dto.ApiPropertyListRequest;
import ru.akvine.custodian.integration.rest.meta.ApiPropertyControllerMeta;
import ru.akvine.custodian.integration.services.SecurityService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApiPropertyController implements ApiPropertyControllerMeta {
    private final PropertyService propertyService;
    private final SecurityService securityService;
    private final ApiPropertyConverter apiPropertyConverter;

    @Override
    public Response list(@RequestHeader("authorization") String token,
                         @Valid ApiPropertyListRequest request) {
        AccessTokenModel accessTokenBean = securityService.check(token);
        securityService.checkAccess(accessTokenBean, AccessRights.RETRIEVE);
        PropertyList propertyList = apiPropertyConverter.convertToPropertyList(request, accessTokenBean);
        List<PropertyModel> properties = propertyService.list(propertyList);
        return apiPropertyConverter.convertToPropertyListResponse(properties);
    }
}
