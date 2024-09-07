package ru.akvine.custodian.admin.controllers.rest.dto.app;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.custodian.admin.controllers.rest.dto.common.SuccessfulResponse;

import java.util.List;

@Data
@Accessors(chain = true)
public class AppListResponse extends SuccessfulResponse {
    private int count;

    @NotNull
    private List<@Valid AppDto> apps;
}
