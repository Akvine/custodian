package ru.akvine.custodian.integration.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.common.SuccessfulResponse;

import java.util.List;

@Data
@Accessors(chain = true)
public class ApiPropertyListResponse extends SuccessfulResponse {
    private int count;

    @NotNull
    private List<@Valid ApiPropertyDto> properties;
}
