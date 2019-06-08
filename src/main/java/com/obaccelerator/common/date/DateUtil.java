package com.obaccelerator.common.date;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateUtil {


    public static final DateTimeFormatter MYSQL_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static String dateTimeUtcNowIso8601() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
    }

    public static String currentDateTimeUtcForMysql() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(MYSQL_DATETIME_FORMATTER);
    }

    public static LocalDateTime utcLocalDateTime() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }

}
