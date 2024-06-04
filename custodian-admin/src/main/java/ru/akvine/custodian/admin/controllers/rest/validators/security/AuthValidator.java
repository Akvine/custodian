package ru.akvine.custodian.admin.controllers.rest.validators.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.custodian.admin.controllers.rest.dto.security.EmailRequest;
import ru.akvine.custodian.core.validators.EmailValidator;

@Component
@RequiredArgsConstructor
public class AuthValidator {
    private final EmailValidator emailValidator;

    public void verifyAuthLogin(EmailRequest request) {
        emailValidator.validate(request.getEmail());
    }
}
