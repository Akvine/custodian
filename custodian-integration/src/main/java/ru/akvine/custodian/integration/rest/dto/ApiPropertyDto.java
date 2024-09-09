package ru.akvine.custodian.integration.rest.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ApiPropertyDto {
    private String profile;
    private String description;
    private String key;
    private String value;
}
