package ru.akvine.custodian.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum ExportFileType {
    XLSX("xlsx", "application/vnd.ms-excel");

    private final String extension;
    private final String mimeType;

    public static ExportFileType safeFrom(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("Value can't be blank!");
        }

        switch (value.toLowerCase()) {
            case "xlsx" -> {
                return XLSX;
            }
            default -> throw new UnsupportedOperationException("Export file type = [" + value + "] is not supported by app!");
        }
    }
}
