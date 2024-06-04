package ru.akvine.custodian.core.services.dto.security.access_restore;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.custodian.core.enums.ActionState;
import ru.akvine.custodian.core.repositories.entities.security.AccessRestoreActionEntity;
import ru.akvine.custodian.core.services.dto.security.OtpAction;

@Data
@Accessors(chain = true)
public class AccessRestoreActionResult {
    private final ActionState state;
    private final OtpAction otp;

    public AccessRestoreActionResult(AccessRestoreActionEntity entity) {
        this.state = entity.getState();
        this.otp = new OtpAction(entity.getOtpAction());
    }
}