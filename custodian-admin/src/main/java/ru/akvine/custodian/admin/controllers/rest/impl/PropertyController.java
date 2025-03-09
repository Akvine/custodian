package ru.akvine.custodian.admin.controllers.rest.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.common.Response;
import ru.akvine.common.SuccessfulResponse;
import ru.akvine.custodian.admin.controllers.rest.converters.PropertyConverter;
import ru.akvine.custodian.admin.controllers.rest.dto.property.*;
import ru.akvine.custodian.admin.controllers.rest.meta.PropertyControllerMeta;
import ru.akvine.custodian.admin.controllers.rest.validators.PropertyValidator;
import ru.akvine.custodian.core.services.dto.property.PropertyExport;
import ru.akvine.custodian.core.services.PropertyService;
import ru.akvine.custodian.core.services.domain.PropertyModel;
import ru.akvine.custodian.core.services.dto.property.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PropertyController implements PropertyControllerMeta {
    private final PropertyService propertyService;
    private final PropertyConverter propertyConverter;
    private final PropertyValidator propertyValidator;

    @Override
    public Response create(@RequestBody @Valid PropertyCreateRequest request) {
        PropertyCreate propertyCreate = propertyConverter.convertToPropertyCreate(request);
        PropertyModel propertyBean = propertyService.create(propertyCreate);
        return propertyConverter.convertToPropertyCreateResponse(propertyBean);
    }

    @Override
    public Response list(@RequestBody @Valid PropertyListRequest propertyListRequest) {
        PropertyList propertyList = propertyConverter.convertToPropertyList(propertyListRequest);
        List<PropertyModel> properties = propertyService.list(propertyList);
        return propertyConverter.convertToPropertyListResponse(properties);
    }

    @Override
    public Response update(@RequestBody @Valid PropertyUpdateRequest request) {
        PropertyUpdate propertyUpdate = propertyConverter.convertToPropertyUpdate(request);
        PropertyModel updatedProperty = propertyService.updateProperty(propertyUpdate);
        return propertyConverter.convertToPropertyListResponse(List.of(updatedProperty));
    }

    @Override
    public Response importProperties(@RequestParam("file") MultipartFile file,
                                     @RequestParam("profile") String profile,
                                     @RequestParam("appTitle") String appTitle) {
        propertyValidator.verifyImportProperties(file, appTitle, profile);
        PropertyImport propertyImport = propertyConverter.convertToPropertyImport(file, appTitle, profile);
        propertyService.importProperties(propertyImport);
        return new SuccessfulResponse();
    }

    @Override
    public ResponseEntity<?> exportProperties(@RequestBody @Valid PropertyExportRequest request) {
        PropertyExport propertyExport = propertyConverter.convertToPropertyExport(request);
        byte[] fileWithProperties = propertyService.exportProperties(propertyExport);
        return propertyConverter.convertToResponseEntity(fileWithProperties, propertyExport.getFileType());
    }

    @Override
    public Response delete(@RequestBody @Valid PropertyDeleteRequest request) {
        PropertyDelete propertyDelete = propertyConverter.convertToPropertyDelete(request);
        propertyService.delete(propertyDelete);
        return new SuccessfulResponse();
    }
}
