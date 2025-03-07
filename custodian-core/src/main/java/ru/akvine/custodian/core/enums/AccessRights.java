package ru.akvine.custodian.core.enums;

import org.apache.commons.lang3.StringUtils;

public enum AccessRights {
    CREATE,
    RETRIEVE,
    UPDATE,
    DELETE;

    public static AccessRights safeFrom(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("Value can't be blank!");
        }

        switch (value.toLowerCase()) {
            case "create" -> {
                return CREATE;
            }
            case "retrieve" -> {
                return RETRIEVE;
            }
            case "update" -> {
                return UPDATE;
            }
            case "delete" -> {
                return DELETE;
            }
            default ->
                    throw new UnsupportedOperationException("Access right = [" + value + "] is not supported by app!");
        }
    }
}
