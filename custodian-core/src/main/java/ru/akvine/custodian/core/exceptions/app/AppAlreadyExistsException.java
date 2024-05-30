package ru.akvine.custodian.core.exceptions.app;

public class AppAlreadyExistsException extends RuntimeException {
    public AppAlreadyExistsException(String message) {
        super(message);
    }
}
