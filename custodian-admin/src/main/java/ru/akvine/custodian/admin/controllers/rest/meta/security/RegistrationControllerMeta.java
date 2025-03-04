package ru.akvine.custodian.admin.controllers.rest.meta.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.common.Response;
import ru.akvine.custodian.admin.controllers.rest.dto.security.registration.*;

@RequestMapping("/registration")
public interface RegistrationControllerMeta {
    @PostMapping(value = "/start")
    Response start(@Valid @RequestBody RegistrationStartRequest request, HttpServletRequest httpServletRequest);

    @PostMapping(value = "/check")
    Response check(@Valid @RequestBody RegistrationCheckOtpRequest request, HttpServletRequest httpServletRequest);

    @PostMapping(value = "/newotp")
    Response newotp(@Valid @RequestBody RegistrationNewOtpRequest request, HttpServletRequest httpServletRequest);

    @PostMapping(value = "/password/validate")
    Response passwordValidate(@Valid @RequestBody RegistrationPasswordValidateRequest request);

    @PostMapping(value = "/finish")
    Response finish(@Valid @RequestBody RegistrationFinishRequest request, HttpServletRequest httpServletRequest);
}
