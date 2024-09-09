package ru.akvine.custodian.admin.controllers.rest.impl.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.common.Response;
import ru.akvine.common.SuccessfulResponse;
import ru.akvine.custodian.admin.controllers.rest.converters.security.AuthConverter;
import ru.akvine.custodian.admin.controllers.rest.dto.security.auth.AuthCredentialsRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.security.auth.AuthFinishRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.security.auth.AuthNewOtpRequest;
import ru.akvine.custodian.admin.controllers.rest.meta.security.AuthControllerMeta;
import ru.akvine.custodian.admin.controllers.rest.utils.SecurityHelper;
import ru.akvine.custodian.admin.controllers.rest.validators.security.AuthValidator;
import ru.akvine.custodian.core.services.domain.ClientBean;
import ru.akvine.custodian.core.services.dto.security.auth.AuthActionRequest;
import ru.akvine.custodian.core.services.dto.security.auth.AuthActionResult;
import ru.akvine.custodian.core.services.security.AuthActionService;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerMeta {
    private final AuthConverter authConverter;
    private final AuthValidator authValidator;
    private final AuthActionService authActionService;
    private final SecurityHelper securityHelper;

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
        securityHelper.authenticate(clientBean, httpServletRequest);
        return new SuccessfulResponse();
    }

    @Override
    public Response logout(HttpServletRequest httpServletRequest) {
        securityHelper.doLogout(httpServletRequest);
        return new SuccessfulResponse();
    }
}
