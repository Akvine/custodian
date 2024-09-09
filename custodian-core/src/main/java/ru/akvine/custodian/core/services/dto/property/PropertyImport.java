package ru.akvine.custodian.core.services.dto.property;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class PropertyImport {
    private long clientId;
    private String appTitle;
    private String profile;
    private Map<String, String> properties;
}
