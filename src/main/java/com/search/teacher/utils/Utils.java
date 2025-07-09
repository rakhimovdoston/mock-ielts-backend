package com.search.teacher.utils;


import com.search.teacher.model.entities.ExamScore;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.enums.Difficulty;
import com.search.teacher.model.enums.ImageType;
import com.search.teacher.model.enums.PaymentType;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class Utils {

    public final static String STANDARD_FORMAT = "yyyy-mm-dd";
    public final static String[] IMAGE_TYPES = {"image/jpeg", "image/png", "image/jpg"};
    public final static String[] COMPRESSED_AUDIO_TYPES = {"audio/mpeg", "audio/aac", "audio/aac", "audio/x-ms-wma", "audio/mpeg"};
    public final static String[] UNCOMPRESSED_AUDIO_TYPES = {"audio/wav", "audio/x-wav", "audio/aiff", "audio/x-aiff"};
    public final static int MIN_VALUE = Integer.MIN_VALUE;
    public final static int MAX_VALUE = Integer.MAX_VALUE;

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

    private static double roundToNearestHalfBand(double score) {
        double floor = Math.floor(score);
        double decimal = score - floor;

        if (decimal < 0.25) {
            return floor; // 7.1 → 7.0
        } else if (decimal < 0.75) {
            return floor + 0.5; // 7.4 → 7.5
        } else {
            return floor + 1.0; // 7.8 → 8.0
        }
    }

    public static boolean isSubscriptionActive(User user) {
        if (user.getPaymentType() == null) {
            return false;
        }

        if (user.getPaymentType().equals(PaymentType.lifetime.name())) {
            return false;
        }

        if (user.getPaymentType().equals(PaymentType.per_test.name())) {
            return user.getSubscriptionExpireDate().after(new Date());
        }

        if (user.getPaymentType().equals(PaymentType.subscription.name())) {
            return user.getSubscriptionExpireDate().after(new Date());
        }
        return false;
    }

}
