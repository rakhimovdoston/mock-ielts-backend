package com.search.teacher.Techlearner.service.impl;

import com.search.teacher.Techlearner.dto.question.*;
import com.search.teacher.Techlearner.dto.response.SaveResponse;
import com.search.teacher.Techlearner.mapper.AnswerMapper;
import com.search.teacher.Techlearner.mapper.QuestionMapper;
import com.search.teacher.Techlearner.model.entities.Answer;
import com.search.teacher.Techlearner.model.entities.Question;
import com.search.teacher.Techlearner.model.entities.QuestionHistory;
import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.repository.AnswerRepository;
import com.search.teacher.Techlearner.repository.QuestionHistoryRepository;
import com.search.teacher.Techlearner.repository.QuestionRepository;
import com.search.teacher.Techlearner.service.excel.ExcelService;
import com.search.teacher.Techlearner.service.question.QuestionDBService;
import com.search.teacher.Techlearner.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionMapper questionMapper;
    private final AnswerMapper answerMapper;
    private final ExcelService excelService;
    private final QuestionDBService questionDBService;
    private final QuestionHistoryRepository questionHistoryRepository;

    @Override
    public List<QuestionDto> getAllQuestion(QuestionSearchFilter questionSearchFilter) {
        return questionDBService.getQuestionsByFilter(questionSearchFilter, true);
    }

    @Override
    public int getCountAllQuestion(QuestionSearchFilter questionSearchFilter) {
        return questionDBService.getCountByFilter(questionSearchFilter, false);
    }

    @Override
    public List<QuestionDto> checkAnswers(User user, AnswerList checker) {
        List<Question> questions = questionRepository.findAllByActiveIsTrueAndIdIn(checker.getQuestions().stream().map(ClientAnswer::getQuestionId).collect(Collectors.toList()));
        List<QuestionDto> questionResponse = questionMapper.toListDto(questions);
        QuestionHistory questionHistory = new QuestionHistory();
        questionHistory.setRequest(checker.getQuestions());
        questionHistory.setUser(user);
        questionHistory.setDate(new Date());
        int count = getCorrectCount(questionResponse, checker.getQuestions());
        questionHistory.setCorrectCount(count);
        questionHistoryRepository.save(questionHistory);
        return questionResponse;
    }

    @Override
    public JResponse saveQuestion(User currentUser, QuestionDto questionDto) {
        Question question = questionMapper.toEntity(questionDto);
        List<Answer> answers = answerMapper.toListEntity(questionDto.getAnswers());
        answerRepository.saveAll(answers);
        question.setActive(true);
        question.setUserId(currentUser.getId());
        question.setAnswers(answers);
        questionRepository.save(question);
        return JResponse.success(new SaveResponse(question.getId()));
    }

    @Override
    public JResponse updateQuestion(User currentUser, QuestionDto questionDto) {
        Question question = questionRepository.findByIdAndActiveIsTrue(questionDto.getId());
        if (question == null)
            return JResponse.error(400, "No such question found");

        List<Answer> answers = answerRepository.findAllByIdInAndQuestion(questionDto.getAnswers().stream().map(AnswerDto::getId).collect(Collectors.toList()), question);
        if (answers.isEmpty()) {
            return JResponse.error(400, "There is no such answer to this question");
        }

        for (AnswerDto answerDto : questionDto.getAnswers()) {
            Answer answer = getAnswers(answers, answerDto.getId());
            if (answer != null) {
                answer.setName(answerDto.getName());
                answerRepository.save(answer);
            }
        }
        question.setName(questionDto.getName());
        questionRepository.save(question);
        return null;
    }

    @Override
    public JResponse uploadQuestions(User currentUser, MultipartFile questionFile) {
        List<Question> questions = excelService.getAllQuestions(questionFile, currentUser);
        if (questions.isEmpty()) {
            return JResponse.error(400, "There are no questions in this excel file");
        }
        questionRepository.saveAll(questions);
        return JResponse.success();
    }

    @Override
    public JResponse deleteQuestion(User currentUser, Long id) {
        Question question = questionRepository.findByIdAndActiveIsTrue(id);
        if (question == null) {
            return JResponse.error(400, "No such question found");
        }
        question.setActive(false);
        questionRepository.save(question);
        return null;
    }

    private int getCorrectCount(List<QuestionDto> questionResponse, List<ClientAnswer> clientAnswerList) {
        int count = 0;
        for (ClientAnswer clientAnswer : clientAnswerList) {
            QuestionDto questionDto = getQuestionAnswer(clientAnswer.getQuestionId(), questionResponse);
            if (questionDto != null && isCorrect(questionDto.getAnswers(), clientAnswer.getAnswerId())) {
                count++;
            }
        }
        return count;
    }

    private boolean isCorrect(List<AnswerDto> answers, Long answerId) {
        for (AnswerDto answer : answers) {
            if (answer.getId().equals(answerId)) {
                answer.setClientAnswer(true);
                if (answer.isCorrect())
                    return true;
            }
        }
        return false;
    }

    private QuestionDto getQuestionAnswer(Long questionId, List<QuestionDto> questions) {
        for (QuestionDto question : questions) {
            if (question.getId().equals(questionId))
                return question;
        }
        return null;
    }

    private Answer getAnswers(List<Answer> answers, Long id) {
        for (Answer answer : answers) {
            if (answer.getId().equals(id))
                return answer;
        }
        return null;
    }
}
