package ru.akvine.custodian.admin.controllers.rest.converters.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.custodian.admin.controllers.rest.dto.security.OtpActionResponse;
import ru.akvine.custodian.admin.controllers.rest.dto.security.registration.*;
import ru.akvine.custodian.admin.controllers.rest.utils.SecurityHelper;
import ru.akvine.custodian.core.services.dto.security.registration.RegistrationActionRequest;
import ru.akvine.custodian.core.services.dto.security.registration.RegistrationActionResult;
import ru.akvine.custodian.core.utils.Asserts;

@Component
@RequiredArgsConstructor
public class RegistrationConverter {
    @Value("${security.otp.new.delay.seconds}")
    private long otpNewDelaySeconds;

    private final SecurityHelper securityHelper;

    public RegistrationActionRequest convertToRegistrationActionRequest(RegistrationStartRequest request,
                                                                        HttpServletRequest httpServletRequest) {
        Asserts.isNotNull(request, "registrationStartRequest is null");
        Asserts.isNotNull(httpServletRequest, "httpServletRequest is null");
        return new RegistrationActionRequest()
                .setEmail(request.getEmail())
                .setSessionId(httpServletRequest.getSession(true).getId());
    }

    public RegistrationActionRequest convertToRegistrationActionRequest(RegistrationCheckOtpRequest request,
                                                                        HttpServletRequest httpServletRequest) {
        Asserts.isNotNull(request, "registrationCheckOtpRequest is null");
        Asserts.isNotNull(httpServletRequest, "httpServletRequest is null");

        return new RegistrationActionRequest()
                .setEmail(request.getEmail())
                .setSessionId(securityHelper.getSession(httpServletRequest).getId())
                .setOtp(request.getOtp());
    }

    public RegistrationActionRequest convertToRegistrationActionRequest(RegistrationNewOtpRequest request,
                                                                        HttpServletRequest httpServletRequest) {
        Asserts.isNotNull(request, "registrationNewOtpRequest is null");
        Asserts.isNotNull(httpServletRequest, "httpServletRequest is null");
        return new RegistrationActionRequest()
                .setEmail(request.getEmail())
                .setSessionId(securityHelper.getSession(httpServletRequest).getId());
    }

    public RegistrationActionRequest convertToRegistrationActionRequest(RegistrationFinishRequest request,
                                                                        HttpServletRequest httpServletRequest) {
        Asserts.isNotNull(request, "registrationFinishRequest is null");
        Asserts.isNotNull(httpServletRequest, "httpServletRequest is null");

        return new RegistrationActionRequest()
                .setEmail(request.getEmail())
                .setSessionId(securityHelper.getSession(httpServletRequest).getId())
                .setPassword(request.getPassword())
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName());
    }

    public RegistrationResponse convertToRegistrationResponse(RegistrationActionResult result) {
        Asserts.isNotNull(result, "registrationActionResult is null");

        OtpActionResponse otpActionResponse = new OtpActionResponse()
                .setActionExpiredAt(result.getOtp().getExpiredAt().toString())
                .setOtpCountLeft(result.getOtp().getOtpCountLeft())
                .setOtpNumber(result.getOtp().getOtpNumber())
                .setOtpLastUpdate(result.getOtp().getOtpLastUpdate() != null ? result.getOtp().getOtpLastUpdate().toString() : null)
                .setNewOtpDelay(otpNewDelaySeconds)
                .setOtpInvalidAttemptsLeft(result.getOtp().getOtpInvalidAttemptsLeft());

        return new RegistrationResponse()
                .setOtp(otpActionResponse)
                .setState(result.getState().name());
    }
}
