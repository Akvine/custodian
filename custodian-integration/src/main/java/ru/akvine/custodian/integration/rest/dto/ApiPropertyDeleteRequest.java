package ru.akvine.custodian.integration.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.Set;

@Data
@Accessors(chain = true)
public class ApiPropertyDeleteRequest {
    @NotBlank
    private String appTitle;

    private Map<String, Set<String>> profilesWithKeys;
}
