package ru.akvine.custodian.core.exceptions.security;

public class RegistrationNotStartedException extends RuntimeException {
    public RegistrationNotStartedException(String message) {
        super(message);
    }
}
