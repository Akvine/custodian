package ru.akvine.custodian.admin.controllers.rest.dto.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.commons.util.UUIDGenerator;

/**
 * Successful response status
 */
@Data
@Accessors(chain = true)
public class SuccessfulResponse implements Response {

    /**
     * Request unique identificator <br/>
     *
     * <b>Example</b>: gbhjnkme-rdcfgv-hbjnkm-7689ui-okp3ew
     */
    @NotBlank
    private final String requestId = UUIDGenerator.uuid();

    /**
     * Result response status
     */
    @NotNull
    private final ResponseStatus status = ResponseStatus.SUCCESS;
}