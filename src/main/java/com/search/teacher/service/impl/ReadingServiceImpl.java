package com.search.teacher.service.impl;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.filter.PaginationResponse;
import com.search.teacher.dto.modules.*;
import com.search.teacher.dto.response.SaveResponse;
import com.search.teacher.exception.BadRequestException;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.modules.reading.*;
import com.search.teacher.model.enums.Difficulty;
import com.search.teacher.model.enums.ModuleType;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.modules.QuestionTypesRepository;
import com.search.teacher.repository.modules.RMultipleChoiceRepository;
import com.search.teacher.repository.modules.ReadingQuestionRepository;
import com.search.teacher.repository.modules.ReadingRepository;
import com.search.teacher.service.JsoupService;
import com.search.teacher.service.modules.ReadingService;
import com.search.teacher.service.organization.OrganizationService;
import com.search.teacher.utils.StringUtils;
import com.search.teacher.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Package com.search.teacher.service.impl
 * Created by doston.rakhimov
 * Date: 14/10/24
 * Time: 18:20
 **/
@Service
@RequiredArgsConstructor
public class ReadingServiceImpl implements ReadingService {

    private final ReadingRepository readingRepository;
    private final ReadingQuestionRepository readingQuestionRepository;
    private final OrganizationService organizationService;
    private final QuestionTypesRepository questionTypesRepository;
    private final RMultipleChoiceRepository rMultipleChoiceRepository;
    private final JsoupService jsoupService;

    @Override
    public JResponse createPassage(User currentUser, ReadingPassageDto passage) {

        Organization organization = organizationService.getOrganisationByOwner(currentUser);

        ReadingPassage readingPassage = new ReadingPassage();
        readingPassage.setTitle(passage.getTitle());
        readingPassage.setDescription(passage.getDescription());
        readingPassage.setOrganization(organization);
        readingPassage.setList(passage.isWithList());
        readingPassage.setDifficulty(passage.getDifficulty());
        readingPassage.setPassages(passage.getPassages());
        readingPassage.setContent(passage.getContent());
        readingPassage.setCount(passage.getPassages().size());
        readingRepository.save(readingPassage);
        return JResponse.success(new SaveResponse(readingPassage.getId()));
    }

    @Override
    public JResponse updatePassage(User currentUser, ReadingPassageDto passage) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);

        ReadingPassage readingPassage = readingRepository.findByIdAndOrganization(passage.getId(), organization);

        if (readingPassage == null) return JResponse.error(400, "This reading is not for you.");

        readingPassage.setTitle(passage.getTitle());
        readingPassage.setDescription(passage.getDescription());
        readingPassage.setList(passage.isWithList());
        readingPassage.setPassages(passage.getPassages());
        readingPassage.setContent(passage.getContent());
        readingPassage.setCount(passage.getPassages().size());
        readingRepository.save(readingPassage);
        return JResponse.success(new SaveResponse(readingPassage.getId()));
    }

    @Override
    public JResponse deleteReadingPassage(User currentUser, Long readingId) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);

        ReadingPassage readingPassage = readingRepository.findByIdAndOrganization(readingId, organization);

        if (readingPassage == null) return JResponse.error(400, "This reading is not for you.");

//        readingPassage.setActive(false);
//        readingPassage.setDeleteDate(new Date());
//        readingRepository.save(readingPassage);
        readingRepository.delete(readingPassage);
        return JResponse.success();
    }

    @Override
    public JResponse deleteReadingAnswer(User currentUser, Long passageId, Long questionId, String type) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);

        ReadingPassage readingPassage = readingRepository.findByIdAndOrganization(passageId, organization);

        if (readingPassage == null) return JResponse.error(400, "This reading is not for you.");

        ReadingQuestionTypes types = ReadingQuestionTypes.getTypeByName(type);
        var question = readingQuestionRepository.findByIdAndPassage(questionId, readingPassage);
        if (question == null) return JResponse.error(400, "This reading question is not for you.");

        setQuestionNewOrder(question, readingPassage.getQuestions());

        if (question.getTypes() == ReadingQuestionTypes.MULTIPLE_CHOICE_QUESTIONS) {
            List<RMultipleChoice> choices = question.getChoices();
            rMultipleChoiceRepository.deleteAll(choices);
        }
        readingQuestionRepository.delete(question);
        return JResponse.success();
    }

    private void setQuestionNewOrder(ReadingQuestion deletedQuestion, List<ReadingQuestion> questions) {
        int startQuestion = Utils.getStartUpdatedQuestion(deletedQuestion);

        questions.sort(Comparator.comparing(ReadingQuestion::getSort));
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
                String newContent = jsoupService.setOrderForHtmlContent(startQuestion, question.getContent());
                question.setContent(newContent);
            }

            List<Form> forms = question.getQuestions();
            for (Form form : forms) {
                form.setOrder(startQuestion);
                startQuestion++;
            }
            question.setQuestions(forms);
            readingQuestionRepository.save(question);
        }
    }

    private List<ReadingQuestion> getWithoutDeleteQuestions(int startQuestion, List<ReadingQuestion> questions, ReadingQuestion deletedQuestion) {
        List<ReadingQuestion> newQuestions = new ArrayList<>();
        for (ReadingQuestion question : questions) {
            var oldQuestion = Utils.getStartUpdatedQuestion(question);
            if (oldQuestion > startQuestion && !question.equals(deletedQuestion)) {
                newQuestions.add(question);
            }
        }

        return newQuestions;
    }

    @Override
    public JResponse getAllReadingPassage(User currentUser, ModuleFilter moduleFilter) {
        PageRequest pageRequest = PageRequest.of(moduleFilter.getPage(), moduleFilter.getSize());
        Organization organization = organizationService.getOrganisationByOwner(currentUser);
        Difficulty difficulty = Difficulty.getValue(moduleFilter.getType());
        Page<ReadingPassageDto> passageContents;
        if (moduleFilter.getType().equals("all"))
            passageContents = readingRepository.findAllOrganizationAndActiveTrue(organization, pageRequest);
        else
            passageContents = readingRepository.findAllOrganizationAndActiveTrueAndDifficulty(organization, difficulty, pageRequest);

        List<ReadingPassageDto> passages = passageContents.getContent();
        if (passages.isEmpty()) return JResponse.error(404, "Reading Passage not found.");

        PaginationResponse response = new PaginationResponse();
        response.setData(passages);
        response.setTotalPages(passageContents.getTotalPages());
        response.setTotalSizes(passageContents.getTotalElements());
        response.setCurrentSize(moduleFilter.getSize());
        response.setCurrentPage(moduleFilter.getPage());
        return JResponse.success(response);
    }

    @Override
    public JResponse getPassageQuestion(User currentUser, Long passageId) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);

        ReadingPassage passage = readingRepository.findByIdAndOrganization(passageId, organization);

        if (passage == null) return JResponse.error(400, "This reading is not for you.");

        return JResponse.success(passage.toQuestionDto());
    }

    @Override
    public JResponse getReadingById(User currentUser, Long id, boolean withAnswer) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);

        ReadingPassage passage = readingRepository.findByIdAndOrganization(id, organization);

        if (passage == null) return JResponse.error(400, "This reading is not for you.");

        return JResponse.success(new ReadingResponse(passage, withAnswer));
    }

    @Override
    public JResponse saveReadingAnswer(User currentUser, Long questionId, RQuestionAnswerDto rAnswer) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);

        ReadingPassage reading = readingRepository.findByIdAndOrganization(questionId, organization);

        if (reading == null) return JResponse.error(400, "This reading passage is not for you.");

        if (rAnswer.getContent() == null && rAnswer.getQuestionList().isEmpty() && rAnswer.getChoices().isEmpty())
            return JResponse.error(400, "This reading passage is empty.");

        ReadingQuestion answers = saveAnswer(reading, rAnswer);
        reading.getQuestions().add(answers);
        readingRepository.save(reading);
        return JResponse.success(new SaveResponse(reading.getId()));
    }

    @Override
    public JResponse updateReadingAnswer(User currentUser, Long readingId, RQuestionAnswerDto answer) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);

        ReadingPassage reading = readingRepository.findByIdAndOrganization(readingId, organization);

        if (reading == null) return JResponse.error(400, "This reading passage is not for you.");

        ReadingQuestion question = readingQuestionRepository.findByIdAndPassage(answer.getId(), reading);

        if (question == null) return JResponse.error(400, "This reading answer is not for you.");

        question.setContent(answer.getContent());
        question.setInstruction(answer.getInstruction());
        question.setHtml(answer.getContent() != null);
        question.setQuestions(answer.getQuestionList());
        question.setTypes(ReadingQuestionTypes.getType(answer.getType()));
        readingQuestionRepository.save(question);

        return JResponse.success(new SaveResponse(reading.getId()));
    }

    private ReadingQuestion saveAnswer(ReadingPassage reading, RQuestionAnswerDto rAnswer) {

        QuestionTypes questionTypes = questionTypesRepository.findByActiveTrueAndModuleTypeAndName(ModuleType.READING.name(), rAnswer.getType());
        if (questionTypes == null) {
            throw new NotfoundException("Question type not found.");
        }

        ReadingQuestion question = new ReadingQuestion();
        ReadingQuestionTypes type = ReadingQuestionTypes.getType(questionTypes.getType());
        question.setTypes(type);
        int sort = reading.getQuestions().stream().map(ReadingQuestion::getSort).max(Integer::compareTo).orElse(0);
        question.setSort(sort + 1);
        question.setInstruction(rAnswer.getInstruction());

        if (!StringUtils.isNullOrEmpty(rAnswer.getContent())) {
            question.setContent(rAnswer.getContent());
            question.setHtml(true);
        }

        if (!rAnswer.getQuestionList().isEmpty()) {
            question.setQuestions(rAnswer.getQuestionList());
        }
        question.setPassage(reading);
        readingQuestionRepository.save(question);
        if (questionTypes.getType().equals(ReadingQuestionTypes.MULTIPLE_CHOICE_QUESTIONS.name())) {
            if (rAnswer.getChoices().isEmpty()) {
                throw new BadRequestException("Please enter multiple choice question");
            }
            question.setChoices(choice(rAnswer.getChoices(), question));
        }
        readingQuestionRepository.save(question);
        return question;
    }

    private List<RMultipleChoice> choice(List<RMultipleChoiceDto> choices, ReadingQuestion question) {
        List<RMultipleChoice> list = new ArrayList<>();
        choices.forEach(choice -> {
            RMultipleChoice newChoice = new RMultipleChoice();
            newChoice.setName(choice.getName());
            newChoice.setSort(choice.getOrder());
            newChoice.setCorrectAnswer(choice.getCorrectAnswer());
            newChoice.setChoices(choice.getAnswers());
            newChoice.setQuestion(question);
            list.add(newChoice);
        });
        return list;
    }
}
