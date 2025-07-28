package com.search.teacher.service.exam;

import com.search.teacher.dto.filter.UserFilter;
import com.search.teacher.dto.request.history.EmailAnswerRequest;
import com.search.teacher.dto.request.history.ModuleScoreRequest;
import com.search.teacher.dto.request.history.ScoreRequest;
import com.search.teacher.dto.request.module.AICheckerRequest;
import com.search.teacher.dto.request.test.TestUserAnswerRequest;
import com.search.teacher.dto.request.test.WritingTestRequest;
import com.search.teacher.dto.response.history.MockExamResponse;
import com.search.teacher.model.entities.MockTestExam;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ExamService {
    JResponse getExam(User currentUser, String id);

    JResponse setExamsToUser(User currentUser);

    JResponse getExamQuestionByModule(User currentUser, String id, String type);

    JResponse saveModuleAnswers(User currentUser, String id, TestUserAnswerRequest request);

    JResponse saveWritingModuleAnswer(User currentUser, String id, WritingTestRequest request);

    JResponse allExams(User currentUser, UserFilter filter, Long userId);

    JResponse giveScoreModule(User currentUser, ModuleScoreRequest request);

    JResponse getMockExamHistory(User currentUser, Long id, String type);

    JResponse setScoreToUser(User currentUser, Long userId, ScoreRequest request);

    JResponse sendAnswerToEmail(User currentUser, EmailAnswerRequest request);

    JResponse checkWriting(User currentUser, AICheckerRequest request);

    List<MockExamResponse> getAllMockExams(List<MockTestExam> exams);

    JResponse refreshUserAnswer(User currentUser, EmailAnswerRequest request);

    ResponseEntity<byte[]> downloadPdfFile(String id);
}
