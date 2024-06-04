package ru.akvine.custodian.admin.controllers.rest.dto.security.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.akvine.custodian.admin.controllers.rest.dto.security.EmailRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthCredentialsRequest extends EmailRequest {
    @NotBlank
    @ToString.Exclude
    private String password;
}
