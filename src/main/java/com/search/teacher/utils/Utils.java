package com.search.teacher.utils;

import com.search.teacher.model.enums.Difficulty;
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

    public static String getListeningPassageName(Difficulty difficulty) {
        return switch (difficulty) {
            case semi_easy -> "Listening Passage 1";
            case easy -> "Listening Passage 2";
            case medium -> "Listening Passage 3";
            case hard -> "Listening Passage 4";
            default -> "";
        };
    }

    public static String getReadingPassageName(Difficulty difficulty) {
        return switch (difficulty) {
            case easy -> "Reading Passage 1";
            case medium -> "Reading Passage 2";
            case hard -> "Reading Passage 3";
            default -> "";
        };
    }
}
