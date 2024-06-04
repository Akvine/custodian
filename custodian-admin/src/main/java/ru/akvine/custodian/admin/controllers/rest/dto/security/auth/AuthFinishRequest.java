package ru.akvine.custodian.admin.controllers.rest.dto.security.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.akvine.custodian.admin.controllers.rest.dto.security.EmailRequest;

@Data
@EqualsAndHashCode(callSuper = false)
public class AuthFinishRequest extends EmailRequest {
    @NotBlank
    @ToString.Exclude
    private String otp;
}
