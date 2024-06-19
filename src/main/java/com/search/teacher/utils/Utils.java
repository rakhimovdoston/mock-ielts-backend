package com.search.teacher.utils;

import com.search.teacher.model.enums.ImageType;

import java.util.Random;

public class Utils {

    public final static String[] COLORS = {"#2196F3", "#32c787", "#00BCD4", "#ff5652", "#ffc107", "#ff85af", "#FF9800", "#39bbb0"};

    public static String getRandomProfileColor(String[] colors) {
        Random random = new Random();
        int randomNumber = random.nextInt(colors.length);
        if (randomNumber == colors.length) {
            randomNumber = random.nextInt(0, colors.length - 1);
        }
        return colors[randomNumber];
    }


    public static String getBucketWithType(ImageType imageType) {
        return switch (imageType) {
            case PHOTO -> "photos";
            case CERTIFICATE -> "certificates";
            case PROFILE_PICTURE -> "profiles";
            case LOGO -> "logo";
            default -> "images";
        };
    }
}
