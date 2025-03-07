package ru.akvine.custodian.core.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.commons.util.UUIDGenerator;
import ru.akvine.custodian.core.enums.AccessRights;
import ru.akvine.custodian.core.exceptions.token.GenerateTokenException;
import ru.akvine.custodian.core.repositories.AccessTokenRepository;
import ru.akvine.custodian.core.repositories.entities.AccessTokenEntity;
import ru.akvine.custodian.core.repositories.entities.AppEntity;
import ru.akvine.custodian.core.repositories.projections.AccessTokenProjection;
import ru.akvine.custodian.core.services.domain.AccessTokenBean;
import ru.akvine.custodian.core.services.domain.AppBean;
import ru.akvine.custodian.core.services.dto.token.TokenGenerate;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccessTokenService {
    private final AccessTokenRepository accessTokenRepository;
    private final AppService appService;

    @Value("${default.expired.days.count}")
    private int defaultExpiredDaysCount;
    @Value("${access.token.max.count.per.app}")
    private int maxCountPerApp;

    public AccessTokenBean generate(TokenGenerate tokenGenerate) {
        Preconditions.checkNotNull(tokenGenerate, "TokenGenerate is null");
        logger.debug("Generate new token for app with title = {} for client with id = {}",
                tokenGenerate.getAppTitle(), tokenGenerate.getClientId());

        AppEntity appEntity = appService.verifyExistsByTitle(
                tokenGenerate.getAppTitle(),
                tokenGenerate.getClientId());

        long count = accessTokenRepository.count(appEntity.getTitle());
        if (count == maxCountPerApp) {
            String errorMessage = String.format(
                    "Can't generate token! Limit per app reached = [%s]",
                    maxCountPerApp
                    );
            throw new GenerateTokenException(errorMessage);
        }

        ZonedDateTime expiredAt = tokenGenerate.getExpiredAt();
        if (expiredAt == null) {
            expiredAt = ZonedDateTime.now().plusDays(defaultExpiredDaysCount);
        }

        AccessTokenEntity generatedToken = new AccessTokenEntity()
                .setExpiredAt(expiredAt)
                .setApp(appEntity)
                .setToken(UUIDGenerator.uuidWithNoDashes())
                .setAccessRights(String.join(",", tokenGenerate.getAccessRights()
                        .stream().map(AccessRights::toString).toList()));

        logger.debug("Access token was successful generated");
        return new AccessTokenBean(accessTokenRepository.save(generatedToken));
    }

    public List<AccessTokenProjection> list(long clientId) {
        logger.debug("List all tokens for client with id = {}", clientId);

        List<AppBean> apps = appService.list(clientId);
        List<Long> appIds = apps.stream().map(AppBean::getId).toList();

        return accessTokenRepository
                .findByAppIds(appIds);
    }
}
