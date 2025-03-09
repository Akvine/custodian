package ru.akvine.custodian.admin.controllers.rest.converters;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.custodian.admin.controllers.rest.dto.token.*;
import ru.akvine.custodian.admin.controllers.rest.utils.SecurityHelper;
import ru.akvine.custodian.core.enums.AccessRights;
import ru.akvine.custodian.core.repositories.projections.AccessTokenProjection;
import ru.akvine.custodian.core.services.domain.AccessTokenModel;
import ru.akvine.custodian.core.services.dto.token.TokenDelete;
import ru.akvine.custodian.core.services.dto.token.TokenGenerate;
import ru.akvine.custodian.core.utils.Asserts;
import ru.akvine.custodian.core.utils.DateUtils;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccessTokenConverter {
    private final SecurityHelper securityHelper;

    public TokenGenerate convertToTokenGenerate(TokenGenerateRequest request) {
        Asserts.isNotNull(request, "TokenGenerateRequest is null");
        return new TokenGenerate()
                .setClientId(securityHelper.getCurrentUser().getId())
                .setAppTitle(request.getAppTitle())
                .setAccessRights(convertToAccessRights(request.getAccessRights()))
                .setExpiredAt(request.getExpiredDate() == null ? null : DateUtils.convertToZonedDateTime(request.getExpiredDate()));
    }

    public TokenDelete convertToTokenDelete(TokenDeleteRequest request) {
        Asserts.isNotNull(request);
        return new TokenDelete()
                .setClientId(securityHelper.getCurrentUser().getId())
                .setAppTitle(request.getAppTitle())
                .setTokens(request.getTokens());
    }

    private List<AccessRights> convertToAccessRights(String value) {
        if (StringUtils.isBlank(value)) {
            return List.of(AccessRights.values());
        }

        return Arrays.stream(value.split(",")).map(AccessRights::safeFrom).toList();
    }

    public TokenGenerateResponse convertToTokenGenerateResponse(AccessTokenModel accessTokenBean) {
        Asserts.isNotNull(accessTokenBean, "AccessTokenBean is null");
        return new TokenGenerateResponse().setToken(accessTokenBean.getToken());
    }

    public TokenListResponse convertToTokenListResponse(List<AccessTokenProjection> tokens) {
        Asserts.isNotNull(tokens, "tokens is null");
        List<TokenDto> tokensDtos = tokens.stream().map(this::buildTokenDto).toList();
        return new TokenListResponse()
                .setCount(tokensDtos.size())
                .setTokens(tokensDtos);
    }

    private TokenDto buildTokenDto(AccessTokenProjection tokenProjection) {
        return new TokenDto()
                .setAppTitle(tokenProjection.appTitle())
                .setToken(tokenProjection.token())
                .setExpiredAt(tokenProjection.expiredAt().toString());
    }
}
