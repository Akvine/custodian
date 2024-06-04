package ru.akvine.custodian.core.constants;

public final class DBLockPrefixesConstants {
    private DBLockPrefixesConstants() {
        throw new IllegalStateException("Call DBLockPrefixesConstants constructor is prohibited!");
    }

    public static final String ACCESS_RESTORE_PREFIX = "ACCESS_RESTORE_";
    public static final String AUTH_PREFIX = "AUTH_";
    public static final String REG_PREFIX = "REG_";
}
