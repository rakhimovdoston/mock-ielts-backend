package com.search.teacher.service.impl;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.filter.PaginationResponse;
import com.search.teacher.dto.modules.RMultipleChoiceDto;
import com.search.teacher.dto.modules.RQuestionAnswerDto;
import com.search.teacher.dto.modules.ReadingPassageDto;
import com.search.teacher.dto.modules.ReadingResponse;
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
import com.search.teacher.service.modules.ReadingService;
import com.search.teacher.service.organization.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        if (types == ReadingQuestionTypes.MULTIPLE_CHOICE_QUESTIONS) {
            List<RMultipleChoice> choices = question.getChoices();
            rMultipleChoiceRepository.deleteAll(choices);
        }
        readingQuestionRepository.delete(question);
        return JResponse.success();
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
    public JResponse getReadingById(User currentUser, Long id) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);

        ReadingPassage passage = readingRepository.findByIdAndOrganization(id, organization);

        if (passage == null) return JResponse.error(400, "This reading is not for you.");

        return JResponse.success(new ReadingResponse(passage));
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

        if (rAnswer.getContent() != null) {
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
