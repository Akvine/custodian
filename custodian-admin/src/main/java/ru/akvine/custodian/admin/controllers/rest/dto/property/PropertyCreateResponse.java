package ru.akvine.custodian.admin.controllers.rest.dto.property;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.common.SuccessfulResponse;

@Data
@Accessors(chain = true)
public class PropertyCreateResponse extends SuccessfulResponse {
    @NotNull
    private PropertyDto property;
}
