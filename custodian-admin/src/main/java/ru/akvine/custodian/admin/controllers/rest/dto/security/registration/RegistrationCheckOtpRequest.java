package ru.akvine.custodian.admin.controllers.rest.dto.security.registration;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.akvine.custodian.admin.controllers.rest.dto.security.EmailRequest;

@Data
public class RegistrationCheckOtpRequest extends EmailRequest {
    @NotBlank
    private String otp;
}
