package ru.akvine.custodian.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.custodian.repositories.entities.ClientEntity;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    @Query("from ClientEntity ce where ce.uuid = :uuid and ce.deleted = false and ce.deletedDate is null")
    Optional<ClientEntity> findByUuid(@Param("uuid") String uuid);

    @Query("from ClientEntity ce where ce.email = :email and ce.deleted = false and ce.deletedDate is null")
    Optional<ClientEntity> findByEmail(@Param("email") String email);
}
