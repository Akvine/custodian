package ru.akvine.custodian.admin.controllers.rest.meta.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.common.Response;
import ru.akvine.custodian.admin.controllers.rest.dto.security.access_restore.AccessRestoreCheckOtpRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.security.access_restore.AccessRestoreFinishRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.security.access_restore.AccessRestoreStartRequest;

@RequestMapping(value = "/access/restore")
public interface AccessRestoreControllerMeta {
    @PostMapping(value = "/start")
    Response start(@Valid @RequestBody AccessRestoreStartRequest request, HttpServletRequest httpServletRequest);

    @PostMapping(value = "/newotp")
    Response newotp(@Valid @RequestBody AccessRestoreStartRequest request, HttpServletRequest httpServletRequest);

    @PostMapping(value = "/check")
    Response check(@Valid @RequestBody AccessRestoreCheckOtpRequest request, HttpServletRequest httpServletRequest);

    @PostMapping(value = "/finish")
    Response finish(@Valid @RequestBody AccessRestoreFinishRequest request, HttpServletRequest httpServletRequest);
}
