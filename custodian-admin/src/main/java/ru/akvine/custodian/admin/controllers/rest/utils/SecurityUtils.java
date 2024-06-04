package ru.akvine.custodian.admin.controllers.rest.utils;

import com.google.common.base.Preconditions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ru.akvine.custodian.admin.controllers.rest.config.security.ClientAuthentication;
import ru.akvine.custodian.core.exceptions.security.NoSessionException;
import ru.akvine.custodian.core.services.domain.ClientBean;

@UtilityClass
public class SecurityUtils {
    public HttpSession getSession(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            throw new NoSessionException();
        }
        return session;
    }

    public void authenticate(ClientBean client) {
        SecurityContextHolder.getContext().setAuthentication(
                new ClientAuthentication(
                        client.getId(),
                        client.getUuid(),
                        client.getEmail()
                )
        );
    }

    public ClientAuthentication getCurrentUser() {
        ClientAuthentication user = getCurrentUserOrNull();
        Preconditions.checkNotNull(user, "user is null");
        return user;
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
        ClientAuthentication user = SecurityUtils.getCurrentUserOrNull();
        if (user == null) {
            return;
        }
        SecurityContextHolder.clearContext();
    }
}
