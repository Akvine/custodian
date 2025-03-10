package ru.akvine.custodian.integration.rest.meta;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.akvine.common.Response;
import ru.akvine.custodian.integration.rest.dto.ApiPropertyDeleteRequest;
import ru.akvine.custodian.integration.rest.dto.ApiPropertyListRequest;

@RequestMapping(value = "api/properties")
public interface ApiPropertyControllerMeta {
    @PostMapping
    Response list(@RequestHeader("Authorization") String token,
                  @Valid @RequestBody ApiPropertyListRequest request);

    @DeleteMapping
    Response delete(@RequestHeader("Authorization") String token,
                    @Valid @RequestBody ApiPropertyDeleteRequest request);
}
