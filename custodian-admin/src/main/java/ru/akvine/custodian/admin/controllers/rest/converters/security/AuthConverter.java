package ru.akvine.custodian.admin.controllers.rest.converters.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.custodian.admin.controllers.rest.dto.security.OtpActionResponse;
import ru.akvine.custodian.admin.controllers.rest.dto.security.auth.AuthCredentialsRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.security.auth.AuthFinishRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.security.auth.AuthResponse;
import ru.akvine.custodian.admin.controllers.rest.utils.SecurityHelper;
import ru.akvine.custodian.core.services.dto.security.auth.AuthActionRequest;
import ru.akvine.custodian.core.services.dto.security.auth.AuthActionResult;
import ru.akvine.custodian.core.utils.Asserts;

@Component
@RequiredArgsConstructor
public class AuthConverter {
    @Value("${security.otp.new.delay.seconds}")
    private int otpNewDelaySeconds;

    private final SecurityHelper securityHelper;

    public AuthActionRequest convertToAuthActionRequest(AuthCredentialsRequest request, HttpServletRequest httpServletRequest) {
        Asserts.isNotNull(request, "authCredentialsRequest is null");
        Asserts.isNotNull(httpServletRequest, "httpServletRequest is null");
        return new AuthActionRequest()
                .setEmail(request.getEmail())
                .setSessionId(httpServletRequest.getSession(true).getId())
                .setPassword(StringUtils.trimToEmpty(request.getPassword()));
    }

    public AuthActionRequest convertToAuthActionRequest(AuthFinishRequest request, HttpServletRequest httpServletRequest) {
        Asserts.isNotNull(request, "authFinishRequest is null");
        Asserts.isNotNull(httpServletRequest, "httpServletRequest is null");

        return new AuthActionRequest()
                .setEmail(request.getEmail())
                .setSessionId(securityHelper.getSession(httpServletRequest).getId())
                .setOtp(request.getOtp());
    }

    public AuthResponse convertToAuthResponse(AuthActionResult result) {
        Asserts.isNotNull(result, "authActionResult is null");
        Asserts.isNotNull(result.getOtp(), "authActionResult.otp is null");

        OtpActionResponse otpActionResponse = new OtpActionResponse()
                .setActionExpiredAt(result.getOtp().getExpiredAt().toString())
                .setOtpNumber(result.getOtp().getOtpNumber())
                .setOtpCountLeft(result.getOtp().getOtpCountLeft())
                .setOtpLastUpdate(result.getOtp().getOtpLastUpdate().toString())
                .setNewOtpDelay(otpNewDelaySeconds)
                .setOtpInvalidAttemptsLeft(result.getOtp().getOtpInvalidAttemptsLeft());
        return new AuthResponse().setOtp(otpActionResponse);
    }
}
