package ru.akvine.custodian.core.utils;

import lombok.experimental.UtilityClass;
import ru.akvine.custodian.core.exceptions.AssertsException;

import java.util.Objects;

@UtilityClass
public class Asserts {
    public void isNotNull(Object object, String message) {
        if (Objects.isNull(object)) {
            throw new AssertsException(message);
        }
    }

    public void isNotNull(Object object) {
        String message = object.getClass().getSimpleName() + " is null!";
        isNotNull(object, message);
    }
}

