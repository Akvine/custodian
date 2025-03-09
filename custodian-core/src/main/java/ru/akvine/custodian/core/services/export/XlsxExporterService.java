package ru.akvine.custodian.core.services.export;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.akvine.custodian.core.enums.ExportFileType;
import ru.akvine.custodian.core.exceptions.property.PropertyExportException;
import ru.akvine.custodian.core.services.domain.PropertyBean;
import ru.akvine.custodian.core.utils.Asserts;
import ru.akvine.custodian.core.utils.POIUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class XlsxExporterService implements ExporterService {
    @Override
    public byte[] export(Map<String, List<PropertyBean>> profilesWithProperties) {
        Asserts.isNotNull(profilesWithProperties, "profilesWithProperties is null");

        try (Workbook workbook = new XSSFWorkbook()) {
            Set<String> profiles = profilesWithProperties.keySet();
            for (String profile : profiles) {
                Sheet sheet = workbook.createSheet(profile);
                createHeaders(sheet);

                List<PropertyBean> properties = profilesWithProperties.get(profile);
                for (int i = 0; i < properties.size(); ++i) {
                    Row row = sheet.createRow(i + 1);

                    Cell id = row.createCell(0);
                    id.setCellValue(i + 1);

                    Cell key = row.createCell(1);
                    key.setCellValue(properties.get(i).getKey());

                    Cell value = row.createCell(2);
                    value.setCellValue(properties.get(i).getValue());

                    Cell description = row.createCell(3);
                    description.setCellValue(properties.get(i).getDescription());
                }
            }

            return POIUtils.mapToBytes(workbook);
        } catch (IOException exception) {
            String errorMessage = String.format("Error while generate report: [%s]", exception.getMessage());
            throw new PropertyExportException(errorMessage);
        }
    }

    @Override
    public ExportFileType getType() {
        return ExportFileType.XLSX;
    }

    private void createHeaders(Sheet sheet) {
        Row headers = sheet.createRow(0);

        Cell id = headers.createCell(0);
        id.setCellValue("ID");

        Cell key = headers.createCell(1);
        key.setCellValue("Key");

        Cell value = headers.createCell(2);
        value.setCellValue("Value");

        Cell description = headers.createCell(3);
        description.setCellValue("Description");
    }
}
