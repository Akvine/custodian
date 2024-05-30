package ru.akvine.custodian.core.exceptions.app;

public class AppNotFoundException extends RuntimeException {
    public AppNotFoundException(String message) {
        super(message);
    }
}
