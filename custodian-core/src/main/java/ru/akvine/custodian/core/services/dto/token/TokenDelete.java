package ru.akvine.custodian.core.services.dto.token;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class TokenDelete {
    private long clientId;
    private String appTitle;
    private Set<String> tokens;
}
