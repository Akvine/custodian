package ru.akvine.custodian.core.services.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.custodian.core.repositories.entities.AccessTokenEntity;
import ru.akvine.custodian.core.services.domain.base.Bean;

import java.time.ZonedDateTime;

@Data
@Accessors(chain = true)
public class AccessTokenBean extends Bean {
    private Long id;
    private AppBean app;
    private String token;
    private ZonedDateTime expiredAt;

    public AccessTokenBean(AccessTokenEntity accessTokenEntity) {
        this.id = accessTokenEntity.getId();
        this.app = new AppBean(accessTokenEntity.getApp());
        this.token = accessTokenEntity.getToken();
        this.expiredAt = accessTokenEntity.getExpiredAt();
    }
}
