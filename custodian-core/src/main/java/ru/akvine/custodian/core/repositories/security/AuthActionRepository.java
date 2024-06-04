package ru.akvine.custodian.core.repositories.security;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.custodian.core.repositories.entities.security.AuthActionEntity;

public interface AuthActionRepository extends ActionRepository<AuthActionEntity> {
    @Query("from AuthActionEntity aae where aae.login = :login")
    AuthActionEntity findCurrentAction(@Param("login") String login);
}
