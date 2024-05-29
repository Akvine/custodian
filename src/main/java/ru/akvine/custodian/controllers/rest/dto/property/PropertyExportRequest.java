package ru.akvine.custodian.controllers.rest.dto.property;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class PropertyExportRequest {
    private String appTitle;
    private Set<String> profiles;
}
