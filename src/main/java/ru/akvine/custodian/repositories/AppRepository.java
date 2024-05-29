package ru.akvine.custodian.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.custodian.repositories.entities.AppEntity;

import java.util.Optional;

public interface AppRepository extends JpaRepository<AppEntity, Long> {
    @Query("from AppEntity ae where ae.title = :title and " +
            "ae.deleted = false and ae.deletedDate is null")
    Optional<AppEntity> findByTitle(@Param("title") String title);
}
