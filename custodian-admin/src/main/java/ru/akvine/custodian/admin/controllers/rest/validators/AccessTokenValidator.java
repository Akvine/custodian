package ru.akvine.custodian.admin.controllers.rest.validators;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Component;
import ru.akvine.custodian.admin.controllers.rest.dto.token.TokenGenerateRequest;
import ru.akvine.custodian.core.exceptions.CommonErrorCodes;
import ru.akvine.custodian.core.exceptions.validation.ValidationException;
import ru.akvine.custodian.core.utils.DateUtils;

import java.time.ZonedDateTime;

import static java.time.ZonedDateTime.now;

@Component
public class AccessTokenValidator {
    public void verifyTokenGenerateRequest(TokenGenerateRequest request) {
        Preconditions.checkNotNull(request, "TokenGenerateRequest is null");

        if (request.getExpiredDate() != null) {
            ZonedDateTime expiredAt = DateUtils.convertToZonedDateTime(request.getExpiredDate());
            if (expiredAt.isBefore(now())) {
                throw new ValidationException(
                        CommonErrorCodes.Validation.DATE_INVALID_ERROR,
                        "Date can't be before current date. Field invalid: expiredAt");
            }
        }
    }
}
