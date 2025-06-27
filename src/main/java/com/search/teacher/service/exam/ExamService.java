package com.search.teacher.service.exam;

import com.search.teacher.dto.filter.UserFilter;
import com.search.teacher.dto.request.history.EmailAnswerRequest;
import com.search.teacher.dto.request.history.ModuleScoreRequest;
import com.search.teacher.dto.request.history.ScoreRequest;
import com.search.teacher.dto.request.test.TestUserAnswerRequest;
import com.search.teacher.dto.request.test.WritingTestRequest;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;

public interface ExamService {
    JResponse getExam(User currentUser, Long id);

    JResponse setExamsToUser(User currentUser);

    JResponse getExamQuestionByModule(User currentUser, Long id, String type);

    JResponse saveModuleAnswers(User currentUser, Long id, TestUserAnswerRequest request);

    JResponse saveWritingModuleAnswer(User currentUser, Long id, WritingTestRequest request);

    JResponse allExams(User currentUser, UserFilter filter, Long userId);

    JResponse giveScoreModule(User currentUser, ModuleScoreRequest request);

    JResponse getMockExamHistory(User currentUser, Long id, String type);

    JResponse setScoreToUser(User currentUser, Long userId, ScoreRequest request);

    JResponse sendAnswerToEmail(User currentUser, EmailAnswerRequest request);
}
