package com.search.teacher.Techlearner.utils;

import com.search.teacher.Techlearner.model.enums.ImageType;

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
