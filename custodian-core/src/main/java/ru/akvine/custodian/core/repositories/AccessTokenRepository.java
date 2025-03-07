package ru.akvine.custodian.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.custodian.core.repositories.entities.AccessTokenEntity;
import ru.akvine.custodian.core.repositories.projections.AccessTokenProjection;

import java.util.List;
import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessTokenEntity, Long> {
    @Query("from AccessTokenEntity ate join app atep where atep.id = :appId")
    Optional<AccessTokenEntity> findByAppId(@Param("appId") long appId);

    @Query("select new ru.akvine.custodian.core.repositories.projections.AccessTokenProjection(atep.title, ate.token) " +
            "from AccessTokenEntity ate join app atep where atep.id in :ids and " +
            "atep.deleted = false and atep.deletedDate is null")
    List<AccessTokenProjection> findByAppIds(@Param("ids") List<Long> appIds);

    @Query("from AccessTokenEntity ate where ate.token = :token")
    Optional<AccessTokenEntity> findByToken(@Param("token") String token);

    @Query("select count(*) from AccessTokenEntity ate " +
            "where ate.app.title = :title")
    long count(@Param("title") String appTitle);

}
