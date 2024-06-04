package ru.akvine.custodian.core.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@UtilityClass
public class DateUtils {
    public long getMinutes(LocalDateTime fromDate, LocalDateTime toDate) {
        return ChronoUnit.MINUTES.between(fromDate, toDate);
    }
}
