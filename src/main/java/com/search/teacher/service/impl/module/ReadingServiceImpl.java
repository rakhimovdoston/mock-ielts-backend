package com.search.teacher.service.impl.module;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.filter.PaginationResponse;
import com.search.teacher.dto.request.module.*;
import com.search.teacher.dto.response.SaveResponse;
import com.search.teacher.dto.response.module.ListeningResponse;
import com.search.teacher.dto.response.module.ModuleResponse;
import com.search.teacher.dto.response.module.QuestionResponse;
import com.search.teacher.dto.response.module.ReadingPassageResponse;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.mapper.ListeningMapper;
import com.search.teacher.mapper.ModuleAnswerMapper;
import com.search.teacher.mapper.ReadingMapper;
import com.search.teacher.model.entities.*;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.ModuleAnswerRepository;
import com.search.teacher.repository.ModuleQuestionRepository;
import com.search.teacher.repository.ReadingRepository;
import com.search.teacher.service.QuestionTypeService;
import com.search.teacher.service.module.ReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ReadingServiceImpl implements ReadingService {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final ReadingRepository readingRepository;
    private final QuestionTypeService questionTypeService;
    private final ModuleQuestionRepository moduleQuestionRepository;
    private final ModuleAnswerRepository moduleAnswerRepository;

    @Override
    public JResponse createPassage(User currentUser, ReadingPassageRequest passage) {
        Reading reading = new Reading();
        reading.setUser(currentUser);
        reading.setType(passage.type());
        reading.setTitle(passage.title());
        reading.setContent(passage.content());
        readingRepository.save(reading);
        return JResponse.success(new SaveResponse(reading.getId()));
    }

    @Override
    public JResponse updateReadingPassage(User currentUser, Long readingId, ReadingPassageRequest passage) {
        Reading reading = getReadingByUserAndId(currentUser, readingId);
        reading.setType(passage.type());
        reading.setTitle(passage.title());
        reading.setContent(passage.content());
        readingRepository.save(reading);
        return JResponse.success();
    }

    @Override
    public JResponse getPassage(User currentUser, Long readingId) {
        Reading reading = getReadingByUserAndId(currentUser, readingId);
        return JResponse.success(ReadingMapper.INSTANCE.toResponse(reading));
    }

    @Override
    public JResponse getPassageQuestion(User currentUser, Long readingId) {
        Reading reading = getReadingByUserAndId(currentUser, readingId);
        List<ModuleQuestions> questions = reading.getQuestions();
        if (questions.isEmpty()) {
            return JResponse.error(404, "No questions found.");
        }
        List<QuestionResponse> responses = new ArrayList<>();
        List<ModuleAnswer> answers = moduleAnswerRepository.findAllByReading(reading);
        for (ModuleQuestions question : questions) {
            QuestionResponse response = new QuestionResponse();
            response.setId(question.getId());
            response.setType(question.getCategoryName());
            response.setContent(question.getQuestion());
            response.setOrder(question.getOrders());
            responses.add(response);
        }
        ModuleResponse moduleResponse = new ModuleResponse();
        moduleResponse.setQuestions(responses);
        moduleResponse.setAnswers(!answers.isEmpty() ? ModuleAnswerMapper.INSTANCE.toList(answers) : List.of());
        return JResponse.success(moduleResponse);
    }

    @Override
    public JResponse savePassageQuestion(User currentUser, QuestionRequest passage, Long readingId) {
        Reading reading = getReadingByUserAndId(currentUser, readingId);
        List<ModuleQuestions> allQuestions = reading.getQuestions();
        int order = allQuestions.isEmpty() ? 1 : allQuestions.get(allQuestions.size() - 1).getOrders() + 1;
        ModuleQuestions questions = new ModuleQuestions();
        questions.setReading(reading);
        questions.setQuestion(passage.questionContent());
        questions.setOrders(order);
        QuestionTypes questionTypes = questionTypeService.getQuestionType(passage.questionType(), "READING");
        questions.setCategoryName(questionTypes.getName());
        questions.setCategoryType(questionTypes.getType());
        moduleQuestionRepository.save(questions);
        return JResponse.success();
    }

    @Override
    public JResponse saveModuleAnswers(User currentUser, ModuleAnswersRequest answers) {
        Reading reading = getReadingByUserAndId(currentUser, answers.passageId());
        reading.setActive(true);
        readingRepository.save(reading);
        List<ModuleAnswer> answersList = moduleAnswerRepository.findAllByReading(reading);
        if (!answersList.isEmpty()) {
            moduleAnswerRepository.deleteAll(answersList);
        }
        for (ModuleQuestionAnswerRequest answer : answers.answers()) {
            ModuleAnswer moduleAnswer = new ModuleAnswer();
            moduleAnswer.setReading(reading);
            moduleAnswer.setKey(answer.key());
            moduleAnswer.setValue(answer.value());
            moduleAnswerRepository.save(moduleAnswer);
        }
        return JResponse.success();
    }

    @Override
    public JResponse getAllPassage(User currentUser, ModuleFilter filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
        Page<Reading> readings = readingRepository.findAllByDeletedIsFalseAndUserAndTypeIn(currentUser, getType(filter.getType()), pageable);
        if (readings.isEmpty()) {
            return JResponse.error(404, "No readings found.");
        }

        PaginationResponse response = new PaginationResponse();
        response.setCurrentPage(filter.getPage());
        response.setCurrentSize(filter.getSize());
        response.setTotalPages(readings.getTotalPages());
        response.setTotalSizes(readings.getTotalElements());
        List<ReadingPassageResponse> responses = new ArrayList<>();
        for (Reading reading : readings.getContent()) {
            ReadingPassageResponse readingRes = ReadingMapper.INSTANCE.toResponse(reading);
            readingRes.setActive(reading.isActive() && !reading.getQuestions().isEmpty() && !reading.getAnswers().isEmpty());
            String error = reading.getQuestions().isEmpty() ? "Reading is not added questions." : reading.getAnswers().isEmpty() ? "Reading questions answer not entered." : null;
            readingRes.setError(error);
            responses.add(readingRes);
        }
        response.setData(responses);
        return JResponse.success(response);
    }

    @Override
    public int countReadings(User currentUser) {
        return readingRepository.countAllByUser(currentUser);
    }

    @Override
    public JResponse deleteListening(User currentUser, Long listeningId) {
        Reading reading = getReadingByUserAndId(currentUser, listeningId);
        reading.setDeleted(true);
        readingRepository.save(reading);
        return JResponse.success();
    }

    @Override
    public JResponse deleteListeningQuestion(User currentUser, Long readingId, DeleteQuestionRequest request) {
        Reading reading = getReadingByUserAndId(currentUser, readingId);
        List<ModuleQuestions> questions = reading.getQuestions().stream().filter(question -> {
            for (QuestionRequest questionRequest : request.questions()) {
                if (question.getId().equals(questionRequest.id())) {
                    return false;
                }
            }
            return true;
        }).toList();
        moduleQuestionRepository.deleteAll(questions);
        for (QuestionRequest questionRequest : request.questions()) {
            Optional<ModuleQuestions> optional = moduleQuestionRepository.findByIdAndReading(questionRequest.id(), reading);
            ModuleQuestions question = optional.orElseGet(ModuleQuestions::new);
            question.setReading(reading);
            question.setQuestion(questionRequest.questionContent());
            QuestionTypes questionTypes = questionTypeService.getQuestionTypeByName(questionRequest.questionType(), "READING");
            question.setCategoryName(questionTypes.getName());
            question.setCategoryType(questionTypes.getType());
            moduleQuestionRepository.save(question);
        }
        var answers = reading.getAnswers();
        moduleAnswerRepository.deleteAll(answers);
        return JResponse.success();
    }

    @Override
    public JResponse updateListening(User currentUser, Long readingId, DeleteQuestionRequest request) {
        Reading reading = getReadingByUserAndId(currentUser, readingId);
        List<ModuleQuestions> questions = reading.getQuestions();
        for (QuestionRequest questionRequest : request.questions()) {
            ModuleQuestions question = questions
                    .stream()
                    .filter(q -> q.getId().equals(questionRequest.id())).findFirst().orElse(null);
            if (question != null) {
                question.setQuestion(questionRequest.questionContent());
                question.setOrders(questionRequest.order());
                moduleQuestionRepository.save(question);
            }
        }
        var answers = reading.getAnswers();
        moduleAnswerRepository.deleteAll(answers);
        return JResponse.success();
    }

    private List<String> getType(String type) {
        if (type.equals("all")) {
            return List.of("easy", "medium", "hard");
        }
        return List.of(type);
    }

    private Reading getReadingByUserAndId(User currentUser, Long readingId) {
        return readingRepository.findByIdAndUser(readingId, currentUser)
                .orElseThrow(() -> new NotfoundException("Reading not found"));
    }
}
