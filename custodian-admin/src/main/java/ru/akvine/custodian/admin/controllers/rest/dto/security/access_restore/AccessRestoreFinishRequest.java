package ru.akvine.custodian.admin.controllers.rest.dto.security.access_restore;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.custodian.admin.controllers.rest.dto.security.EmailRequest;

@Data
@Accessors(chain = true)
public class AccessRestoreFinishRequest extends EmailRequest {
    @NotBlank
    private String password;
}
