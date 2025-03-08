package ru.akvine.custodian.admin.controllers.rest.dto.app;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AppUpdateRequest {
    @NotBlank
    private String title;

    private String newTitle;

    private String newDescription;
}
