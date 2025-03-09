package ru.akvine.custodian.core.services.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.custodian.core.enums.AccessRights;
import ru.akvine.custodian.core.repositories.entities.AccessTokenEntity;
import ru.akvine.custodian.core.services.domain.base.Model;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

@Data
@Accessors(chain = true)
public class AccessTokenModel extends Model {
    private Long id;
    private AppModel app;
    private String token;
    private ZonedDateTime expiredAt;
    private List<AccessRights> accessRights;

    public AccessTokenModel(AccessTokenEntity accessTokenEntity) {
        this.id = accessTokenEntity.getId();
        this.app = new AppModel(accessTokenEntity.getApp());
        this.token = accessTokenEntity.getToken();
        this.expiredAt = accessTokenEntity.getExpiredAt();

        this.accessRights = Arrays.stream(accessTokenEntity.getAccessRights().split(","))
                .map(AccessRights::safeFrom)
                .toList();
    }
}
