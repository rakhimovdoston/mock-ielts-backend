package com.search.teacher.service.impl;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.modules.ListeningAnswerDto;
import com.search.teacher.dto.modules.ListeningDto;
import com.search.teacher.exception.BadRequestException;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.modules.listening.ListeningModule;
import com.search.teacher.model.entities.modules.listening.ListeningQuestion;
import com.search.teacher.model.entities.modules.reading.Form;
import com.search.teacher.model.enums.Difficulty;
import com.search.teacher.model.enums.ImageType;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.modules.ListeningModuleRepository;
import com.search.teacher.repository.modules.ListeningQuestionRepository;
import com.search.teacher.service.FileService;
import com.search.teacher.service.modules.ListeningService;
import com.search.teacher.service.organization.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Package com.search.teacher.service.impl
 * Created by doston.rakhimov
 * Date: 23/10/24
 * Time: 15:00
 **/
@Service
@RequiredArgsConstructor
public class ListeningModuleServiceImpl implements ListeningService {
    private final Logger logger = LoggerFactory.getLogger(ListeningModuleServiceImpl.class);
    private final ListeningModuleRepository listeningModuleRepository;
    private final ListeningQuestionRepository listeningQuestionRepository;
    private final OrganizationService organizationService;
    private final FileService fileService;

    @Override
    public JResponse saveListening(User currentUser, ListeningDto dto) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);

        String filename = fileService.checkFilename(dto.getAudio(), ImageType.LISTENING);
        ListeningModule listening = new ListeningModule();
        listening.setDifficulty(Difficulty.getValue(dto.getDifficulty()));
        listening.setAudio(filename);
        listening.setOrganization(organization);
        listening.setTitle(dto.getTitle());
        listeningModuleRepository.save(listening);
        return JResponse.success();
    }

    @Override
    public JResponse saveQuestionListening(User currentUser, Long listeningId, ListeningAnswerDto dto) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);

        ListeningModule listeningModule = Optional.ofNullable(listeningModuleRepository.findByIdAndOrganization(listeningId, organization))
                .orElseThrow(() -> new NotfoundException("Listening audio file not found please upload audio"));

        if (dto.questions().isEmpty()) {
            throw new BadRequestException("Please send listening questions");
        }

        ListeningQuestion question = getListeningQuestion(dto, listeningModule);

        listeningQuestionRepository.save(question);
        return JResponse.success();
    }

    @Override
    public JResponse getAllListening(User currentUser, ModuleFilter filter) {
        return JResponse.success();
    }

    @Override
    public JResponse deleteListening(User currentUser, Long byId) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);

        ListeningModule listeningModule = Optional.ofNullable(listeningModuleRepository.findByIdAndOrganization(byId, organization))
                .orElseThrow(() -> new NotfoundException("Listening audio file not found please upload audio"));

        listeningModule.setActive(false);
        listeningModuleRepository.save(listeningModule);
        return JResponse.success();
    }

    @Override
    public JResponse getListeningById(User currentUser, Long id) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);
        ListeningModule listeningModule = listeningModuleRepository.findByIdAndOrganization(id, organization);
        return null;
    }

    @NotNull
    private static ListeningQuestion getListeningQuestion(ListeningAnswerDto dto, ListeningModule listeningModule) {
        ListeningQuestion question = new ListeningQuestion();
        question.setListening(listeningModule);
        question.setHtml(dto.content() != null);
        question.setContent(dto.content());
        question.setInstruction(dto.instruction());

        question.setQustions(dto.questions());
        List<Form> answers = new ArrayList<>();
        for (Form form : dto.questions()) {
            if (form.getAnswer() == null) {
                throw new BadRequestException("Please Enter question answers: " + form.getText());
            }
            Form newForm = new Form();
            newForm.setAnswer(form.getAnswer());
            newForm.setOrder(form.getOrder());
            answers.add(newForm);
        }
        question.setAnswers(answers);
        return question;
    }
}
