package ru.akvine.custodian.core.services.dto.property;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;

@Data
@Accessors(chain = true)
public class PropertyUpdate {
    private long clientId;
    private String appTitle;
    private String profile;
    private String key;

    @Nullable
    private String newKey;
    @Nullable
    private String newValue;
    @Nullable
    private String newProfile;
    @Nullable
    private String newDescription;
    @Nullable
    private Character newMask;
    @Nullable
    private Integer newRadiusMask;
}
