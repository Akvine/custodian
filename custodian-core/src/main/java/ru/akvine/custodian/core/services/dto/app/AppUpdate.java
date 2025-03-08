package ru.akvine.custodian.core.services.dto.app;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;

@Data
@Accessors(chain = true)
public class AppUpdate {
    private long clientId;

    private String appTitle;

    @Nullable
    private String newTitle;

    @Nullable
    private String newDescription;
}
