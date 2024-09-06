package ru.akvine.custodian.admin.controllers.rest.meta;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.custodian.admin.controllers.rest.dto.common.Response;
import ru.akvine.custodian.admin.controllers.rest.dto.token.TokenGenerateRequest;

@RequestMapping(value = "/access/tokens")
public interface AccessTokenControllerMeta {
    @PostMapping(value = "/generate")
    Response generate(@RequestBody @Valid TokenGenerateRequest request);

    @PostMapping(value = "/list")
    Response list(HttpServletRequest request);
}
