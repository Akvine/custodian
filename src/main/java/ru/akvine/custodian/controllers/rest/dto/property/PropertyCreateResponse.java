package ru.akvine.custodian.controllers.rest.dto.property;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.custodian.controllers.rest.dto.common.SuccessfulResponse;

@Data
@Accessors(chain = true)
public class PropertyCreateResponse extends SuccessfulResponse {
    @NotNull
    private PropertyDto property;
}
