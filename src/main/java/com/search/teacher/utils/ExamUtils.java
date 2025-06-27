package com.search.teacher.utils;

import com.search.teacher.model.entities.MockTestExam;
import com.search.teacher.model.enums.Status;

public class ExamUtils {
    public static long calculateRemainingDuration(long now, long start, long duration) {
        return duration - (now - start);
    }

    public static boolean isExamValid(MockTestExam exam) {
        return exam != null && exam.getStatus().equals(Status.opened.name());
    }
}