package ru.akvine.custodian.services.dto.app;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;

@Data
@Accessors(chain = true)
public class AppCreate {
    private String clientUuid;
    private String title;
    @Nullable
    private String description;
}
