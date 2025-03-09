package ru.akvine.custodian.admin.controllers.rest.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.common.Response;
import ru.akvine.common.SuccessfulResponse;
import ru.akvine.custodian.admin.controllers.rest.converters.AccessTokenConverter;
import ru.akvine.custodian.admin.controllers.rest.dto.token.TokenDeleteRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.token.TokenGenerateRequest;
import ru.akvine.custodian.admin.controllers.rest.meta.AccessTokenControllerMeta;
import ru.akvine.custodian.admin.controllers.rest.utils.SecurityHelper;
import ru.akvine.custodian.admin.controllers.rest.validators.AccessTokenValidator;
import ru.akvine.custodian.core.repositories.projections.AccessTokenProjection;
import ru.akvine.custodian.core.services.AccessTokenService;
import ru.akvine.custodian.core.services.domain.AccessTokenBean;
import ru.akvine.custodian.core.services.dto.token.TokenDelete;
import ru.akvine.custodian.core.services.dto.token.TokenGenerate;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccessTokenController implements AccessTokenControllerMeta {
    private final AccessTokenConverter accessTokenConverter;
    private final AccessTokenValidator accessTokenValidator;
    private final AccessTokenService accessTokenService;
    private final SecurityHelper securityHelper;

    @Override
    public Response generate(@RequestBody @Valid TokenGenerateRequest request) {
        accessTokenValidator.verifyTokenGenerateRequest(request);
        TokenGenerate tokenGenerate = accessTokenConverter.convertToTokenGenerate(request);
        AccessTokenBean accessTokenBean = accessTokenService.generate(tokenGenerate);
        return accessTokenConverter.convertToTokenGenerateResponse(accessTokenBean);
    }

    @Override
    public Response list(HttpServletRequest request) {
        List<AccessTokenProjection> tokens = accessTokenService.list(securityHelper.getCurrentUser().getId());
        return accessTokenConverter.convertToTokenListResponse(tokens);
    }

    @Override
    public Response delete(@RequestBody @Valid TokenDeleteRequest request) {
        TokenDelete tokenDelete = accessTokenConverter.convertToTokenDelete(request);
        accessTokenService.delete(tokenDelete);
        return new SuccessfulResponse();
    }
}
