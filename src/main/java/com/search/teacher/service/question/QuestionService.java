package com.search.teacher.service.question;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.search.teacher.dto.question.AnswerList;
import com.search.teacher.dto.question.CheckQuestionResponse;
import com.search.teacher.dto.question.QuestionDto;
import com.search.teacher.dto.question.QuestionSearchFilter;
import com.search.teacher.model.entities.QCategory;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface QuestionService {
    List<QuestionDto> getAllQuestion(User currentUser, QuestionSearchFilter questionSearchFilter);

    JResponse saveQuestion(User currentUser, QuestionDto questionDto);

    JResponse updateQuestion(User user, QuestionDto questionDto);

    JResponse deleteQuestion(User currentUser, Long id);

    JResponse uploadQuestions(User currentUser, MultipartFile questionFile);

    int getCountAllQuestion(QuestionSearchFilter questionSearchFilter);

    CheckQuestionResponse checkAnswers(User user, AnswerList checker);

    JResponse externalQuestionRun();

    List<QCategory> getCategories();

    JResponse getQuestionHistories(User user, Date beginDate, Date endDate);

    JResponse questionHistoryByRequestId(User user, String requestId);
}
