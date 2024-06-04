package ru.akvine.custodian.core.exceptions.security;

public class BlockedCredentialsException extends RuntimeException {
    public BlockedCredentialsException(String message) {
        super(message);
    }
}
