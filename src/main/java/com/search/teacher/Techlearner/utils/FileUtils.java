package com.search.teacher.Techlearner.utils;

public class FileUtils {
    public static final String[] PHOTO_CONTENTS = {"JPG", "JPEG", "PNG", "GIF"};
    public static final String[] VIDEO_CONTENTS = {"MP$", "AVI"};
    public static final String[] PDF_CONTENTS = {"PDF"};

    public static String errorMessage(String message, String[] photoContents) {
        StringBuilder errorMessage = new StringBuilder(message);
        for (String content : photoContents) {
            errorMessage.append(" ").append(content);
        }
        return errorMessage.toString().replace("  ", " ");
    }

    public static boolean isCheckContent(String content, String[] contents) {
        for (String cont : contents) {
            if (content.toUpperCase().contains(cont.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
