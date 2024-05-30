package ru.akvine.custodian.admin.controllers.rest.dto.property;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.custodian.admin.controllers.rest.dto.common.SuccessfulResponse;

import java.util.List;

@Data
@Accessors(chain = true)
public class PropertyListResponse extends SuccessfulResponse {
    @NotNull
    private List<@Valid PropertyDto> properties;
}
