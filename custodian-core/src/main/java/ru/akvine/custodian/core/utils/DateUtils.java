package ru.akvine.custodian.core.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@UtilityClass
public class DateUtils {
    private static final DateTimeFormatter DATE_TIME_FORMATTER_DEFAULT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public long getMinutes(LocalDateTime fromDate, LocalDateTime toDate) {
        return ChronoUnit.MINUTES.between(fromDate, toDate);
    }

    public ZonedDateTime convertToZonedDateTime(@NotNull String date) {
        return convertToZonedDateTime(date, DATE_TIME_FORMATTER_DEFAULT);
    }

    public ZonedDateTime convertToZonedDateTime(@NotNull String date, DateTimeFormatter dateTimeFormatter) {
        if (StringUtils.isBlank(date)) {
            throw new IllegalArgumentException("date can't be blank!");
        }
        return ZonedDateTime.from(dateTimeFormatter.parse(date));
    }
}
