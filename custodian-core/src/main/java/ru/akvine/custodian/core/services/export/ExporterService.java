package ru.akvine.custodian.core.services.export;

import ru.akvine.custodian.core.enums.ExportFileType;
import ru.akvine.custodian.core.services.domain.PropertyBean;

import java.util.List;
import java.util.Map;

public interface ExporterService {
    byte[] export(Map<String, List<PropertyBean>> profilesWithProperties);

    ExportFileType getType();
}
