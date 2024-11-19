package com.search.teacher.model.entities.modules.reading;

/**
 * Package com.search.teacher.model.entities.modules.reading
 * Created by doston.rakhimov
 * Date: 14/10/24
 * Time: 18:06
 **/
public enum ReadingQuestionTypes {
    MATCHING_HEADINGS("Matching Headings", true),
    LOCATING_INFORMATION("Locating Information", true),
    TRUE_FALSE_NOT_GIVEN("True/False/Not Given", true),
    MULTIPLE_CHOICE_QUESTIONS("Multiple Choice Questions", true),
    SUMMARY_COMPLETION("Summary Completion", true),
    DIAGRAM_LABELLING("Diagram Labelling", true),
    MATCHING_SENTENCE_ENDINGS("Matching Sentence Endings", true),
    MATCHING_FEATURES("Matching Features", true),
    SENTENCE_COMPLETION("Sentence Completion", true),
    SHORT_ANSWER_QUESTIONS("Short Answer Questions", true),
    FLOW_CHART_COMPLETION("Flow Chart Completion", true),
    NOTE_COMPLETION("Note Completion", true),
    YES_NO_NOT_GIVEN("Yes/No/Not Given", true),
    TABLE_COMPLETION("Table Completion", false),
    //    Listening
    MATCHING("Matching", true),
    MULTIPLE_CHOICES("Multiple Choices", true),
    NOTE_COMPLETION_LIS("Note completion", true),
    FORM_COMPLETION("Form Completion", true),
    TABLE_COMPLETION_LIS("Table Completion", true),
    SENTENCE_COMPLETION_LIS("Sentence Completion", true),
    SUMMARY_COMPLETION_LIS("Summary Completion", true),
    SHORT_ANSWER_COMPLETION_LIS("Short Answer Completion", true),
    MAP_PLAN_LABELLING("Map & Plan Labelling", true),
    DIAGRAM_AND_FLOWCHART_COMPLETION("Diagram & flowchart completion", true),
    ;

    public static ReadingQuestionTypes getType(String type) {
        for (var name : ReadingQuestionTypes.values()) {
            if (name.name().equals(type)) {
                return name;
            }
        }
        return MATCHING_HEADINGS;
    }

    public static ReadingQuestionTypes getTypeByName(String type) {
        for (var name : ReadingQuestionTypes.values()) {
            if (name.getDisplayName().equals(type))
                return name;
        }

        return MATCHING_HEADINGS;
    }

    private String displayName;
    private boolean active;

    ReadingQuestionTypes(String displayName, boolean active) {
        this.displayName = displayName;
        this.active = active;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isActive() {
        return active;
    }
}
