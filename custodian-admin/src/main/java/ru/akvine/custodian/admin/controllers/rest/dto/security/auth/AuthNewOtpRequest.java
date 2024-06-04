package ru.akvine.custodian.admin.controllers.rest.dto.security.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.akvine.custodian.admin.controllers.rest.dto.security.EmailRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthNewOtpRequest extends EmailRequest {
}
