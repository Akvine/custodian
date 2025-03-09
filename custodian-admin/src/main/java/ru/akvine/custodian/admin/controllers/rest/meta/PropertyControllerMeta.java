package ru.akvine.custodian.admin.controllers.rest.meta;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.common.Response;
import ru.akvine.custodian.admin.controllers.rest.dto.property.PropertyCreateRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.property.PropertyDeleteRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.property.PropertyListRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.property.PropertyUpdateRequest;

@RequestMapping(value = "/properties")
public interface PropertyControllerMeta {
    @PostMapping(value = "/create")
    Response create(@RequestBody @Valid PropertyCreateRequest request);

    @PostMapping(value = "/list")
    Response list(@RequestBody @Valid PropertyListRequest propertyListRequest);

    @PatchMapping
    Response update(@RequestBody @Valid PropertyUpdateRequest request);

    @PostMapping(value = "/import")
    Response importProperties(@RequestParam("file") MultipartFile file,
                              @RequestParam("profile") String profile,
                              @RequestParam("appTitle") String appTitle);

    @DeleteMapping
    Response delete(@RequestBody @Valid PropertyDeleteRequest propertyDeleteRequest);
}
