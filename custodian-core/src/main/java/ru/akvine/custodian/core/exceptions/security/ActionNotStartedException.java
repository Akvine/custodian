package ru.akvine.custodian.core.exceptions.security;

public class ActionNotStartedException extends RuntimeException {
    public ActionNotStartedException(String message) {
        super(message);
    }
}
