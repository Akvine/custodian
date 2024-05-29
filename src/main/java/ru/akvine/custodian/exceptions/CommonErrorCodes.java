package ru.akvine.custodian.exceptions;

public final class CommonErrorCodes {
    private CommonErrorCodes() {
        throw new IllegalStateException("Calling CommonErrorCodes constructor is prohibited!");
    }

    public static final String GENERAL_ERROR = "general.error";

    public interface Validation {
        String EMAIL_BLANK_ERROR = "email.blank.error";
        String EMAIL_INVALID_ERROR = "email.invalid.error";

        String FILE_SIZE_INVALID_ERROR = "file.size.invalid.error";
        String FILE_CONTENT_TYPE_INVALID_ERROR = "file.content-type.invalid.error";

        String FIELD_INVALID_LENGTH_ERROR = "field.invalid.length.error";
    }
}
