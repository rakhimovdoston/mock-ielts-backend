package com.search.teacher.service.impl.module;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.filter.PaginationResponse;
import com.search.teacher.dto.request.module.*;
import com.search.teacher.dto.response.SaveResponse;
import com.search.teacher.dto.response.module.ListeningResponse;
import com.search.teacher.dto.response.module.ModuleResponse;
import com.search.teacher.dto.response.module.QuestionResponse;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.mapper.ListeningMapper;
import com.search.teacher.mapper.ModuleAnswerMapper;
import com.search.teacher.model.entities.*;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.ListeningRepository;
import com.search.teacher.repository.ModuleAnswerRepository;
import com.search.teacher.repository.ModuleQuestionRepository;
import com.search.teacher.service.QuestionTypeService;
import com.search.teacher.service.module.ListeningService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListeningServiceImpl implements ListeningService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ListeningRepository listeningRepository;
    private final ModuleQuestionRepository moduleQuestionRepository;
    private final ModuleAnswerRepository moduleAnswerRepository;
    private final QuestionTypeService questionTypeService;

    @Override
    public JResponse createListening(User currentUser, ListeningRequest request) {
        Listening listening = new Listening();
        listening.setUser(currentUser);
        listening.setType(request.type());
        listening.setAudio(request.audio());
        listening.setTitle(request.title());
        listeningRepository.save(listening);
        return JResponse.success(new SaveResponse(listening.getId()));
    }

    @Override
    public JResponse getListening(User currentUser, Long id) {
        Listening listening = getListeningByUserAndId(currentUser, id);
        return JResponse.success(ListeningMapper.INSTANCE.toResponse(listening));
    }

    @Override
    public JResponse getAllListening(User currentUser, ModuleFilter filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
        Page<Listening> listeningResults = listeningRepository.findAllByDeletedIsFalseAndUserAndTypeIn(currentUser, getTypes(filter.getType()), pageable);
        if (listeningResults.isEmpty()) {
            return JResponse.error(404, "No listening found.");
        }

        PaginationResponse response = new PaginationResponse();
        response.setCurrentPage(filter.getPage());
        response.setCurrentSize(filter.getSize());
        response.setTotalPages(listeningResults.getTotalPages());
        response.setTotalSizes(listeningResults.getTotalElements());
        List<ListeningResponse> responses = new ArrayList<>();
        for (Listening listening : listeningResults.getContent()) {
            ListeningResponse listeningRes = ListeningMapper.INSTANCE.toResponse(listening);
            listeningRes.setActive(listening.isActive() && !listening.getQuestions().isEmpty() && !listening.getAnswers().isEmpty());
            String error = listening.getQuestions().isEmpty() ? "Listening is not added questions." : listening.getAnswers().isEmpty() ? "Listening questions answer not entered." : null;
            listeningRes.setError(error);
            responses.add(listeningRes);
        }
        response.setData(responses);
        return JResponse.success(response);
    }

    @Override
    public JResponse saveListeningQuestion(User currentUser, Long listeningId, QuestionRequest request) {
        Listening listening = getListeningByUserAndId(currentUser, listeningId);
        List<ModuleQuestions> allQuestions = listening.getQuestions();
        int order = allQuestions.isEmpty() ? 1 : allQuestions.get(allQuestions.size() - 1).getOrders() + 1;
        ModuleQuestions questions = new ModuleQuestions();
        questions.setListening(listening);
        questions.setOrders(order);
        questions.setQuestion(request.questionContent());
        QuestionTypes questionTypes = questionTypeService.getQuestionType(request.questionType(), "LISTENING");
        questions.setCategoryName(questionTypes.getName());
        questions.setCategoryType(questionTypes.getType());
        moduleQuestionRepository.save(questions);
        return JResponse.success();
    }

    @Override
    public JResponse getListeningQuestionByListening(User currentUser, Long listeningId) {
        Listening listening = getListeningByUserAndId(currentUser, listeningId);
        List<ModuleQuestions> questions = listening.getQuestions();
        if (questions.isEmpty()) {
            return JResponse.error(404, "No questions found.");
        }
        List<QuestionResponse> responses = new ArrayList<>();
        List<ModuleAnswer> answers = moduleAnswerRepository.findAllByListening(listening);
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
    public JResponse saveModuleAnswers(User currentUser, ModuleAnswersRequest answers) {
        Listening listening = getListeningByUserAndId(currentUser, answers.passageId());
        listening.setActive(true);
        listeningRepository.save(listening);
        List<ModuleAnswer> answersList = moduleAnswerRepository.findAllByListening(listening);
        if (!answersList.isEmpty()) {
            moduleAnswerRepository.deleteAll(answersList);
        }
        for (ModuleQuestionAnswerRequest answer : answers.answers()) {
            ModuleAnswer moduleAnswer = new ModuleAnswer();
            moduleAnswer.setListening(listening);
            moduleAnswer.setKey(answer.key());
            moduleAnswer.setValue(answer.value());
            moduleAnswer.setKeys(answer.keys());
            moduleAnswer.setValues(answer.values());
            moduleAnswerRepository.save(moduleAnswer);
        }
        return JResponse.success();
    }

    @Override
    public int countListening(User currentUser) {
        return listeningRepository.countAllByUser(currentUser);
    }

    @Override
    public JResponse deleteListening(User currentUser, Long listeningId) {
        Listening listening = getListeningByUserAndId(currentUser, listeningId);
        listening.setDeleted(true);
        listeningRepository.save(listening);
        return JResponse.success();
    }

    @Override
    public JResponse deleteListeningQuestion(User currentUser, Long listeningId, DeleteQuestionRequest request) {
        Listening listening = getListeningByUserAndId(currentUser, listeningId);
        List<ModuleQuestions> questions = listening.getQuestions();
        moduleQuestionRepository.deleteAll(questions);
        listening.getQuestions().clear();

        for (QuestionRequest questionRequest : request.questions()) {
            ModuleQuestions question = new ModuleQuestions();
            question.setListening(listening);
            question.setQuestion(questionRequest.questionContent());
            QuestionTypes questionTypes = questionTypeService.getQuestionTypeByName(questionRequest.questionType(), "LISTENING");
            question.setCategoryName(questionTypes.getName());
            question.setCategoryType(questionTypes.getType());
            moduleQuestionRepository.save(question);
            listening.getQuestions().add(question);
        }
        var answers = listening.getAnswers();
        moduleAnswerRepository.deleteAll(answers);
        listening.getAnswers().clear();
        listeningRepository.save(listening);
        return JResponse.success();
    }

    @Override
    public JResponse updateListening(User currentUser, Long listeningId, DeleteQuestionRequest request) {
        Listening listening = getListeningByUserAndId(currentUser, listeningId);
        List<ModuleQuestions> questions = listening.getQuestions();
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
        var answers = listening.getAnswers();
        moduleAnswerRepository.deleteAll(answers);
        return JResponse.success();
    }

    private List<String> getTypes(String type) {
        if (type.equals("all")) {
            return List.of("part_1", "part_2", "part_3", "part_4");
        }
        return List.of(type);
    }

    private Listening getListeningByUserAndId(User currentUser, Long id) {
        return listeningRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new NotfoundException("Listening not found."));
    }
}
