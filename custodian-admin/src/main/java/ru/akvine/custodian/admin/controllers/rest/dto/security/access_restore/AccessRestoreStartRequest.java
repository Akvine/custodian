package ru.akvine.custodian.admin.controllers.rest.dto.security.access_restore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.akvine.custodian.admin.controllers.rest.dto.security.EmailRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccessRestoreStartRequest extends EmailRequest {
}
