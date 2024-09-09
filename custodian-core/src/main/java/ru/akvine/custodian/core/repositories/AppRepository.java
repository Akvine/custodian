package ru.akvine.custodian.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.akvine.custodian.core.repositories.entities.AppEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppRepository extends JpaRepository<AppEntity, Long> {
    @Query("from AppEntity ae where ae.title = :title and ae.client.id = :clientId and " +
            "ae.deleted = false and ae.deletedDate is null")
    Optional<AppEntity> findByTitleAndClientId(@Param("title") String title, @Param("clientId") long clientId);

    @Query("from AppEntity ae join ae.client aec where aec.id = :clientId and " +
            "ae.deleted = false and ae.deletedDate is null and aec.deleted = false and aec.deletedDate is null")
    List<AppEntity> findByClientId(@Param("clientId") long clientId);
}
