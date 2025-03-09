package ru.akvine.custodian.admin.controllers.rest.dto.property;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.Set;

@Data
@Accessors(chain = true)
public class PropertyDeleteRequest {
    @NotBlank
    private String appTitle;

    @NotNull
    private Map<String, Set<String>> profilesWithKeys;
}
