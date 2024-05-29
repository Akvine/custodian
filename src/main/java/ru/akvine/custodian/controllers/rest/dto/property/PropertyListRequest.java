package ru.akvine.custodian.controllers.rest.dto.property;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.Set;

@Data
@Accessors(chain = true)
public class PropertyListRequest {
    @NotBlank
    private String appTitle;
    private Set<String> profiles;
    private Map<String, Set<String>> keys;
}
