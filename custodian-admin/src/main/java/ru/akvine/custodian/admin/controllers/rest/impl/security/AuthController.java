package ru.akvine.custodian.admin.controllers.rest.impl.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.custodian.admin.controllers.rest.converters.security.AuthConverter;
import ru.akvine.custodian.admin.controllers.rest.dto.common.Response;
import ru.akvine.custodian.admin.controllers.rest.dto.common.SuccessfulResponse;
import ru.akvine.custodian.admin.controllers.rest.dto.security.auth.AuthCredentialsRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.security.auth.AuthFinishRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.security.auth.AuthNewOtpRequest;
import ru.akvine.custodian.admin.controllers.rest.meta.security.AuthControllerMeta;
import ru.akvine.custodian.admin.controllers.rest.validators.security.AuthValidator;
import ru.akvine.custodian.core.services.domain.ClientBean;
import ru.akvine.custodian.core.services.dto.security.auth.AuthActionRequest;
import ru.akvine.custodian.core.services.dto.security.auth.AuthActionResult;
import ru.akvine.custodian.core.services.security.AuthActionService;
import ru.akvine.custodian.admin.controllers.rest.utils.SecurityUtils;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerMeta {
    private final AuthConverter authConverter;
    private final AuthValidator authValidator;
    private final AuthActionService authActionService;

    @Override
    public Response start(@Valid AuthCredentialsRequest request, HttpServletRequest httpServletRequest) {
        authValidator.verifyAuthLogin(request);
        AuthActionRequest authActionRequest = authConverter.convertToAuthActionRequest(request, httpServletRequest);
        AuthActionResult authActionResult = authActionService.startAuth(authActionRequest);
        return authConverter.convertToAuthResponse(authActionResult);
    }

    @Override
    public Response newotp(@Valid AuthNewOtpRequest request, HttpServletRequest httpServletRequest) {
        authValidator.verifyAuthLogin(request);
        AuthActionResult authActionResult = authActionService.generateNewOtp(request.getEmail());
        return authConverter.convertToAuthResponse(authActionResult);
    }

    @Override
    public Response finish(@Valid @RequestBody AuthFinishRequest request, HttpServletRequest httpServletRequest) {
        authValidator.verifyAuthLogin(request);
        AuthActionRequest authActionRequest = authConverter.convertToAuthActionRequest(request, httpServletRequest);
        ClientBean clientBean = authActionService.finishAuth(authActionRequest);
        SecurityUtils.authenticate(clientBean);
        return new SuccessfulResponse();
    }

    @Override
    public Response logout(HttpServletRequest httpServletRequest) {
        SecurityUtils.doLogout(httpServletRequest);
        return new SuccessfulResponse();
    }
}
