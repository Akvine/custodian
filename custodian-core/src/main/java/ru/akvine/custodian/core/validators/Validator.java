package ru.akvine.custodian.core.validators;

public interface Validator<T> {
    void validate(T obj);
}
