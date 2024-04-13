package com.search.teacher.Techlearner.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.search.teacher.Techlearner.dto.QuestionExternal;
import com.search.teacher.Techlearner.dto.question.*;
import com.search.teacher.Techlearner.dto.response.SaveResponse;
import com.search.teacher.Techlearner.mapper.AnswerMapper;
import com.search.teacher.Techlearner.mapper.QuestionMapper;
import com.search.teacher.Techlearner.model.entities.*;
import com.search.teacher.Techlearner.model.enums.Difficulty;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.repository.AnswerRepository;
import com.search.teacher.Techlearner.repository.QCategoryRepository;
import com.search.teacher.Techlearner.repository.QuestionHistoryRepository;
import com.search.teacher.Techlearner.repository.QuestionRepository;
import com.search.teacher.Techlearner.service.excel.ExcelService;
import com.search.teacher.Techlearner.service.http.HttpClientService;
import com.search.teacher.Techlearner.service.question.QuestionDBService;
import com.search.teacher.Techlearner.service.question.QuestionService;
import com.search.teacher.Techlearner.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
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
    private final HttpClientService httpClientService;
    private final QCategoryRepository qCategoryRepository;
    private final ObjectMapper objectMapper;

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
    public JResponse externalQuestionRun() {
        JResponse jResponse = httpClientService.questionFromExternal();
        if (!jResponse.isSuccess())
            return jResponse;
        JsonNode response = (JsonNode) jResponse.getData();
        Integer responseCode = response.has("response_code") ? response.get("response_code").asInt() : -1;
        if (responseCode == -1)
            return JResponse.error(500, "Internal Error");

        ArrayNode arrayNode = (ArrayNode) response.get("results");
        List<QuestionExternal> questionExternals = null;
        try {
            questionExternals = JsonUtils.convertFromJsonToList(arrayNode.toPrettyString(), new TypeReference<List<QuestionExternal>>() {
            });
        } catch (IOException e) {
            logger.error("Converting Error: {}", e.getMessage());
            questionExternals = new ArrayList<>();
        }
        if (questionExternals.isEmpty())
            return JResponse.error(500, "Internal Error");

        for (QuestionExternal questionExternal: questionExternals) {
            Question question = new Question();
            question.setUserId(getRandomNumber());
            question.setName(questionExternal.getQuestion());
            question.setDifficulty(Difficulty.getValue(questionExternal.getDifficulty()));
            question.setAnswers(new ArrayList<>());
            question.setCategory(getQuestionCategory(questionExternal.getCategory()));
            questionRepository.save(question);
            Answer answer = new Answer();
            answer.setCorrect(true);
            answer.setName(questionExternal.getCorrectAnswer());
            answer.setQuestion(question);
            question.getAnswers().add(answer);
            for (String incorrectAnswer: questionExternal.getIncorrectAnswers()) {
                Answer inAnswer = new Answer();
                inAnswer.setCorrect(false);
                inAnswer.setName(incorrectAnswer);
                inAnswer.setQuestion(question);
                question.getAnswers().add(inAnswer);
            }
            questionRepository.save(question);
        }
        return JResponse.success();
    }

    private Long getRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(1, 999999);
        return (long) randomNumber;
    }

    private QCategory getQuestionCategory(String category) {
        QCategory qCategory = qCategoryRepository.findByName(category);
        if (qCategory == null) {
            qCategory = new QCategory();
            qCategory.setName(category);
            qCategoryRepository.save(qCategory);
        }
        return qCategory;
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
