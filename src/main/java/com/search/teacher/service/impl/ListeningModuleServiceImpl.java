package com.search.teacher.service.impl;

import com.search.teacher.dto.ImageDto;
import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.filter.PaginationResponse;
import com.search.teacher.dto.modules.ListeningAnswerDto;
import com.search.teacher.dto.modules.ListeningDto;
import com.search.teacher.dto.modules.PassageConfirmDto;
import com.search.teacher.dto.modules.ReadingQuestionResponse;
import com.search.teacher.dto.modules.listening.FileDto;
import com.search.teacher.dto.modules.listening.ListeningResponse;
import com.search.teacher.dto.modules.listening.ListeningUpdateRequest;
import com.search.teacher.dto.modules.listening.MultipleQuestionSecondDto;
import com.search.teacher.dto.response.SaveResponse;
import com.search.teacher.exception.BadRequestException;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.mapper.PassageAnswerMapper;
import com.search.teacher.model.entities.Image;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.modules.listening.ListeningModule;
import com.search.teacher.model.entities.modules.listening.ListeningQuestion;
import com.search.teacher.model.entities.modules.reading.*;
import com.search.teacher.model.enums.Difficulty;
import com.search.teacher.model.enums.ImageType;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.modules.*;
import com.search.teacher.service.FileService;
import com.search.teacher.service.JsoupService;
import com.search.teacher.service.modules.ListeningService;
import com.search.teacher.service.organization.OrganizationService;
import com.search.teacher.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.unique.CreateTableUniqueDelegate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

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
    private final RMultipleChoiceRepository rMultipleChoiceRepository;
    private final FileService fileService;
    private final MatchingSentenceRepository matchingSentenceRepository;
    private final JsoupService jsoupService;
    private final PassageAnswerRepository passageAnswerRepository;

    @Override
    public JResponse saveQuestionListening(User currentUser, Long listeningId, ListeningAnswerDto dto) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);

        ListeningModule listeningModule = Optional.ofNullable(listeningModuleRepository.findByIdAndOrganization(listeningId, organization)).orElseThrow(() -> new NotfoundException("Listening audio file not found please upload audio"));

        getListeningQuestion(dto, listeningModule);
        return JResponse.success();
    }

    @Override
    public JResponse getAllListening(User currentUser, ModuleFilter filter) {
        PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize());
        Organization organization = organizationService.getOrganisationByOwner(currentUser);
        Difficulty difficulty = Difficulty.getValue(filter.getType());
        Page<ListeningModule> page;
        if (filter.getType().equals("all"))
            page = listeningModuleRepository.findAllByOrganization(organization, pageRequest);
        else page = listeningModuleRepository.findAllByOrganizationAndDifficulty(organization, difficulty, pageRequest);

        if (page.getContent().isEmpty()) return JResponse.error(404, "Listening Module not found.");

        PaginationResponse response = getPaginationResponse(filter, page);
        return JResponse.success(response);
    }

    @NotNull
    private PaginationResponse getPaginationResponse(ModuleFilter filter, Page<ListeningModule> page) {
        PaginationResponse response = new PaginationResponse();
        response.setTotalPages(page.getTotalPages());
        response.setTotalSizes(page.getTotalElements());
        response.setCurrentSize(filter.getSize());
        response.setCurrentPage(filter.getPage());
        List<ListeningDto> list = new ArrayList<>();
        for (ListeningModule listeningModule : page.getContent()) {
            ListeningDto listening = new ListeningDto();
            listening.setId(listeningModule.getId());
            listening.setTitle(listeningModule.getTitle());
            listening.setActive(listeningModule.isActive());
            listening.setAnswers(passageAnswerRepository.existsAllByListening(listeningModule));
            listening.setAudio(listeningModule.getAudio().getUrl());
            listening.setTypes(listeningModule.getQuestions().stream().map(ques -> ques.getTypes().getDisplayName()).toList());
            listening.setDifficulty(listeningModule.getDifficulty().name());
            list.add(listening);
        }
        response.setData(list);
        return response;
    }

    @Override
    public JResponse deleteListening(User currentUser, Long byId) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);

        ListeningModule listeningModule = Optional.ofNullable(listeningModuleRepository.findByIdAndOrganization(byId, organization)).orElseThrow(() -> new NotfoundException("Listening module not found"));

        if (!listeningModule.getQuestions().isEmpty())
            listeningQuestionRepository.deleteAll(listeningModule.getQuestions());

        Image image = listeningModule.getAudio();
        listeningModuleRepository.delete(listeningModule);
        fileService.removeAudio(currentUser.getId(), image);
        return JResponse.success();
    }

    @Override
    public JResponse getListeningById(User currentUser, Long id) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);
        ListeningModule listening = listeningModuleRepository.findByIdAndOrganization(id, organization);
        if (listening == null) {
            return JResponse.error(404, "Audio File Not Found");
        }
        ListeningResponse response = listening.toResponse();
        response.setAnswerStart(!listening.getQuestions().isEmpty() ? Utils.countAnswerStart(listening.getQuestions()) : 0);
        response.setQuestion(getListeningQuestion(listening));
        return JResponse.success(response);
    }

    private List<ReadingQuestionResponse> getListeningQuestion(ListeningModule listening) {
        List<ReadingQuestionResponse> responses = new ArrayList<>();

        for (var question : listening.getQuestions()) {
            ReadingQuestionResponse response = new ReadingQuestionResponse();
            response.setId(question.getId());
            response.setText(question.getContent());
            response.setTypes(question.getTypes().getDisplayName());
            response.setCount(Utils.getCountString(question));

            if (question.getTypes().equals(ReadingQuestionTypes.MULTIPLE_CHOICES_SECONDS)) {
                response.setCount(question.getQuestionCount());
            }

            response.setCondition(JsoupService.replaceInstruction(question.getInstruction(), response.getCount()));
            if (question.getQuestions() != null)
                response.setQuestions(question.getQuestions().stream().peek(form -> form.setAnswer(null)).toList());
            if (question.getTypes().equals(ReadingQuestionTypes.MULTIPLE_CHOICE_QUESTIONS))
                response.setChoices(question.getChoices().stream().map(RMultipleChoice::toDto).toList());

            responses.add(response);
        }
        return responses;
    }

    public ListeningResponse createListening(Organization organization, Image image, FileDto fileDto) {
        Difficulty difficulty = Difficulty.getValue(fileDto.getListeningPart());
        ListeningModule listeningModule = null;
        if (fileDto.getListeningId() != null)
            listeningModule = listeningModuleRepository.findByIdAndOrganization(Long.valueOf(fileDto.getListeningId()), organization);

        if (listeningModule == null) listeningModule = new ListeningModule();
        else fileService.removeAudio(listeningModule.getAudio().getUserId(), listeningModule.getAudio());

        listeningModule.setDifficulty(difficulty);
        listeningModule.setAudio(image);
        listeningModule.setOrganization(organization);
        listeningModuleRepository.save(listeningModule);
        return listeningModule.toResponse();
    }

    @Override
    public JResponse uploadAudio(User currentUser, FileDto fileDto) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);
        Image image = fileService.audioUpload(currentUser, fileDto.getFile());
        ListeningResponse response = createListening(organization, image, fileDto);
        return JResponse.success(new SaveResponse(response.getId()));
    }

    @Override
    public JResponse deleteListeningQuestion(User currentUser, Long listeningId, Long questionId, String type) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);
        ListeningModule listening = listeningModuleRepository.findByIdAndOrganization(listeningId, organization);
        if (listening == null) return JResponse.error(400, "This Listening is not for you.");

        ListeningQuestion question = listeningQuestionRepository.findByIdAndListening(questionId, listening);
        if (question == null) return JResponse.error(400, "This listening question is not for you.");

        setQuestionNewOrder(question, listening.getQuestions());
        if (question.getTypes() == ReadingQuestionTypes.MULTIPLE_CHOICE_QUESTIONS) {
            List<RMultipleChoice> choices = question.getChoices();
            rMultipleChoiceRepository.deleteAll(choices);
        }

        listeningQuestionRepository.delete(question);
        return JResponse.success();
    }

    @Override
    public JResponse confirmListening(User currentUser, PassageConfirmDto confirmDto) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);
        ListeningModule listeningModule = listeningModuleRepository.findByIdAndOrganization(confirmDto.getPassageId(), organization);
        if (listeningModule == null) return JResponse.error(400, "This Listening is not for you.");

        listeningModule.setActive(true);
        String title = getListeningTitle(listeningModule.getQuestions());
        listeningModule.setTitle(title);
        for (var confirm : confirmDto.getAnswers()) {
            PassageAnswer answer = new PassageAnswer();
            answer.setListening(listeningModule);
            answer.setQuestionId(confirm.getId());
            answer.setAnswer(confirm.getText());
            answer.setAnswers(confirm.getAnswers());
            answer.setQuestionIds(confirm.getCount());
            passageAnswerRepository.save(answer);
        }
        listeningModuleRepository.save(listeningModule);
        return JResponse.success();
    }

    private String getListeningTitle(List<ListeningQuestion> questions) {
        return questions.isEmpty() ? "You have not questions" :
            JsoupService.getTitleFromCondition(questions.get(0).getInstruction().isEmpty() || questions.get(0).getInstruction() == null ?
                questions.get(0).getContent() : questions.get(0).getInstruction());
    }

    @Override
    public JResponse getAnswerListening(User currentUser, Long byId) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);
        ListeningModule listening = listeningModuleRepository.findByIdAndOrganization(byId, organization);
        if (listening == null) return JResponse.error(404, "This Listening is not for you.");
        List<PassageAnswer> answers = passageAnswerRepository.findAllByListening(listening);
        if (answers.isEmpty()) return JResponse.error(404, "This Listening answer is not found.");
        return JResponse.success(PassageAnswerMapper.INSTANCE.toListDto(answers));
    }

    @Override
    public JResponse updateListening(User currentUser, ListeningUpdateRequest request) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);
        ListeningModule listeningModule = listeningModuleRepository.findByIdAndOrganization(request.getPassageId(), organization);
        if (listeningModule == null) return JResponse.error(404, "This Listening is not for you.");

        String title = getListeningTitle(listeningModule.getQuestions());
        listeningModule.setTitle(title);
        listeningModule.setDifficulty(Difficulty.getValue(request.getListeningPart()));
        listeningModuleRepository.save(listeningModule);
        List<PassageAnswer> answers = passageAnswerRepository.findAllByListening(listeningModule);
        for (var answer : answers) {
            for (var requestAnswer : request.getAnswers()) {
                if (answer.getQuestionId() != null) {
                    if (answer.getQuestionId().equals(requestAnswer.getId())) {
                        answer.setAnswer(requestAnswer.getText());
                        break;
                    }
                } else if (answer.getQuestionIds().equals(requestAnswer.getCount())) {
                    answer.setAnswers(requestAnswer.getAnswers());
                }
            }
            passageAnswerRepository.save(answer);
        }
        return JResponse.success();
    }

    private void setQuestionNewOrder(ListeningQuestion deletedQuestion, List<ListeningQuestion> questions) {
        int startQuestion = Utils.getStartUpdatedQuestionForListening(deletedQuestion);

        questions.sort(Comparator.comparing(ListeningQuestion::getSort));
        questions = getWithoutDeleteQuestions(startQuestion, questions, deletedQuestion);
        for (var question : questions) {
            if (question.getTypes() == ReadingQuestionTypes.MULTIPLE_CHOICE_QUESTIONS) {
                List<RMultipleChoice> choices = question.getChoices();
                for (var choice : question.getChoices()) {
                    choice.setSort(startQuestion);
                    startQuestion++;
                }
                rMultipleChoiceRepository.saveAll(choices);
                continue;
            }


            if (question.getContent() != null && question.isHtml()) {
                MultipleQuestionSecondDto dto = jsoupService.setOrderForHtmlContent(startQuestion, question.getContent());
                question.setContent(dto.getConditions());
                question.setQuestionCount(question.getQuestionCount());
            }

            List<Form> forms = question.getQuestions();
            for (Form form : forms) {
                form.setOrder(startQuestion);
                startQuestion++;
            }
            question.setQuestions(forms);
            listeningQuestionRepository.save(question);
        }
    }

    private List<ListeningQuestion> getWithoutDeleteQuestions(int startQuestion, List<ListeningQuestion> questions, ListeningQuestion deletedQuestion) {
        List<ListeningQuestion> newQuestions = new ArrayList<>();
        for (ListeningQuestion question : questions) {
            var oldQuestion = Utils.getStartUpdatedQuestionForListening(question);
            if (oldQuestion > startQuestion && !question.equals(deletedQuestion)) {
                newQuestions.add(question);
            }
        }

        return newQuestions;
    }

    @NotNull
    private ListeningQuestion getListeningQuestion(ListeningAnswerDto dto, ListeningModule listeningModule) {
        ListeningQuestion question = new ListeningQuestion();
        question.setListening(listeningModule);
        int startQuestion = !listeningModule.getQuestions().isEmpty() ? Utils.countAnswerStart(listeningModule.getQuestions()) : 0;
        if (dto.content() != null && !dto.content().isEmpty()) {

            if (JsoupService.checkListeningQuestion(dto.content(), startQuestion))
                throw new BadRequestException("Each Listening section should not have more than 10 questions. Please reduce your questions.");

            question.setContent(dto.content());
            question.setHtml(true);
        }

        if (dto.type().equals(ReadingQuestionTypes.MAP_PLAN_LABELLING.getDisplayName()) || dto.type().equals(ReadingQuestionTypes.MATCHING.getDisplayName())) {
            question.setContent(JsoupService.replaceQuestionList(dto.content(), startQuestion));
        }


        if (dto.type().equals(ReadingQuestionTypes.MULTIPLE_CHOICES.getDisplayName()) && startQuestion + dto.choices().size() > 10) {
            throw new BadRequestException("Each Listening section should not have more than 10 questions. Please reduce your questions.");
        }
        question.setQuestions(dto.questions() == null ? new ArrayList<>() : dto.questions());
        question.setInstruction(dto.instruction());
        question.setTypes(ReadingQuestionTypes.getTypeByName(dto.type()));
        listeningQuestionRepository.save(question);

        if (dto.type().equals(ReadingQuestionTypes.MULTIPLE_CHOICES.getDisplayName())) {
            dto.choices().forEach(ques -> {
                RMultipleChoice choice = new RMultipleChoice();
                choice.setListening(question);
                choice.setName(ques.getName());
                choice.setChoices(ques.getAnswers());
                choice.setSort(ques.getOrder());
                choice.setCorrectAnswer(ques.getCorrectAnswer());
                rMultipleChoiceRepository.save(choice);
                question.getChoices().add(choice);
            });
            question.setInstruction(JsoupService.replaceInstruction(question.getInstruction(), dto.choices()));
        }
        listeningQuestionRepository.save(question);

        question.setQuestionCount(Utils.getCountString(question));

        if (dto.type().equals(ReadingQuestionTypes.MULTIPLE_CHOICES_SECONDS.getDisplayName())) {
            question.setContent(null);
            question.setHtml(false);
            MultipleQuestionSecondDto questionSecondDto = JsoupService.replaceMultipleChoice(dto.content(), startQuestion);
            question.setInstruction(questionSecondDto.getConditions() != null ? questionSecondDto.getConditions() : dto.instruction());
            question.setQuestions(questionSecondDto.getForms());
            question.setQuestionCount(questionSecondDto.getQuestionCount());
        }

        question.setSort(listeningModule.getQuestions().isEmpty() ? 1 : listeningModule.getQuestions().get(listeningModule.getQuestions().size() - 1).getSort() + 1);

        listeningQuestionRepository.save(question);
        return question;
    }
}
