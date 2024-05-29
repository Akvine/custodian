package ru.akvine.custodian.controllers.rest.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.custodian.enums.FileType;
import ru.akvine.custodian.exceptions.CommonErrorCodes;
import ru.akvine.custodian.exceptions.validation.ValidationException;
import ru.akvine.custodian.managers.FileValidatorsManager;

@Component
@RequiredArgsConstructor
public class PropertyValidator {
    private final static int PROFILE_MAX_LENGTH = 100;
    private final static int APP_TITLE_MAX_LENGTH = 100;

    private final FileValidatorsManager fileValidatorsManager;

    public void verifyImportProperties(MultipartFile file, String appTitle, String profile) {
        fileValidatorsManager
                .getFileValidators()
                .get(FileType.PROPERTIES)
                .validate(file);

        if (appTitle.length() > APP_TITLE_MAX_LENGTH) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.FIELD_INVALID_LENGTH_ERROR,
                    "Field 'appTitle' has invalid length");
        }

        if (profile.length() > PROFILE_MAX_LENGTH) {
            throw new ValidationException(
                    CommonErrorCodes.Validation.FIELD_INVALID_LENGTH_ERROR,
                    "Field 'profile' has invalid length");
        }
    }
}
