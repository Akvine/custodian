package ru.akvine.custodian.admin.controllers.rest.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.custodian.admin.controllers.rest.dto.client.ClientCreateRequest;
import ru.akvine.custodian.core.validators.EmailValidator;


@Component
@RequiredArgsConstructor
public class ClientValidator {
    private final EmailValidator emailValidator;

    public void verifyClientCreateRequest(ClientCreateRequest request) {
        emailValidator.validate(request.getEmail());
    }
}
