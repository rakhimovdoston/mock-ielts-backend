package com.search.teacher.model.enums;

public enum ImageType {
    CERTIFICATE,
    PHOTO,
    LOGO,
    PROFILE_PICTURE,
    READING,
    LISTENING,
    WRITING,
    AUDIO,
    SPEAKING;

    public static ImageType getValue(String type) {
        for (ImageType imageType : ImageType.values()) {
            if (imageType.toString().equals(type)) {
                return imageType;
            }
        }
        return ImageType.PHOTO;
    }
}
