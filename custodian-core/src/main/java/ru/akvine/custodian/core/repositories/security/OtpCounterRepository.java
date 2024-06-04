package ru.akvine.custodian.core.repositories.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.custodian.core.repositories.entities.security.OtpCounterEntity;

public interface OtpCounterRepository extends JpaRepository<OtpCounterEntity, Long> {
    @Query("from OtpCounterEntity oce where oce.login = :login")
    OtpCounterEntity findByLogin(@Param("login") String login);
}
