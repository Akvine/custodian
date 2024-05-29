package ru.akvine.custodian.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.custodian.repositories.entities.PropertyEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
}
