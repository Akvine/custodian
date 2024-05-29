package ru.akvine.custodian.controllers.rest.dto.app;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AppDto {
    @NotBlank
    private String title;

    private String description;
}
