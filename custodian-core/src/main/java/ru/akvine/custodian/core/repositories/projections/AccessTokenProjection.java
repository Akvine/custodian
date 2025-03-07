package ru.akvine.custodian.core.repositories.projections;

import java.time.ZonedDateTime;

public record AccessTokenProjection(String appTitle, String token, ZonedDateTime expiredAt) {
}
