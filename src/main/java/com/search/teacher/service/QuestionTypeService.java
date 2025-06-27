package com.search.teacher.service;

import com.search.teacher.exception.NotfoundException;
import com.search.teacher.model.entities.QuestionTypes;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.QuestionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionTypeService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final QuestionTypeRepository questionTypeRepository;

    public JResponse getAllTypes(String type) {
        List<QuestionTypes> questionTypes = questionTypeRepository.findAllByModuleTypeAndActiveIsTrue(type);
        if (questionTypes.isEmpty()) {
            logger.error("No question types found.");
            return JResponse.error(404, "No question types found.");
        }
        return JResponse.success(questionTypes);
    }

    public QuestionTypes getQuestionType(String questionType, String moduleType) {
        QuestionTypes questionTypes = questionTypeRepository.findByTypeAndModuleType(questionType, moduleType);
        if (questionTypes == null) {
            logger.error("Question type not found.");
            throw new NotfoundException("Question type not found.");
        }
        return questionTypes;
    }

    public QuestionTypes getQuestionTypeByName(String questionType, String moduleType) {
        QuestionTypes questionTypes = questionTypeRepository.findByNameAndModuleType(questionType, moduleType);
        if (questionTypes == null) {
            logger.error("Question type not found.");
            throw new NotfoundException("Question type not found.");
        }
        return questionTypes;
    }
}
