package ru.akvine.custodian.admin.controllers.rest.dto.property;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class PropertyExportRequest {
    @NotBlank
    private String appTitle;

    @NotBlank
    private String fileType;

    private Set<String> profiles;
}
