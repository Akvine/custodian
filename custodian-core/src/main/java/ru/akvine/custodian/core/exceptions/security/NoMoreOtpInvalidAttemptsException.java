package ru.akvine.custodian.core.exceptions.security;

public class NoMoreOtpInvalidAttemptsException extends RuntimeException {
    public NoMoreOtpInvalidAttemptsException(String message) {
        super(message);
    }
}
