package ru.akvine.custodian.admin.controllers.rest.validators.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.custodian.admin.controllers.rest.dto.security.EmailRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.security.access_restore.AccessRestoreFinishRequest;
import ru.akvine.custodian.core.exceptions.client.ClientNotFoundException;
import ru.akvine.custodian.core.services.ClientService;
import ru.akvine.custodian.core.utils.Asserts;
import ru.akvine.custodian.core.validators.EmailValidator;
import ru.akvine.custodian.core.validators.PasswordValidator;

@Component
@RequiredArgsConstructor
public class AccessRestoreValidator {
    private final ClientService clientService;
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;

    public void verifyAccessRestore(EmailRequest request) {
        Asserts.isNotNull(request, "emailRequest is null");
        Asserts.isNotNull(request.getEmail(), "emailRequest.email is null");

        String email = request.getEmail();
        emailValidator.validate(email);
        verifyNotExistsByLogin(email);
    }

    public void verifyAccessRestoreFinish(AccessRestoreFinishRequest request) {
        Asserts.isNotNull(request, "accessRestoreFinishRequest is null");
        Asserts.isNotNull(request.getEmail(), "accessRestoreFinishRequest.email is null");
        Asserts.isNotNull(request.getPassword(), "accessRestoreFinishRequest.password is null");

        verifyNotExistsByLogin(request.getEmail());
        passwordValidator.validate(request.getPassword());
    }

    public void verifyNotExistsByLogin(String login) {
        boolean exists = clientService.isExistsByEmail(login);
        if (!exists) {
            throw new ClientNotFoundException("Client with email = [" + login + "] not registered!");
        }
    }
}
