package ru.akvine.custodian.core.services.dto.security.registration;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.custodian.core.enums.ActionState;
import ru.akvine.custodian.core.repositories.entities.security.RegistrationActionEntity;
import ru.akvine.custodian.core.services.dto.security.OtpAction;

@Data
@Accessors(chain = true)
public class RegistrationActionResult {
    private ActionState state;
    private OtpAction otp;

    public RegistrationActionResult(RegistrationActionEntity registrationActionEntity) {
        this.state = registrationActionEntity.getState();
        this.otp = new OtpAction(registrationActionEntity.getOtpAction());
    }
}
