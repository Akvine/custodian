package ru.akvine.custodian.core.services.dto.property;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.custodian.core.enums.ExportFileType;

import javax.annotation.Nullable;
import java.util.Set;

@Data
@Accessors(chain = true)
public class PropertyExport {
    private long clientId;
    private String appTitle;
    private ExportFileType fileType;
    @Nullable
    private Set<String> profiles;
}
