package ru.akvine.custodian.admin.controllers.rest.converters;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.custodian.admin.controllers.rest.dto.token.TokenDto;
import ru.akvine.custodian.admin.controllers.rest.dto.token.TokenGenerateRequest;
import ru.akvine.custodian.admin.controllers.rest.dto.token.TokenGenerateResponse;
import ru.akvine.custodian.admin.controllers.rest.dto.token.TokenListResponse;
import ru.akvine.custodian.admin.controllers.rest.utils.SecurityHelper;
import ru.akvine.custodian.core.enums.AccessRights;
import ru.akvine.custodian.core.repositories.projections.AccessTokenProjection;
import ru.akvine.custodian.core.services.domain.AccessTokenBean;
import ru.akvine.custodian.core.services.dto.token.TokenGenerate;
import ru.akvine.custodian.core.utils.DateUtils;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccessTokenConverter {
    private final SecurityHelper securityHelper;

    public TokenGenerate convertToTokenGenerate(TokenGenerateRequest request) {
        Preconditions.checkNotNull(request, "TokenGenerateRequest is null");
        return new TokenGenerate()
                .setClientId(securityHelper.getCurrentUser().getId())
                .setAppTitle(request.getAppTitle())
                .setAccessRights(convertToAccessRights(request.getAccessRights()))
                .setExpiredAt(request.getExpiredDate() == null ? null : DateUtils.convertToZonedDateTime(request.getExpiredDate()));
    }

    private List<AccessRights> convertToAccessRights(String value) {
        if (StringUtils.isBlank(value)) {
            return List.of(AccessRights.values());
        }

        return Arrays.stream(value.split(",")).map(AccessRights::safeFrom).toList();
    }

    public TokenGenerateResponse convertToTokenGenerateResponse(AccessTokenBean accessTokenBean) {
        Preconditions.checkNotNull(accessTokenBean, "AccessTokenBean is null");
        return new TokenGenerateResponse().setToken(accessTokenBean.getToken());
    }

    public TokenListResponse convertToTokenListResponse(List<AccessTokenProjection> tokens) {
        Preconditions.checkNotNull(tokens, "tokens is null");
        return new TokenListResponse().setTokens(tokens.stream().map(this::buildTokenDto).toList());
    }

    private TokenDto buildTokenDto(AccessTokenProjection tokenProjection) {
        return new TokenDto()
                .setAppTitle(tokenProjection.appTitle())
                .setToken(tokenProjection.token());
    }
}
