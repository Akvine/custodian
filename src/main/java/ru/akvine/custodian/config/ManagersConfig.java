package ru.akvine.custodian.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.akvine.custodian.enums.FileType;
import ru.akvine.custodian.validators.file.FileValidator;
import ru.akvine.custodian.managers.FileValidatorsManager;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Configuration
public class ManagersConfig {

    @Bean
    public FileValidatorsManager fileManager(List<FileValidator> fileValidators) {
        Map<FileType, FileValidator> availableFileValidators = fileValidators
                .stream()
                .collect(toMap(FileValidator::getType, identity()));
        return new FileValidatorsManager(availableFileValidators);
    }
}
