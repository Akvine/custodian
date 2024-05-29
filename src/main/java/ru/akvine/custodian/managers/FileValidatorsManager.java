package ru.akvine.custodian.managers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.akvine.custodian.enums.FileType;
import ru.akvine.custodian.validators.file.FileValidator;

import java.util.Map;

@Getter
@AllArgsConstructor
public class FileValidatorsManager {
    private final Map<FileType, FileValidator> fileValidators;
}
