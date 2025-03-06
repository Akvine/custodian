package ru.akvine.custodian.admin.controllers.rest.dto.property;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MaskingInfo {
    @NotNull
    @Size(min = 1, max = 1)
    private String mask;

    @Min(1)
    private int radius;
}
