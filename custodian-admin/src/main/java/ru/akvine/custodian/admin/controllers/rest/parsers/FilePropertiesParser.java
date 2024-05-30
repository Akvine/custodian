package ru.akvine.custodian.admin.controllers.rest.parsers;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class FilePropertiesParser {
    public Map<String, String> parse(MultipartFile file) {
        Map<String, String> properties = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (isEmptyLineOrComment(line)) {
                    continue;
                }
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    properties.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

    private boolean isEmptyLineOrComment(String line) {
       return line.trim().isEmpty() || line.trim().startsWith("###");
    }
}
