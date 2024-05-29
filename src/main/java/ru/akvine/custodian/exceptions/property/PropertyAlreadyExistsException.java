package ru.akvine.custodian.exceptions.property;

public class PropertyAlreadyExistsException extends RuntimeException {
    public PropertyAlreadyExistsException(String message) {
        super(message);
    }
}
