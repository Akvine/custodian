package ru.akvine.custodian.core.exceptions.security;

public class RegistrationWrongStateException extends RuntimeException {
    public RegistrationWrongStateException(String message) {
        super(message);
    }
}
