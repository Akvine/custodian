package ru.akvine.custodian.core.services.dto.property;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;
import java.util.Map;

@Data
@Accessors(chain = true)
public class PropertyImport {
    private long clientId;
    private String appTitle;
    private String profile;
    private Map<String, String> properties;
    @Nullable
    private Character mask;
    @Nullable
    private Integer maskingRadius;
}
