package ru.akvine.custodian.core.repositories.entities.security;

public interface OneTimePasswordable {
    Long getId();
    String getLogin();
    OtpActionEntity getOtpAction();
    String getSessionId();
}
