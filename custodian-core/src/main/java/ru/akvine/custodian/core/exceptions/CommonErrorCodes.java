package ru.akvine.custodian.core.exceptions;

public final class CommonErrorCodes {
    private CommonErrorCodes() {
        throw new IllegalStateException("Calling CommonErrorCodes constructor is prohibited!");
    }

    public static final String GENERAL_ERROR = "general.error";
    public static final String NO_SESSION = "no.session.error";

    public static final String CLIENT_ALREADY_EXISTS_ERROR = "client.already.exists.error";
    public static final String CLIENT_NOT_FOUND_ERROR = "client.notFound.error";

    public static final String APP_ALREADY_EXISTS_ERROR = "app.already.exists.error";
    public static final String APP_NOT_FOUND_ERROR = "app.notFound.error";

    public static final String PROPERTY_ALREADY_EXISTS_ERROR = "property.already.exists.error";
    public static final String PROPERTY_NOT_FOUND_ERROR = "property.notFound.error";

    public interface Validation {
        String DATE_INVALID_ERROR = "date.invalid.error";

        String AGE_INVALID_ERROR = "age.invalid.error";

        String EMAIL_BLANK_ERROR = "email.blank.error";
        String EMAIL_INVALID_ERROR = "email.invalid.error";

        String FILE_SIZE_INVALID_ERROR = "file.size.invalid.error";
        String FILE_CONTENT_TYPE_INVALID_ERROR = "file.content-type.invalid.error";

        String FIELD_INVALID_LENGTH_ERROR = "field.invalid.length.error";

        String REGISTRATION_PASSWORD_BLANK_ERROR = "registration.password.blank.error";
        String REGISTRATION_PASSWORD_INVALID_ERROR = "registration.password.invalid.error";
    }
}
