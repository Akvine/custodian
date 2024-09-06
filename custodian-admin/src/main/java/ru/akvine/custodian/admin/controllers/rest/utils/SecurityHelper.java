package ru.akvine.custodian.admin.controllers.rest.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import ru.akvine.custodian.core.exceptions.security.NoSessionException;
import ru.akvine.custodian.core.services.domain.ClientBean;

@Component
@RequiredArgsConstructor
public class SecurityHelper {
    private final UserDetailsService userDetailsService;

    public void authenticate(ClientBean clientBean, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(clientBean.getEmail());
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authToken);

        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", context);
    }

    //    @Nullable
//    public ClientAuthentication getCurrentUserOrNull() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication instanceof ClientAuthentication) {
//            return (ClientAuthentication) authentication;
//        }
//        return null;
//    }
//
//    public void doLogout(HttpServletRequest request) {
//        if (request == null) {
//            return;
//        }
//        HttpSession session = request.getSession(false);
//        if (session == null) {
//            return;
//        }
//        ClientAuthentication user = getCurrentUserOrNull();
//        if (user == null) {
//            return;
//        }
//        SecurityContextHolder.clearContext();
//    }
//
//    public ClientAuthentication getCurrentUser() {
//        ClientAuthentication user = getCurrentUserOrNull();
//        Preconditions.checkNotNull(user, "user is null");
//        return user;
//    }
//
    public HttpSession getSession(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            throw new NoSessionException();
        }
        return session;
    }
}
