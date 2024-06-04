package ru.akvine.custodian.core.repositories.entities.security;

public interface AccountPasswordable {
    int decrementPwdInvalidAttemptsLeft();
    int getPwdInvalidAttemptsLeft();
}
