package ru.akvine.custodian.core.exceptions.security;

public class OtpAuthRequiredException extends RuntimeException {
    public OtpAuthRequiredException(String message) {
        super(message);
    }
}
