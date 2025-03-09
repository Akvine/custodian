package ru.akvine.custodian.admin.controllers.rest.dto.token;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class TokenDeleteRequest {
    @NotBlank
    private String appTitle;

    @NotNull
    private Set<String> tokens;
}
