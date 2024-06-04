package ru.akvine.custodian.admin.controllers.rest.dto.security.access_restore;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import ru.akvine.custodian.admin.controllers.rest.dto.security.EmailRequest;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AccessRestoreCheckOtpRequest extends EmailRequest {
    @NotBlank
    private String otp;
}
