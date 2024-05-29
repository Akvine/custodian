package ru.akvine.custodian.controllers.rest.dto.property;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PropertyCreateRequest {
    @NotBlank
    private String clientUuid;

    @NotBlank
    private String appTitle;

    @NotBlank
    private String profile;

    private String description;

    @NotBlank
    private String key;

    @NotBlank
    private String value;
}
