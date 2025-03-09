package ru.akvine.custodian.core.validators.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.custodian.core.enums.ImportFileType;

import java.util.List;

@Component
public class FilePropertiesValidator extends FileValidator {
    @Value("${import.file.properties.max-size.bytes}")
    private long maxSizeBytes;
    @Value("${import.file.properties.available-content-types}")
    private List<String> availableTypes;

    @Override
    public void validate(MultipartFile file) {
        validate(file, maxSizeBytes, availableTypes);
    }

    @Override
    public ImportFileType getType() {
        return ImportFileType.PROPERTIES;
    }
}
