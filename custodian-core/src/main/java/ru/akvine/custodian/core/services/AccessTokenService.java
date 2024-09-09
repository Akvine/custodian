package ru.akvine.custodian.core.services;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.commons.util.UUIDGenerator;
import ru.akvine.custodian.core.repositories.AccessTokenRepository;
import ru.akvine.custodian.core.repositories.entities.AccessTokenEntity;
import ru.akvine.custodian.core.repositories.entities.AppEntity;
import ru.akvine.custodian.core.repositories.projections.AccessTokenProjection;
import ru.akvine.custodian.core.services.domain.AccessTokenBean;
import ru.akvine.custodian.core.services.domain.AppBean;
import ru.akvine.custodian.core.services.dto.token.TokenGenerate;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccessTokenService {
    private final AccessTokenRepository accessTokenRepository;
    private final AppService appService;

    @Value("${default.expired.days.count}")
    private int defaultExpiredDaysCount;

    public AccessTokenBean generate(TokenGenerate tokenGenerate) {
        Preconditions.checkNotNull(tokenGenerate, "TokenGenerate is null");
        logger.debug("Generate new token for app with title = {} for client with id = {}",
                tokenGenerate.getAppTitle(), tokenGenerate.getClientId());

        AppEntity appEntity = appService.verifyExistsByTitle(
                tokenGenerate.getAppTitle(),
                tokenGenerate.getClientId());
        Optional<AccessTokenEntity> token = accessTokenRepository.findByAppId(appEntity.getId());
        ZonedDateTime expiredAt = tokenGenerate.getExpiredAt();
        if (expiredAt == null) {
            expiredAt = ZonedDateTime.now().plusDays(defaultExpiredDaysCount);
        }

        AccessTokenEntity generatedToken;
        if (token.isEmpty()) {
            generatedToken = new AccessTokenEntity()
                    .setExpiredAt(expiredAt)
                    .setApp(appEntity)
                    .setToken(UUIDGenerator.uuidWithNoDashes());

        } else {
            generatedToken = token.get().setToken(UUIDGenerator.uuidWithNoDashes());
        }

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
