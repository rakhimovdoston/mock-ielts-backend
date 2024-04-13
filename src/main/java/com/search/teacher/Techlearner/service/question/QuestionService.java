package com.search.teacher.Techlearner.service.question;

import com.search.teacher.Techlearner.dto.question.AnswerList;
import com.search.teacher.Techlearner.dto.question.QuestionDto;
import com.search.teacher.Techlearner.dto.question.QuestionSearchFilter;
import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.model.response.JResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuestionService {
    List<QuestionDto> getAllQuestion(QuestionSearchFilter questionSearchFilter);

    JResponse saveQuestion(User currentUser, QuestionDto questionDto);

    JResponse updateQuestion(User user, QuestionDto questionDto);

    JResponse deleteQuestion(User currentUser, Long id);

    JResponse uploadQuestions(User currentUser, MultipartFile questionFile);

    int getCountAllQuestion(QuestionSearchFilter questionSearchFilter);

    List<QuestionDto> checkAnswers(User user, AnswerList checker);

    JResponse externalQuestionRun();
}
