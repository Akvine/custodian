package ru.akvine.custodian.validators;

public interface Validator<T> {
    void validate(T obj);
}
