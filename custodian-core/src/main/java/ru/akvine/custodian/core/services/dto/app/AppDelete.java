package ru.akvine.custodian.core.services.dto.app;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AppDelete {
    private long clientId;
    private String appTitle;
}
