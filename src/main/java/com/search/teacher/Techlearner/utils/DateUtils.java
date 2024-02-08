package com.search.teacher.Techlearner.utils;

import java.util.Date;

public class DateUtils {

    public static Long EXPIRATION_CODE = 120000L;

    public static boolean isExpirationCode(Date date) {
        long nowDate = new Date().getTime();
        long createdDate = date.getTime();
        return (nowDate - createdDate) < EXPIRATION_CODE;
    }

    public static boolean isExpirationToken(Date expirationDate) {
        return expirationDate.after(new Date());
    }
}
