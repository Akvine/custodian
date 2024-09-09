package ru.akvine.custodian.integration.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.akvine.custodian.core.repositories.AccessTokenRepository;
import ru.akvine.custodian.core.repositories.entities.AccessTokenEntity;
import ru.akvine.custodian.core.services.domain.AccessTokenBean;
import ru.akvine.custodian.integration.exceptions.AuthException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccessTokenRepository accessTokenRepository;

    private final static String BEARER_TOKEN_PREFIX = "Bearer ";

    public AccessTokenBean check(String authorizationToken) {
        Preconditions.checkNotNull(authorizationToken, "authorizationToken is null");

        String extractedToken = extractToken(authorizationToken);
        Optional<AccessTokenEntity> accessTokenEntityOptional = accessTokenRepository.findByToken(extractedToken);
        if  (accessTokenEntityOptional.isEmpty() || accessTokenEntityOptional.get().isExpired()) {
            throw new AuthException("Access token is invalid");
        }

        return new AccessTokenBean(accessTokenEntityOptional.get());
    }

    private String extractToken(String authorizationToken) {
        if (StringUtils.startsWith(authorizationToken, BEARER_TOKEN_PREFIX)) {
            return StringUtils.substringAfter(authorizationToken, BEARER_TOKEN_PREFIX);
        } else {
            throw new AuthException("Invalid token format");
        }
    }
}
