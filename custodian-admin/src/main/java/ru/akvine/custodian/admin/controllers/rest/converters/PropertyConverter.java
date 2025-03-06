package ru.akvine.custodian.admin.controllers.rest.converters;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.custodian.admin.controllers.rest.dto.property.*;
import ru.akvine.custodian.admin.controllers.rest.parsers.FilePropertiesParser;
import ru.akvine.custodian.admin.controllers.rest.utils.SecurityHelper;
import ru.akvine.custodian.admin.controllers.rest.utils.StringHelper;
import ru.akvine.custodian.core.services.domain.PropertyBean;
import ru.akvine.custodian.core.services.dto.property.PropertyCreate;
import ru.akvine.custodian.core.services.dto.property.PropertyImport;
import ru.akvine.custodian.core.services.dto.property.PropertyList;


import java.util.List;

@Component
@RequiredArgsConstructor
public class PropertyConverter {
    private final FilePropertiesParser filePropertiesParser;
    private final SecurityHelper securityHelper;

    public PropertyCreate convertToPropertyCreate(PropertyCreateRequest request) {
        Preconditions.checkNotNull(request, "propertyCreateRequest is null");
        return new PropertyCreate()
                .setClientId(securityHelper.getCurrentUser().getId())
                .setAppTitle(request.getAppTitle().trim().toLowerCase())
                .setProfile(request.getProfile().trim().toLowerCase())
                .setKey(request.getKey())
                .setValue(request.getValue())
                .setDescription(request.getDescription())
                .setMask(request.getMaskingInfo() != null ? request.getMaskingInfo().getMask().toCharArray()[0] : null)
                .setMaskingRadius(request.getMaskingInfo() != null ? request.getMaskingInfo().getRadius() : null);
    }

    public PropertyCreateResponse convertToPropertyCreateResponse(PropertyBean propertyBean) {
        Preconditions.checkNotNull(propertyBean, "propertyBean is null");
        return new PropertyCreateResponse().setProperty(buildPropertyDto(propertyBean));
    }

    public PropertyList convertToPropertyList(PropertyListRequest request) {
        Preconditions.checkNotNull(request, "propertyListRequest is null");
        return new PropertyList()
                .setClientId(securityHelper.getCurrentUser().getId())
                .setAppTitle(request.getAppTitle())
                .setProfilesAndKeys(request.getKeys())
                .setProfiles(request.getProfiles());
    }

    public PropertyListResponse convertToPropertyListResponse(List<PropertyBean> properties) {
        Preconditions.checkNotNull(properties, "properties is null");
        return new PropertyListResponse()
                .setCount(properties.size())
                .setProperties(properties.stream().map(this::buildPropertyDto).toList());
    }

    public PropertyImport convertToPropertyImport(MultipartFile file,
                                                  String appTitle,
                                                  String profile) {
        Preconditions.checkNotNull(file, "file is null");
        Preconditions.checkNotNull(appTitle, "appTitle is null");
        Preconditions.checkNotNull(profile, "profile is null");
        return new PropertyImport()
                .setClientId(securityHelper.getCurrentUser().getId())
                .setProperties(filePropertiesParser.parse(file))
                .setAppTitle(appTitle.trim().toLowerCase())
                .setProfile(profile.trim().toLowerCase());
    }

    private PropertyDto buildPropertyDto(PropertyBean propertyBean) {
        String value;
        if (propertyBean.getMask() != null && propertyBean.getMaskingRadius() != null) {
            value = StringHelper.replaceAroundMiddle(
                    propertyBean.getValue(),
                    propertyBean.getMask(),
                    propertyBean.getMaskingRadius());
        } else {
            value = propertyBean.getValue();
        }

        return new PropertyDto()
                .setKey(propertyBean.getKey())
                .setValue(value)
                .setDescription(propertyBean.getDescription())
                .setProfile(propertyBean.getProfile());
    }
}
