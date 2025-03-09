package ru.akvine.custodian.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.custodian.core.repositories.entities.PropertyEntity;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {
    @Query("from PropertyEntity pe join pe.app pea where pea.title = :appTitle and " +
            "pe.profile = :profile and pe.key = :key and " +
            "pe.deleted = false and pe.deletedDate is null and pea.deleted = false and pea.deletedDate is null")
    Optional<PropertyEntity> findByAppTitleAndProfileAndKey(@Param("appTitle") String appTitle,
                                                            @Param("profile") String profile,
                                                            @Param("key") String key);

    @Query("from PropertyEntity pe join pe.app pea where pea.title = :appTitle and " +
            "pe.profile in :profiles and " +
            "pe.deleted = false and pe.deletedDate is null and pea.deleted = false and pea.deletedDate is null")
    List<PropertyEntity> findByAppTitleAndProfiles(@Param("appTitle") String appTitle,
                                                   @Param("profiles") Collection<String> profiles);

    @Query("from PropertyEntity pe join pe.app pea where pea.title = :appTitle and " +
            "pe.profile = :profile and pe.key in :keys and " +
            "pe.deleted = false and pe.deletedDate is null and pea.deleted = false and pea.deletedDate is null")
    List<PropertyEntity> findByAppTitleAndProfileAndKeys(@Param("appTitle") String appTitle,
                                                         @Param("profile") String profile,
                                                         @Param("keys") Collection<String> keys);

    @Query("from PropertyEntity pe join pe.app pea where pea.title = :appTitle and " +
            "pe.deleted = false and pe.deletedDate is null and pea.deleted = false and pea.deletedDate is null")
    List<PropertyEntity> findByAppTitle(@Param("appTitle") String appTitle);

    @Modifying
    @Transactional
    @Query(value = "update PROPERTY_ENTITY " +
            "set is_deleted = TRUE, deleted_date = :deleteDate " +
            "from APP_ENTITY " +
            "where PROPERTY_ENTITY.app_id = APP_ENTITY.id " +
            "and " +
            "APP_ENTITY.title = :title " +
            "and " +
            "PROPERTY_ENTITY.profile = :profile " +
            "and " +
            "PROPERTY_ENTITY.key in :keys", nativeQuery = true)
    void delete(@Param("title") String appTitle,
                @Param("profile") String profile,
                @Param("keys") Collection<String> keys,
                @Param("deleteDate") ZonedDateTime deleteDate);

    @Modifying
    @Transactional
    @Query(value = "update PROPERTY_ENTITY " +
            "set is_deleted = true, deleted_date = :deleteDate " +
            "from APP_ENTITY " +
            "where PROPERTY_ENTITY.app_id = APP_ENTITY.id " +
            "and " +
            "APP_ENTITY.title = :title", nativeQuery = true)
    void delete(@Param("title") String appTitle,
                @Param("deleteDate") ZonedDateTime deleteDate);
}
