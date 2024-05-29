package ru.akvine.custodian.controllers.rest.dto.app;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import ru.akvine.custodian.controllers.rest.dto.common.SuccessfulResponse;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AppCreateResponse extends SuccessfulResponse {
    @NotNull
    private AppDto app;
}
