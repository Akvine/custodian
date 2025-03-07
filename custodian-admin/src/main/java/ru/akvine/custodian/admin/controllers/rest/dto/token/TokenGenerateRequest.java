package ru.akvine.custodian.admin.controllers.rest.dto.token;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TokenGenerateRequest {
    @NotBlank
    private String appTitle;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String expiredDate;

    private String accessRights;
}
