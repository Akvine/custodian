package ru.akvine.custodian.integration.rest.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.common.Response;
import ru.akvine.common.SuccessfulResponse;
import ru.akvine.custodian.core.enums.AccessRights;
import ru.akvine.custodian.core.services.PropertyService;
import ru.akvine.custodian.core.services.domain.AccessTokenModel;
import ru.akvine.custodian.core.services.domain.PropertyModel;
import ru.akvine.custodian.core.services.dto.property.PropertyDelete;
import ru.akvine.custodian.core.services.dto.property.PropertyList;
import ru.akvine.custodian.integration.rest.converters.ApiPropertyConverter;
import ru.akvine.custodian.integration.rest.dto.ApiPropertyDeleteRequest;
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
    public Response list(@RequestHeader("Authorization") String token,
                         @Valid ApiPropertyListRequest request) {
        AccessTokenModel accessTokenModel = securityService.check(token);
        securityService.checkAccess(accessTokenModel, AccessRights.RETRIEVE);
        PropertyList propertyList = apiPropertyConverter.convertToPropertyList(request, accessTokenModel);
        List<PropertyModel> properties = propertyService.list(propertyList);
        return apiPropertyConverter.convertToPropertyListResponse(properties);
    }

    @Override
    public Response delete(@RequestHeader("Authorization") String token,
                           @Valid ApiPropertyDeleteRequest request) {
        AccessTokenModel accessToken = securityService.check(token);
        securityService.checkAccess(accessToken, AccessRights.DELETE);
        PropertyDelete propertyDelete = apiPropertyConverter.convertToPropertyDelete(request, accessToken);
        propertyService.delete(propertyDelete);
        return new SuccessfulResponse();
    }
}
