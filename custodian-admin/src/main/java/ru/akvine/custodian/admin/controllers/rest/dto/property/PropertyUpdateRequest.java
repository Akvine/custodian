package ru.akvine.custodian.admin.controllers.rest.dto.property;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PropertyUpdateRequest {
    @NotBlank
    private String appTitle;

    @NotBlank
    private String profile;

    @NotBlank
    private String key;

    private String newKey;

    private String newProfile;

    private String newValue;

    private String newDescription;

    private String newMask;

    private Integer newRadiusMask;
}
