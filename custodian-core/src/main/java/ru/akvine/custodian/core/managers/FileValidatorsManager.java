package ru.akvine.custodian.core.managers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.akvine.custodian.core.enums.FileType;
import ru.akvine.custodian.core.validators.file.FileValidator;

import java.util.Map;

@Getter
@AllArgsConstructor
public class FileValidatorsManager {
    private final Map<FileType, FileValidator> fileValidators;
}
