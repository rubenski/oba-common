package com.obaccelerator.common.date;

import java.time.*;
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

    public static LocalDateTime utcLocalDateTimeNow() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }

    public static OffsetDateTime offsetDateTimeNow() {
        return ZonedDateTime.now().toOffsetDateTime();
    }

    public static OffsetDateTime utcOffsetDateTimeNow() {
        return ZonedDateTime.now(ZoneId.of("UTC")).toOffsetDateTime();
    }

    public static OffsetDateTime mysqlUtcDateTimeToOffsetDateTime(String mysqlDateTime) {
        return LocalDateTime.parse(mysqlDateTime, DateUtil.MYSQL_DATETIME_FORMATTER).atOffset(ZoneOffset.UTC);
    }

}
