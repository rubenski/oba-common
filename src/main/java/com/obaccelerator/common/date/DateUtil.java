package com.obaccelerator.common.date;

import java.sql.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateUtil {


    public static final DateTimeFormatter MYSQL_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final ZoneId UTCZoneId = ZoneId.of("UTC");

    public static String rfc1123DateTimeGmtNow() {
        return DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz").withZone(ZoneId.of("GMT")).format(ZonedDateTime.now());
    }

    public static String dateTimeUtcNowIso8601() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
    }

    public static LocalDateTime sqlDateToUtcLocalDateTime(java.sql.Date sqlDate) {
        return sqlDate.toInstant().atZone(DateUtil.UTCZoneId).toLocalDateTime();
    }

    public static String currentDateTimeUtcForMysql() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(MYSQL_DATETIME_FORMATTER);
    }

    public static String offsetDateTimeToMysql(OffsetDateTime time) {
        return time.format(MYSQL_DATETIME_FORMATTER);
    }

    public static java.sql.Date offsetDateTimeToMysqlDate(OffsetDateTime time) {
        return new Date(time.toEpochSecond() * 1000);
    }

    public static java.sql.Date zonedDateTimeToMysqlDate(ZonedDateTime time) {
        return new Date(time.toEpochSecond() * 1000);
    }

    public static String epochMsToUtcForMysql(long ms) {
        Instant instant = Instant.ofEpochMilli(ms);
        ZonedDateTime utc = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
        return utc.format(MYSQL_DATETIME_FORMATTER);
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
        if (mysqlDateTime == null) {
            return null;
        }
        return LocalDateTime.parse(mysqlDateTime, DateUtil.MYSQL_DATETIME_FORMATTER).atOffset(ZoneOffset.UTC);
    }

    public static ZonedDateTime mysqlUtcDateTimeToZonedDateTime(String mysqlDateTime, String timeZone) {
        if (mysqlDateTime == null) {
            return null;
        }
        LocalDateTime parsed = LocalDateTime.parse(mysqlDateTime, DateUtil.MYSQL_DATETIME_FORMATTER);
        return parsed.atZone(ZoneId.of(timeZone));
    }
}
