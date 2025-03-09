package ru.akvine.custodian.core.services.dto.property;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

@Data
@Accessors(chain = true)
public class PropertyDelete {
    private long clientId;
    private String appTitle;

    @Nullable
    private Map<String, Set<String>> profilesWithKeys;
}
