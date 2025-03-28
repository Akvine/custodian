package ru.akvine.custodian.admin.controllers.rest.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.custodian.admin.controllers.rest.dto.property.*;
import ru.akvine.custodian.admin.controllers.rest.parsers.FilePropertiesParser;
import ru.akvine.custodian.admin.controllers.rest.utils.SecurityHelper;
import ru.akvine.custodian.admin.controllers.rest.utils.StringHelper;
import ru.akvine.custodian.core.enums.ExportFileType;
import ru.akvine.custodian.core.services.domain.PropertyModel;
import ru.akvine.custodian.core.services.dto.property.*;
import ru.akvine.custodian.core.utils.Asserts;

import java.util.List;

import static ru.akvine.custodian.core.enums.ExportFileType.XLSX;

@Component
@RequiredArgsConstructor
public class PropertyConverter {
    private static final String HEADER_PREFIX = "attachment; filename=";

    private final FilePropertiesParser filePropertiesParser;
    private final SecurityHelper securityHelper;

    public PropertyCreate convertToPropertyCreate(PropertyCreateRequest request) {
        Asserts.isNotNull(request, "propertyCreateRequest is null");
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

    public PropertyCreateResponse convertToPropertyCreateResponse(PropertyModel propertyBean) {
        Asserts.isNotNull(propertyBean, "propertyBean is null");
        return new PropertyCreateResponse().setProperty(buildPropertyDto(propertyBean));
    }

    public PropertyList convertToPropertyList(PropertyListRequest request) {
        Asserts.isNotNull(request, "propertyListRequest is null");
        return new PropertyList()
                .setClientId(securityHelper.getCurrentUser().getId())
                .setAppTitle(request.getAppTitle())
                .setProfilesAndKeys(request.getKeys())
                .setProfiles(request.getProfiles());
    }

    public PropertyUpdate convertToPropertyUpdate(PropertyUpdateRequest request) {
        Asserts.isNotNull(request);
        return new PropertyUpdate()
                .setClientId(securityHelper.getCurrentUser().getId())
                .setKey(request.getKey())
                .setProfile(request.getProfile())
                .setAppTitle(request.getAppTitle())
                .setNewKey(request.getNewKey())
                .setNewValue(request.getNewValue())
                .setNewProfile(request.getNewProfile())
                .setNewDescription(request.getNewDescription());

    }

    public PropertyDelete convertToPropertyDelete(PropertyDeleteRequest request) {
        Asserts.isNotNull(request);
        return new PropertyDelete()
                .setClientId(securityHelper.getCurrentUser().getId())
                .setAppTitle(request.getAppTitle())
                .setProfilesWithKeys(request.getProfilesWithKeys());
    }

    public PropertyListResponse convertToPropertyListResponse(List<PropertyModel> properties) {
        Asserts.isNotNull(properties, "properties is null");
        return new PropertyListResponse()
                .setCount(properties.size())
                .setProperties(properties.stream().map(this::buildPropertyDto).toList());
    }

    public PropertyImport convertToPropertyImport(MultipartFile file,
                                                  String appTitle,
                                                  String profile) {
        Asserts.isNotNull(file, "file is null");
        Asserts.isNotNull(appTitle, "appTitle is null");
        Asserts.isNotNull(profile, "profile is null");
        return new PropertyImport()
                .setClientId(securityHelper.getCurrentUser().getId())
                .setProperties(filePropertiesParser.parse(file))
                .setAppTitle(appTitle.trim().toLowerCase())
                .setProfile(profile.trim().toLowerCase());
    }

    public PropertyExport convertToPropertyExport(PropertyExportRequest request) {
        Asserts.isNotNull(request);
        return new PropertyExport()
                .setClientId(securityHelper.getCurrentUser().getId())
                .setAppTitle(request.getAppTitle())
                .setFileType(ExportFileType.safeFrom(request.getFileType()))
                .setProfiles(request.getProfiles());
    }

    public ResponseEntity<?> convertToResponseEntity(byte[] file, ExportFileType fileType) {
        Asserts.isNotNull(file, "fileWithProperties is null");
        Asserts.isNotNull(fileType, "fileType is null");

        String formattedName;
        switch (fileType) {
            case XLSX -> formattedName = HEADER_PREFIX + "response." + XLSX.getExtension();
            default ->
                    throw new UnsupportedOperationException("File with type = [" + fileType + "] is not supported by app!");
        }

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, formattedName)
                .header(HttpHeaders.CONTENT_TYPE, fileType.getMimeType())
                .body(file);
    }

    private PropertyDto buildPropertyDto(PropertyModel propertyBean) {
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
