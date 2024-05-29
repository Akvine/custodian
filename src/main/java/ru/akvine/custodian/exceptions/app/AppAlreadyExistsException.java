package ru.akvine.custodian.exceptions.app;

public class AppAlreadyExistsException extends RuntimeException {
    public AppAlreadyExistsException(String message) {
        super(message);
    }
}
