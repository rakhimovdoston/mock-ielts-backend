package com.search.teacher.utils;

import com.search.teacher.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

@Slf4j
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

    public static Date convertStringToDate(String stringDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.parse(stringDate);
        } catch (ParseException exception) {
            log.error("Date Parse error: {}", exception.getMessage());
            throw new BadRequestException("Please check Date Format, We accept 'yyyy-MM-dd' format");
        }
    }

    public static String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static String dateToStringForFilter(Date currentDate, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.MONTH, month);
        return dateToString(calendar.getTime());
    }
}
