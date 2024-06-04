package ru.akvine.custodian.admin.controllers.rest.dto.security.registration;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.akvine.custodian.admin.controllers.rest.dto.security.EmailRequest;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class RegistrationFinishRequest extends EmailRequest {
    @NotBlank
    @ToString.Exclude
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Min(1)
    private int age;
}
