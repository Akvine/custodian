package ru.akvine.custodian.controllers.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.custodian.controllers.rest.converters.PropertyConverter;
import ru.akvine.custodian.controllers.rest.dto.common.Response;
import ru.akvine.custodian.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.custodian.controllers.rest.dto.property.PropertyCreateRequest;
import ru.akvine.custodian.controllers.rest.dto.property.PropertyImport;
import ru.akvine.custodian.controllers.rest.dto.property.PropertyListRequest;
import ru.akvine.custodian.controllers.rest.meta.PropertyControllerMeta;
import ru.akvine.custodian.controllers.rest.validators.PropertyValidator;
import ru.akvine.custodian.services.PropertyService;
import ru.akvine.custodian.services.domain.PropertyBean;
import ru.akvine.custodian.services.dto.property.PropertyCreate;
import ru.akvine.custodian.services.dto.property.PropertyList;

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
        PropertyBean propertyBean = propertyService.create(propertyCreate);
        return propertyConverter.convertToPropertyCreateResponse(propertyBean);
    }

    @Override
    public Response list(@RequestBody @Valid PropertyListRequest propertyListRequest) {
        PropertyList propertyList = propertyConverter.convertToPropertyList(propertyListRequest);
        List<PropertyBean> properties = propertyService.list(propertyList);
        return propertyConverter.convertToPropertyListResponse(properties);
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
}
