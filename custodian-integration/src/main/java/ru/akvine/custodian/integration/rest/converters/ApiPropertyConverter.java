package ru.akvine.custodian.integration.rest.converters;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.custodian.core.services.domain.AccessTokenBean;
import ru.akvine.custodian.core.services.domain.PropertyBean;
import ru.akvine.custodian.core.services.dto.property.PropertyList;
import ru.akvine.custodian.integration.rest.dto.ApiPropertyDto;
import ru.akvine.custodian.integration.rest.dto.ApiPropertyListRequest;
import ru.akvine.custodian.integration.rest.dto.ApiPropertyListResponse;

import java.util.List;
import java.util.Set;

@Component
public class ApiPropertyConverter {
    public PropertyList convertToPropertyList(ApiPropertyListRequest request, AccessTokenBean accessTokenBean) {
        return new PropertyList()
                .setClientId(accessTokenBean.getApp().getClient().getId())
                .setAppTitle(accessTokenBean.getApp().getTitle())
                .setProfiles(StringUtils.isNotBlank(request.getProfile()) ? Set.of(request.getProfile()) : null);
    }

    public ApiPropertyListResponse convertToPropertyListResponse(List<PropertyBean> properties) {
        Preconditions.checkNotNull(properties, "properties is null");
        return new ApiPropertyListResponse()
                .setCount(properties.size())
                .setProperties(properties.stream().map(this::buildPropertyDto).toList());
    }

    private ApiPropertyDto buildPropertyDto(PropertyBean propertyBean) {
        return new ApiPropertyDto()
                .setKey(propertyBean.getKey())
                .setValue(propertyBean.getValue())
                .setDescription(propertyBean.getDescription())
                .setProfile(propertyBean.getProfile());
    }
}
