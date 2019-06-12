package com.obaccelerator.common.date;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {


    public static final DateTimeFormatter MYSQL_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final ZoneId UTCZoneId = ZoneId.of("UTC");


    public static String dateTimeUtcNowIso8601() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
    }

    public static LocalDateTime sqlDateToUtcLocalDateTime(java.sql.Date sqlDate) {
        return sqlDate.toInstant().atZone(DateUtil.UTCZoneId).toLocalDateTime();
    }

    public static String currentDateTimeUtcForMysql() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(MYSQL_DATETIME_FORMATTER);
    }

    public static LocalDateTime utcLocalDateTime() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }

}
