package ru.akvine.custodian.integration.rest.meta;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.common.Response;
import ru.akvine.custodian.integration.rest.dto.ApiPropertyListRequest;

@RequestMapping(value = "api/properties")
public interface ApiPropertyControllerMeta {
    @PostMapping
    Response list(@RequestHeader("authorization") String token,
                  @Valid @RequestBody ApiPropertyListRequest request);
}
