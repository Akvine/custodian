package ru.akvine.custodian.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.akvine.custodian.core.enums.ExportFileType;
import ru.akvine.custodian.core.enums.ImportFileType;
import ru.akvine.custodian.core.managers.ExportersManagers;
import ru.akvine.custodian.core.managers.FileValidatorsManager;
import ru.akvine.custodian.core.services.export.ExporterService;
import ru.akvine.custodian.core.validators.file.FileValidator;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;


@Configuration
public class ManagersConfig {

    @Bean
    public FileValidatorsManager fileManager(List<FileValidator> fileValidators) {
        Map<ImportFileType, FileValidator> availableFileValidators = fileValidators
                .stream()
                .collect(toMap(FileValidator::getType, identity()));
        return new FileValidatorsManager(availableFileValidators);
    }

    @Bean
    public ExportersManagers exportersManagers(List<ExporterService> exporterServices) {
        Map<ExportFileType, ExporterService> exporters = exporterServices
                .stream()
                .collect(toMap(ExporterService::getType, identity()));
        return new ExportersManagers(exporters);
    }
}
