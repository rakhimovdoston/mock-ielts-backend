package com.search.teacher.service.impl;

import com.search.teacher.dto.modules.RQuestionAnswerDto;
import com.search.teacher.dto.modules.ReadingPassageDto;
import com.search.teacher.dto.modules.ReadingResponse;
import com.search.teacher.dto.response.SaveResponse;
import com.search.teacher.exception.BadRequestException;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.modules.reading.ReadingPassage;
import com.search.teacher.model.entities.modules.reading.ReadingQuestion;
import com.search.teacher.model.entities.modules.reading.ReadingQuestionTypes;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.modules.ReadingQuestionRepository;
import com.search.teacher.repository.modules.ReadingRepository;
import com.search.teacher.service.modules.ReadingService;
import com.search.teacher.service.organization.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        if (rAnswer.getContent() == null || rAnswer.getQuestionList().isEmpty())
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
        ReadingQuestion question = new ReadingQuestion();
        ReadingQuestionTypes type = ReadingQuestionTypes.getType(rAnswer.getType());
        question.setTypes(type);
        question.setInstruction(rAnswer.getInstruction());
        if (rAnswer.getContent() != null) {
            question.setContent(rAnswer.getContent());
            question.setHtml(true);
        }
        if (!rAnswer.getQuestionList().isEmpty()) {
            int count = 0;
            for (var answer : rAnswer.getQuestionList()) {
                if (answer.getAnswer() != null) count++;
            }

            if (count == 0) throw new BadRequestException("Please enter question answers");

            question.setQuestions(rAnswer.getQuestionList());
        }
        question.setPassage(reading);
        readingQuestionRepository.save(question);
        return question;
    }
}
