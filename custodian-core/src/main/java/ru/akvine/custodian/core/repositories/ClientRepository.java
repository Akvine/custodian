package ru.akvine.custodian.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.akvine.custodian.core.repositories.entities.ClientEntity;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    @Query("from ClientEntity ce where ce.uuid = :uuid and ce.deleted = false and ce.deletedDate is null")
    Optional<ClientEntity> findByUuid(@Param("uuid") String uuid);

    @Query("from ClientEntity ce where ce.email = :email and ce.deleted = false and ce.deletedDate is null")
    Optional<ClientEntity> findByEmail(@Param("email") String email);
}
