package ru.akvine.custodian.admin.controllers.rest.dto.token;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TokenDto {
    @NotBlank
    private String appTitle;

    @NotBlank
    private String token;

    @NotBlank
    private String expiredAt;
}
