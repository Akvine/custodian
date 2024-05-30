package ru.akvine.custodian.admin.controllers.rest.dto.property;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PropertyDto {
    private String profile;
    private String description;
    private String key;
    private String value;
}
