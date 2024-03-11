package com.search.teacher.Techlearner.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtils {

    public static Long EXPIRATION_CODE = 120000L;

    public static boolean isExpirationCode(Date date) {

        return (new Date().getTime() - date.getTime()) < EXPIRATION_CODE;
    }

    public static boolean isExpirationToken(Date expirationDate) {
        return expirationDate.after(new Date());
    }

    public static boolean isBetweenIntervals(LocalDateTime nowDate, LocalDateTime updateDate) {
        long minutesDifferences = ChronoUnit.MINUTES.between(nowDate, updateDate);
        return minutesDifferences <= EXPIRATION_CODE;
    }
    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();

        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}
