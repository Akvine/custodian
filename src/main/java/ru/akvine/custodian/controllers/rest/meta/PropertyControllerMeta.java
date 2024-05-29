package ru.akvine.custodian.controllers.rest.meta;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.custodian.controllers.rest.dto.common.Response;
import ru.akvine.custodian.controllers.rest.dto.property.PropertyCreateRequest;
import ru.akvine.custodian.controllers.rest.dto.property.PropertyListRequest;

@RequestMapping(value = "/properties")
public interface PropertyControllerMeta {
    @PostMapping(value = "/create")
    Response create(@RequestBody @Valid PropertyCreateRequest request);

    @PostMapping(value = "/list")
    Response list(@RequestBody @Valid PropertyListRequest propertyListRequest);

    @PostMapping(value = "/import")
    Response importProperties(@RequestParam("file") MultipartFile file,
                              @RequestParam("profile") String profile,
                              @RequestParam("appTitle") String appTitle);
}
