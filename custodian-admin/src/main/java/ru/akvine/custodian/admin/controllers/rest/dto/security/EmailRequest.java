package ru.akvine.custodian.admin.controllers.rest.dto.security;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public abstract class EmailRequest {
    @NotBlank
    private String email;
}
