package ru.akvine.custodian.admin.controllers.rest.dto.property;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PropertyCreateRequest {
    @NotBlank
    private String appTitle;

    @NotBlank
    private String profile;

    private String description;

    @NotBlank
    private String key;

    @NotBlank
    private String value;

    @Valid
    private MaskingInfo maskingInfo;
}
