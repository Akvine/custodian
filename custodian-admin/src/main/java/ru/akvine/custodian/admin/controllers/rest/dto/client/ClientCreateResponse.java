package ru.akvine.custodian.admin.controllers.rest.dto.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import ru.akvine.custodian.admin.controllers.rest.dto.common.SuccessfulResponse;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ClientCreateResponse extends SuccessfulResponse {
    @NotNull
    private ClientDto client;
}
