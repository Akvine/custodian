package ru.akvine.custodian.core.services.dto.token;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.time.ZonedDateTime;

@Data
@Accessors(chain = true)
public class TokenGenerate {
    private long clientId;
    private String appTitle;
    @Nullable
    private ZonedDateTime expiredAt;
}
