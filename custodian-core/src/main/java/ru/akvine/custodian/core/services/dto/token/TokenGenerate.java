package ru.akvine.custodian.core.services.dto.token;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.akvine.custodian.core.enums.AccessRights;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class TokenGenerate {
    private long clientId;
    private String appTitle;
    @Nullable
    private ZonedDateTime expiredAt;
    private List<AccessRights> accessRights;
}
