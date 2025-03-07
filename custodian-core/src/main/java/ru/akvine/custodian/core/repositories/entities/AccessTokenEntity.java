package ru.akvine.custodian.core.repositories.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.custodian.core.repositories.entities.base.BaseEntity;

import java.time.ZonedDateTime;

@Entity
@Table(name = "ACCESS_TOKEN_ENTITY")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class AccessTokenEntity extends BaseEntity {
    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accessTokenEntitySeq")
    @SequenceGenerator(name = "accessTokenEntitySeq", sequenceName = "SEQ_ACCESS_TOKEN_ENTITY", allocationSize = 1000)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APP_ID", nullable = false)
    private AppEntity app;

    @Column(name = "TOKEN", nullable = false)
    private String token;

    @Column(name = "EXPIRED_AT", nullable = false)
    private ZonedDateTime expiredAt;

    @Transient
    public boolean isExpired() {
        return getCreatedDate().isAfter(expiredAt);
    }

    @Column(name = "ACCESS_RIGHTS", nullable = false)
    private String accessRights = "ALL";
}
