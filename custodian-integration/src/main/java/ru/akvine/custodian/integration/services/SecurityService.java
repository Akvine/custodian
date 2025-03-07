package ru.akvine.custodian.integration.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.akvine.custodian.core.enums.AccessRights;
import ru.akvine.custodian.core.repositories.AccessTokenRepository;
import ru.akvine.custodian.core.repositories.entities.AccessTokenEntity;
import ru.akvine.custodian.core.services.domain.AccessTokenBean;
import ru.akvine.custodian.core.utils.Asserts;
import ru.akvine.custodian.integration.exceptions.AuthException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final AccessTokenRepository accessTokenRepository;

    private final static String BEARER_TOKEN_PREFIX = "Bearer ";

    public AccessTokenBean check(String authorizationToken) {
        Asserts.isNotNull(authorizationToken, "authorizationToken is null");

        String extractedToken = extractToken(authorizationToken);
        Optional<AccessTokenEntity> accessTokenEntityOptional = accessTokenRepository.findByToken(extractedToken);
        if  (accessTokenEntityOptional.isEmpty() || accessTokenEntityOptional.get().isExpired()) {
            throw new AuthException("Access token is invalid");
        }

        return new AccessTokenBean(accessTokenEntityOptional.get());
    }

    public void checkAccess(AccessTokenBean accessToken, AccessRights accessRights) {
        Asserts.isNotNull(accessToken, "accessToken is null");
        Asserts.isNotNull(accessRights, "accessRights is null");

        if (!accessToken.getAccessRights().contains(accessRights)) {
            String errorMessage = "Access denied. The provided token does not have the necessary permissions to perform this action";
            throw new AuthException(errorMessage);
        }
    }

    private String extractToken(String authorizationToken) {
        if (StringUtils.startsWith(authorizationToken, BEARER_TOKEN_PREFIX)) {
            return StringUtils.substringAfter(authorizationToken, BEARER_TOKEN_PREFIX);
        } else {
            throw new AuthException("Invalid token format");
        }
    }
}
