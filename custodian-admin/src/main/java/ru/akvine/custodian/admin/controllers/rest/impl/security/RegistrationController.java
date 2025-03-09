package ru.akvine.custodian.admin.controllers.rest.impl.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.common.Response;
import ru.akvine.common.SuccessfulResponse;
import ru.akvine.custodian.admin.controllers.rest.converters.security.RegistrationConverter;
import ru.akvine.custodian.admin.controllers.rest.dto.security.registration.*;
import ru.akvine.custodian.admin.controllers.rest.meta.security.RegistrationControllerMeta;
import ru.akvine.custodian.admin.controllers.rest.utils.SecurityHelper;
import ru.akvine.custodian.admin.controllers.rest.validators.security.RegistrationValidator;
import ru.akvine.custodian.core.services.domain.ClientModel;
import ru.akvine.custodian.core.services.dto.security.registration.RegistrationActionRequest;
import ru.akvine.custodian.core.services.dto.security.registration.RegistrationActionResult;
import ru.akvine.custodian.core.services.security.RegistrationActionService;

@RestController
@RequiredArgsConstructor
public class RegistrationController implements RegistrationControllerMeta {
    private final RegistrationValidator registrationValidator;
    private final RegistrationConverter registrationConverter;
    private final RegistrationActionService registrationActionService;
    private final SecurityHelper securityHelper;


    @Override
    public Response start(@Valid RegistrationStartRequest request, HttpServletRequest httpServletRequest) {
        registrationValidator.verifyRegistrationLogin(request);
        RegistrationActionRequest registrationActionRequest = registrationConverter.convertToRegistrationActionRequest(request, httpServletRequest);
        RegistrationActionResult registrationActionResult = registrationActionService.startRegistration(registrationActionRequest);
        return registrationConverter.convertToRegistrationResponse(registrationActionResult);
    }

    @Override
    public Response check(@Valid RegistrationCheckOtpRequest request, HttpServletRequest httpServletRequest) {
        registrationValidator.verifyRegistrationLogin(request);
        RegistrationActionRequest registrationActionRequest = registrationConverter.convertToRegistrationActionRequest(request, httpServletRequest);
        RegistrationActionResult registrationActionResult = registrationActionService.checkOneTimePassword(registrationActionRequest);
        return registrationConverter.convertToRegistrationResponse(registrationActionResult);
    }

    @Override
    public Response newotp(@Valid RegistrationNewOtpRequest request, HttpServletRequest httpServletRequest) {
        registrationValidator.verifyRegistrationLogin(request);
        RegistrationActionRequest registrationActionRequest = registrationConverter.convertToRegistrationActionRequest(request, httpServletRequest);
        RegistrationActionResult registrationActionResult = registrationActionService.generateNewOneTimePassword(registrationActionRequest);
        return registrationConverter.convertToRegistrationResponse(registrationActionResult);
    }

    @Override
    public Response passwordValidate(@Valid RegistrationPasswordValidateRequest request) {
        registrationValidator.verifyRegistrationPassword(request);
        return new SuccessfulResponse();
    }

    @Override
    public Response finish(@Valid RegistrationFinishRequest request, HttpServletRequest httpServletRequest) {
        registrationValidator.verifyRegistrationFinish(request);
        RegistrationActionRequest registrationActionRequest = registrationConverter.convertToRegistrationActionRequest(request, httpServletRequest);
        ClientModel clientBean = registrationActionService.finishRegistration(registrationActionRequest);
        securityHelper.authenticate(clientBean, httpServletRequest);
        return new SuccessfulResponse();
    }
}
