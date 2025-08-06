package com.search.teacher.utils;


import com.search.teacher.model.entities.ExamScore;
import com.search.teacher.model.enums.Difficulty;
import com.search.teacher.model.enums.ImageType;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

public class Utils {

    public final static String[] COLORS = {"#2196F3", "#32c787", "#00BCD4", "#ff5652", "#ffc107", "#ff85af", "#FF9800", "#39bbb0"};
    public final static String STANDARD_FORMAT = "yyyy-mm-dd";
    public final static String[] IMAGE_TYPES = {"image/jpeg", "image/png", "image/jpg"};
    public final static String[] COMPRESSED_AUDIO_TYPES = {"audio/mpeg", "audio/aac", "audio/aac", "audio/x-ms-wma", "audio/mpeg"};
    public final static String[] UNCOMPRESSED_AUDIO_TYPES = {"audio/wav", "audio/x-wav", "audio/aiff", "audio/x-aiff"};
    public final static int MIN_VALUE = Integer.MIN_VALUE;
    public final static int MAX_VALUE = Integer.MAX_VALUE;

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

    public static List<String> getHeadingList(int count) {
        List<String> headingList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            char letter = (char) (i + 64);
            headingList.add(String.valueOf(letter));
        }
        return headingList;
    }

    public static String getCurrentDateStandardFormat() {
        SimpleDateFormat format = new SimpleDateFormat(STANDARD_FORMAT);
        return format.format(new Date());
    }

    public static boolean isImage(String contentType) {
        if (contentType == null) {
            return false;
        }
        for (String imageType : IMAGE_TYPES) {
            if (contentType.equals(imageType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAudio(String contentType) {
        if (contentType == null) {
            return false;
        }
        if (contentType.startsWith("audio/")) {
            return true;
        }
        List<String> audioFormats = Stream.concat(Arrays.stream(COMPRESSED_AUDIO_TYPES), Arrays.stream(UNCOMPRESSED_AUDIO_TYPES)).toList();
        for (String audioFormat : audioFormats) {
            if (contentType.toLowerCase().equals(audioFormat)) return true;
        }

        return false;
    }

    public static String countOverall(ExamScore examScore) {
        double writing = Double.parseDouble(examScore.getWriting());
        double listening = Double.parseDouble(examScore.getListening());
        double reading = Double.parseDouble(examScore.getReading());
        double speaking = Double.parseDouble(examScore.getSpeaking());
        double total = (writing + listening + reading + speaking) / 4.0;
        double rounded = roundToNearestHalfBand(total);
        return String.valueOf(rounded);
    }

    public static double roundToNearestHalfBand(double score) {
        double floor = Math.floor(score);
        double decimal = score - floor;

        if (decimal < 0.25) {
            return floor; // 7.1 → 7.0
        } else if (decimal < 0.75) {
            return floor + 0.5;
        } else {
            return floor + 1.0; // 7.8 → 8.0
        }
    }

    public static double roundToNearestWriting(double score) {
        double floor = Math.floor(score);
        double decimal = score - floor;
        if (decimal < 0.5) {
            return floor;
        } else if (decimal >= 0.5 &&  decimal < 1.0) {
            return floor + 0.5;
        }
        return floor;
    }
}
