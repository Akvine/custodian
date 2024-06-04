package ru.akvine.custodian.core.repositories.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.akvine.custodian.core.repositories.entities.security.OneTimePasswordable;

@NoRepositoryBean
public interface ActionRepository <T extends OneTimePasswordable> extends JpaRepository<T, Long> {
    T findCurrentAction(String payload);
}
