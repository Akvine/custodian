package ru.akvine.custodian.admin.controllers.rest.dto.token;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.custodian.admin.controllers.rest.dto.common.SuccessfulResponse;

@Data
@Accessors(chain = true)
public class TokenGenerateResponse extends SuccessfulResponse {
    @NotBlank
    private String token;
}
