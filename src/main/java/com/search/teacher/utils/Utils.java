package com.search.teacher.utils;

import com.search.teacher.dto.modules.ReadingQuestionResponse;
import com.search.teacher.model.entities.modules.listening.ListeningModule;
import com.search.teacher.model.entities.modules.listening.ListeningQuestion;
import com.search.teacher.model.entities.modules.reading.*;
import com.search.teacher.model.enums.Difficulty;
import com.search.teacher.model.enums.ImageType;
import com.search.teacher.service.JsoupService;
import org.jetbrains.annotations.NotNull;

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

    public static String getCountString(ReadingQuestion question) {
        return getString(question.getTypes(), question.getChoices(), question.getMatching(), question.isHtml(), question.getContent(), question.getQuestions());
    }

    public static int getLastQuestionsNumber(List<ReadingQuestion> questions) {
        questions.sort(Comparator.comparing(ReadingQuestion::getSort));
        ReadingQuestion question = questions.get(questions.size() - 1);
        return getLastQuestionNumber(question);
    }

    public static int getLastQuestionNumber(ReadingQuestion question) {
        if (question.getTypes() == ReadingQuestionTypes.MULTIPLE_CHOICE_QUESTIONS) {
            return question.getChoices().stream().map(RMultipleChoice::getSort).max(Integer::compareTo).orElse(1);
        }

        if (question.isHtml() && question.getContent() != null) {
            return JsoupService.getLastQuestionNumberFromHtml(question.getContent());
        }

        if (ReadingQuestionTypes.isMatchingSentenceOrFeatures(question.getTypes().getDisplayName())) {
            return question.getMatching().getSentence().stream().map(Form::getOrder).max(Integer::compareTo).orElse(1);
        }

        return question.getQuestions().stream().map(Form::getOrder).max(Integer::compareTo).orElse(1);
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

    public static int getStartUpdatedQuestion(ReadingQuestion question) {
        if (question.getTypes() == ReadingQuestionTypes.MULTIPLE_CHOICE_QUESTIONS) {
            return question.getChoices().stream().map(RMultipleChoice::getSort).min(Integer::compareTo).orElse(1);
        }

        if (question.isHtml() && !StringUtils.isNullOrEmpty(question.getContent())) {
            return JsoupService.getStartQuestionNumber(question.getContent());
        }

        if (ReadingQuestionTypes.isMatchingSentenceOrFeatures(question.getTypes().getDisplayName()))
            return question.getMatching().getSentence().stream().map(Form::getOrder).min(Integer::compareTo).orElse(1);

        return question.getQuestions().stream().map(Form::getOrder).min(Integer::compareTo).orElse(1);
    }

    public static int getStartUpdatedQuestionForListening(ListeningQuestion question) {
        if (question.getTypes() == ReadingQuestionTypes.MULTIPLE_CHOICE_QUESTIONS) {
            return question.getChoices().stream().map(RMultipleChoice::getSort).min(Integer::compareTo).orElse(1);
        }

        if (question.isHtml() && !StringUtils.isNullOrEmpty(question.getContent())) {
            return JsoupService.getStartQuestionNumber(question.getContent());
        }

        if (ReadingQuestionTypes.isMatchingSentenceOrFeatures(question.getTypes().getDisplayName()))
            return question.getMatching().getSentence().stream().map(Form::getOrder).min(Integer::compareTo).orElse(1);

        return question.getQuestions().stream().map(Form::getOrder).min(Integer::compareTo).orElse(1);
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
            if (contentType.toLowerCase().equals(audioFormat))
                return true;
        }

        return false;
    }

    public static int countAnswerStart(List<ListeningQuestion> questions) {
        questions.sort(Comparator.comparing(ListeningQuestion::getSort));
        ListeningQuestion question = questions.get(questions.size() - 1);
        return getLastQuestionLastQuestionOrder(question);
    }

    public static int getLastQuestionLastQuestionOrder(ListeningQuestion question) {
        if (question.getTypes() == ReadingQuestionTypes.MULTIPLE_CHOICE_QUESTIONS) {
            return question.getChoices().stream().map(RMultipleChoice::getSort).max(Integer::compareTo).orElse(1);
        }

        if (question.isHtml() && question.getContent() != null) {
            return JsoupService.getLastQuestionNumberFromHtml(question.getContent());
        }

        if (ReadingQuestionTypes.isMatchingSentenceOrFeatures(question.getTypes().getDisplayName())) {
            return question.getMatching().getSentence().stream().map(Form::getOrder).max(Integer::compareTo).orElse(1);
        }

        return question.getQuestions().stream().map(Form::getOrder).max(Integer::compareTo).orElse(1);
    }

    public static String getCountString(ListeningQuestion question) {
        return getString(
            question.getTypes(),
            question.getChoices(),
            question.getMatching(),
            question.isHtml(),
            question.getContent(),
            question.getQuestions());

    }

    @NotNull
    private static String getString(ReadingQuestionTypes types, List<RMultipleChoice> choices, MatchingSentence matching, boolean html, String content, List<Form> questions) {
        int max, min;
        if (types == ReadingQuestionTypes.MULTIPLE_CHOICE_QUESTIONS) {
            min = choices.stream().map(RMultipleChoice::getSort).min(Integer::compareTo).orElse(1);
            max = choices.stream().map(RMultipleChoice::getSort).max(Integer::compareTo).orElse(1);

            return min + "-" + max;
        }

        if (ReadingQuestionTypes.isMatchingSentenceOrFeatures(types.getDisplayName())) {
            min = matching.getSentence().stream().map(Form::getOrder).min(Integer::compareTo).orElse(1);
            max = matching.getSentence().stream().map(Form::getOrder).max(Integer::compareTo).orElse(1);
            return min + "-" + max;
        }

        if (html && content != null) {
            return JsoupService.questionCountString(content);
        }
        min = questions.stream().map(Form::getOrder).min(Integer::compareTo).orElse(1);
        max = questions.stream().map(Form::getOrder).max(Integer::compareTo).orElse(1);
        return min + "-" + max;
    }
}
