package ru.akvine.custodian.core.services.dto.property;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;

@Data
@Accessors(chain = true)
public class PropertyCreate {
    private long clientId;
    private String appTitle;
    private String profile;
    @Nullable
    private String description;
    private String key;
    private String value;
}
