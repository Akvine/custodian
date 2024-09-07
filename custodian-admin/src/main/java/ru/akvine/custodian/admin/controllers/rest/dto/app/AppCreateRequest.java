package ru.akvine.custodian.admin.controllers.rest.dto.app;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AppCreateRequest {
    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 200)
    private String description;
}
