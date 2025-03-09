package ru.akvine.custodian.core.managers;

import ru.akvine.custodian.core.enums.ExportFileType;
import ru.akvine.custodian.core.services.export.ExporterService;

import java.util.Map;

public record ExportersManagers(Map<ExportFileType, ExporterService> exporters) {

    public ExporterService getByType(ExportFileType type) {
        if (exporters.containsKey(type)) {
            return exporters.get(type);
        }

        throw new UnsupportedOperationException("Exporter by file type = [" + type + "] is not supported by app!");
    }
}
