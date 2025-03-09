package ru.akvine.custodian.integration.rest.converters;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.custodian.core.services.domain.AccessTokenModel;
import ru.akvine.custodian.core.services.domain.PropertyModel;
import ru.akvine.custodian.core.services.dto.property.PropertyList;
import ru.akvine.custodian.core.utils.Asserts;
import ru.akvine.custodian.integration.rest.dto.ApiPropertyDto;
import ru.akvine.custodian.integration.rest.dto.ApiPropertyListRequest;
import ru.akvine.custodian.integration.rest.dto.ApiPropertyListResponse;

import java.util.List;
import java.util.Set;

@Component
public class ApiPropertyConverter {
    public PropertyList convertToPropertyList(ApiPropertyListRequest request, AccessTokenModel accessTokenBean) {
        return new PropertyList()
                .setClientId(accessTokenBean.getApp().getClient().getId())
                .setAppTitle(accessTokenBean.getApp().getTitle())
                .setProfiles(StringUtils.isNotBlank(request.getProfile()) ? Set.of(request.getProfile()) : null);
    }

    public ApiPropertyListResponse convertToPropertyListResponse(List<PropertyModel> properties) {
        Asserts.isNotNull(properties, "properties is null");
        return new ApiPropertyListResponse()
                .setCount(properties.size())
                .setProperties(properties.stream().map(this::buildPropertyDto).toList());
    }

    private ApiPropertyDto buildPropertyDto(PropertyModel propertyBean) {
        return new ApiPropertyDto()
                .setKey(propertyBean.getKey())
                .setValue(propertyBean.getValue())
                .setDescription(propertyBean.getDescription())
                .setProfile(propertyBean.getProfile());
    }
}
