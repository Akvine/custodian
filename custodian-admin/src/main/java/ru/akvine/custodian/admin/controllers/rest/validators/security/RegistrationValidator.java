package ru.akvine.custodian.admin.controllers.rest.validators.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.custodian.admin.controllers.rest.dto.security.EmailRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.security.registration.RegistrationFinishRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.security.registration.RegistrationPasswordValidateRequest;
import ru.akvine.custodian.core.exceptions.CommonErrorCodes;
import ru.akvine.custodian.core.exceptions.client.ClientAlreadyExistsException;
import ru.akvine.custodian.core.exceptions.validation.ValidationException;
import ru.akvine.custodian.core.services.ClientService;
import ru.akvine.custodian.core.validators.EmailValidator;
import ru.akvine.custodian.core.validators.PasswordValidator;

@Component
@RequiredArgsConstructor
public class RegistrationValidator {
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;
    private final ClientService clientService;

    private static final int ZERO = 0;
    private static final int INVALID_AGE = 150;

    public void verifyRegistrationLogin(EmailRequest request) {
        String login = request.getEmail();
        emailValidator.validate(login);
        verifyNotExistsByLogin(request.getEmail());
    }

    public void verifyRegistrationPassword(RegistrationPasswordValidateRequest request) {
        passwordValidator.validate(request.getPassword());
    }

    public void verifyRegistrationFinish(RegistrationFinishRequest request) {
        if (request.getAge() < ZERO) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.AGE_INVALID_ERROR, "Age can't be less than 0. Field name: age");
        }
        if (request.getAge() > INVALID_AGE) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.AGE_INVALID_ERROR, "Age can't be more than 150. Field name: age");
        }

        passwordValidator.validate(request.getPassword());
        emailValidator.validate(request.getEmail());
        verifyNotExistsByLogin(request.getEmail());
    }

    public void verifyNotExistsByLogin(String login) {
        boolean exists = clientService.isExistsByEmail(login);
        if (exists) {
            throw new ClientAlreadyExistsException("Client with email = [" + login + "] already exists!");
        }
    }
}
