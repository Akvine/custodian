package ru.akvine.custodian.admin.controllers.rest.dto.security.registration;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrationPasswordValidateRequest {
    @NotBlank
    private String password;
}
