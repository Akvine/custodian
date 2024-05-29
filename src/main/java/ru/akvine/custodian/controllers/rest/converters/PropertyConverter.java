package ru.akvine.custodian.controllers.rest.converters;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.custodian.controllers.rest.dto.property.*;
import ru.akvine.custodian.controllers.rest.parsers.FilePropertiesParser;
import ru.akvine.custodian.services.domain.PropertyBean;
import ru.akvine.custodian.services.dto.property.PropertyCreate;
import ru.akvine.custodian.services.dto.property.PropertyList;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PropertyConverter {
    private final FilePropertiesParser filePropertiesParser;

    public PropertyCreate convertToPropertyCreate(PropertyCreateRequest request) {
        Preconditions.checkNotNull(request, "propertyCreateRequest is null");
        return new PropertyCreate()
                .setClientUuid(request.getClientUuid())
                .setAppTitle(request.getAppTitle().trim().toLowerCase())
                .setProfile(request.getProfile().trim().toLowerCase())
                .setKey(request.getKey())
                .setValue(request.getValue())
                .setDescription(request.getDescription());
    }

    public PropertyCreateResponse convertToPropertyCreateResponse(PropertyBean propertyBean) {
        Preconditions.checkNotNull(propertyBean, "propertyBean is null");
        return new PropertyCreateResponse().setProperty(buildPropertyDto(propertyBean));
    }

    public PropertyList convertToPropertyList(PropertyListRequest request) {
        Preconditions.checkNotNull(request, "propertyListRequest is null");
        return new PropertyList()
                .setAppTitle(request.getAppTitle())
                .setProfilesAndKeys(request.getKeys())
                .setProfiles(request.getProfiles());
    }

    public PropertyListResponse convertToPropertyListResponse(List<PropertyBean> properties) {
        Preconditions.checkNotNull(properties, "properties is null");
        return new PropertyListResponse().setProperties(properties.stream().map(this::buildPropertyDto).toList());
    }

    public PropertyImport convertToPropertyImport(MultipartFile file,
                                                  String appTitle,
                                                  String profile) {
        Preconditions.checkNotNull(file, "file is null");
        Preconditions.checkNotNull(appTitle, "appTitle is null");
        Preconditions.checkNotNull(profile, "profile is null");
        return new PropertyImport()
                .setProperties(filePropertiesParser.parse(file))
                .setAppTitle(appTitle.trim().toLowerCase())
                .setProfile(profile.trim().toLowerCase());
    }

    private PropertyDto buildPropertyDto(PropertyBean propertyBean) {
        return new PropertyDto()
                .setKey(propertyBean.getKey())
                .setValue(propertyBean.getValue())
                .setDescription(propertyBean.getDescription())
                .setProfile(propertyBean.getProfile());
    }
}
