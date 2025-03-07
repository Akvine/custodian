package ru.akvine.custodian.admin.controllers.rest.converters.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.akvine.custodian.admin.controllers.rest.dto.security.OtpActionResponse;
import ru.akvine.custodian.admin.controllers.rest.dto.security.access_restore.AccessRestoreCheckOtpRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.security.access_restore.AccessRestoreFinishRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.security.access_restore.AccessRestoreResponse;
import ru.akvine.custodian.admin.controllers.rest.dto.security.access_restore.AccessRestoreStartRequest;
import ru.akvine.custodian.admin.controllers.rest.utils.SecurityHelper;
import ru.akvine.custodian.core.services.dto.security.access_restore.AccessRestoreActionRequest;
import ru.akvine.custodian.core.services.dto.security.access_restore.AccessRestoreActionResult;
import ru.akvine.custodian.core.utils.Asserts;

@Component
@RequiredArgsConstructor
public class AccessRestoreConverter {

    @Value("${security.otp.new.delay.seconds}")
    private long otpNewDelaySeconds;

    private final SecurityHelper securityHelper;

    public AccessRestoreActionRequest convertToAccessRestoreActionRequest(AccessRestoreStartRequest request,
                                                                          HttpServletRequest httpServletRequest) {
        Asserts.isNotNull(request, "accessRestoreStartRequest is null");
        Asserts.isNotNull(httpServletRequest, "httpServletRequest is null");
        return new AccessRestoreActionRequest()
                .setLogin(request.getEmail())
                .setSessionId(httpServletRequest.getSession(true).getId());
    }

    public AccessRestoreActionRequest convertToAccessRestoreActionRequest(AccessRestoreCheckOtpRequest request,
                                                                          HttpServletRequest httpServletRequest) {
        Asserts.isNotNull(request, "accessRestoreCheckOtpRequest is null");
        Asserts.isNotNull(httpServletRequest, "httpServletRequest is null");

        return new AccessRestoreActionRequest()
                .setLogin(request.getEmail())
                .setSessionId(securityHelper.getSession(httpServletRequest).getId())
                .setOtp(request.getOtp());
    }

    public AccessRestoreActionRequest convertToAccessRestoreActionRequest(AccessRestoreFinishRequest request,
                                                                          HttpServletRequest httpServletRequest) {
        Asserts.isNotNull(request, "accessRestoreFinishRequest is null");
        Asserts.isNotNull(httpServletRequest, "httpServletRequest is null");
        return new AccessRestoreActionRequest()
                .setLogin(request.getEmail())
                .setSessionId(securityHelper.getSession(httpServletRequest).getId())
                .setPassword(request.getPassword());
    }

    public AccessRestoreResponse convertToAccessRestoreResponse(AccessRestoreActionResult result) {
        Asserts.isNotNull(result, "accessRestoreActionResult is null");
        Asserts.isNotNull(result.getState(), "accessRestoreActionResult.state is null");
        Asserts.isNotNull(result.getOtp(), "accessRestoreActionResult.otp is null");

        OtpActionResponse otpActionResponse = new OtpActionResponse()
                .setActionExpiredAt(result.getOtp().getExpiredAt().toString())
                .setOtpCountLeft(result.getOtp().getOtpCountLeft())
                .setOtpNumber(result.getOtp().getOtpNumber())
                .setOtpLastUpdate(result.getOtp().getOtpLastUpdate() != null ? result.getOtp().getOtpLastUpdate().toString() : null)
                .setNewOtpDelay(otpNewDelaySeconds)
                .setOtpInvalidAttemptsLeft(result.getOtp().getOtpInvalidAttemptsLeft());

        return new AccessRestoreResponse()
                .setState(result.getState().toString())
                .setOtp(otpActionResponse);
    }
}
