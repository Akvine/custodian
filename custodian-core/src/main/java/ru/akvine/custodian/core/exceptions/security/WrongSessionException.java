package ru.akvine.custodian.core.exceptions.security;

public class WrongSessionException extends RuntimeException {
    public WrongSessionException(String message) {
        super(message);
    }
}
