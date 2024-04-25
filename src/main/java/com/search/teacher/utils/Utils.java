package com.search.teacher.utils;

import com.search.teacher.model.enums.ImageType;

public class Utils {
    public static String getBucketWithType(ImageType imageType) {
        return switch (imageType) {
            case PHOTO -> "photos";
            case CERTIFICATE -> "certificates";
            case PROFILE_PICTURE -> "profiles";
            default -> "images";
        };
    }
}
