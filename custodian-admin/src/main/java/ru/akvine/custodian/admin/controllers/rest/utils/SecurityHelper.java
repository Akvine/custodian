package ru.akvine.custodian.admin.controllers.rest.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.akvine.custodian.admin.controllers.rest.config.security.ClientAuthentication;
import ru.akvine.custodian.core.exceptions.security.NoSessionException;
import ru.akvine.custodian.core.services.domain.ClientModel;
import ru.akvine.custodian.core.utils.Asserts;

@Component
@RequiredArgsConstructor
public class SecurityHelper {

    public void authenticate(ClientModel clientBean, HttpServletRequest request) {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new ClientAuthentication(
                clientBean.getId(),
                clientBean.getUuid(),
                clientBean.getEmail()));

        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", context);
    }

    @Nullable
    public ClientAuthentication getCurrentUserOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof ClientAuthentication) {
            return (ClientAuthentication) authentication;
        }
        return null;
    }

    public void doLogout(HttpServletRequest request) {
        if (request == null) {
            return;
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        ClientAuthentication user = getCurrentUserOrNull();
        if (user == null) {
            return;
        }

        session.removeAttribute("SPRING_SECURITY_CONTEXT");
        SecurityContextHolder.clearContext();
    }

    public ClientAuthentication getCurrentUser() {
        ClientAuthentication user = getCurrentUserOrNull();
        Asserts.isNotNull(user, "user is null");
        return user;
    }

    public HttpSession getSession(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            throw new NoSessionException();
        }
        return session;
    }
}
