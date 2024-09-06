package ru.akvine.custodian.admin.controllers.rest.dto.token;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.custodian.admin.controllers.rest.dto.common.SuccessfulResponse;

import java.util.List;

@Data
@Accessors(chain = true)
public class TokenListResponse extends SuccessfulResponse {
    private List<TokenDto> tokens;
}
