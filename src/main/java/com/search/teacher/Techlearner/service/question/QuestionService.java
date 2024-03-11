package com.search.teacher.Techlearner.service.question;

import com.search.teacher.Techlearner.dto.question.QuestionDto;
import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.model.response.JResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuestionService {
    List<QuestionDto> getAllQuestion();

    JResponse saveQuestion(User currentUser, QuestionDto questionDto);

    JResponse updateQuestion(User user, QuestionDto questionDto);

    JResponse deleteQuestion(User currentUser, Long id);

    JResponse uploadQuestions(User currentUser, MultipartFile questionFile);
}
