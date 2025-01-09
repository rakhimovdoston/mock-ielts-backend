package com.search.teacher.service.impl;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.filter.PaginationResponse;
import com.search.teacher.dto.modules.QuestionTypeDto;
import com.search.teacher.dto.modules.ReadingDto;
import com.search.teacher.dto.modules.ReadingPassageDto;
import com.search.teacher.dto.modules.ReadingQuestionResponse;
import com.search.teacher.dto.modules.listening.CheckListeningRequest;
import com.search.teacher.mapper.QuestionTypesMapper;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.modules.reading.QuestionTypes;
import com.search.teacher.model.entities.modules.reading.RMultipleChoice;
import com.search.teacher.model.entities.modules.reading.ReadingPassage;
import com.search.teacher.model.entities.modules.reading.ReadingQuestionTypes;
import com.search.teacher.model.enums.Difficulty;
import com.search.teacher.model.enums.ModuleType;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.OrganizationRepository;
import com.search.teacher.repository.modules.*;
import com.search.teacher.service.JsoupService;
import com.search.teacher.service.modules.ModuleService;
import com.search.teacher.service.organization.OrganizationService;
import com.search.teacher.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Package com.search.teacher.service.impl
 * Created by doston.rakhimov
 * Date: 21/06/24
 * Time: 15:07
 **/
@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final QuestionTypesRepository questionTypesRepository;
    private final QuestionTypesMapper questionTypesMapper;
    private final ListeningQuestionRepository listeningQuestionRepository;
    private final PassageAnswerRepository passageAnswerRepository;
    private final RMultipleChoiceRepository rMultipleChoiceRepository;
    private final ReadingRepository readingRepository;
    private final OrganizationService organizationService;

    @Override
    public JResponse getQuestionTypes(ModuleType type) {
        List<QuestionTypes> questionTypes = questionTypesRepository.findAllByModuleTypeAndActiveIsTrue(type.name());
        var response = questionTypesMapper.questionTypesToQuestionTypeDtoList(questionTypes);
        return JResponse.success(response);
    }

    @Override
    public JResponse checkListening(User currentUser, CheckListeningRequest request) {
        var correctAnswers = listeningQuestionRepository.findCorrectAnswersByTestId(request.getTestId());
        Map<Long, String> correctAnswerMap = correctAnswers.stream()
                .collect(Collectors.toMap(row -> (Long) row[0], row -> (String) row[1]));

        int correctCount = 0;

        for (var userAnswer : request.getAnswers()) {
            Long questionId = userAnswer.getQuestionId();
            boolean isMultiChoice = passageAnswerRepository.isMultipleChoice(request.getTestId(), questionId);

            String correctAnswer = correctAnswerMap.get(questionId);

            if (isMultiChoice) {
                String multipleChoiceCorrectAnswer = rMultipleChoiceRepository.findCorrectAnswerByQuestionId(questionId);
                if (multipleChoiceCorrectAnswer != null && multipleChoiceCorrectAnswer.equalsIgnoreCase(userAnswer.getAnswer())) {
                    correctCount++;
                }
            } else {
                if (correctAnswer != null && correctAnswer.equalsIgnoreCase(userAnswer.getAnswer())) {
                    correctCount++;
                }
            }
        }

        int totalQuestions = correctAnswerMap.size();
        double percentage = totalQuestions > 0 ? ((double) correctCount / totalQuestions) * 100 : 0;

        return JResponse.success(Map.of(
                "correctAnswers", correctCount,
                "totalQuestions", totalQuestions,
                "percentage", percentage
        ));
    }

    @Override
    public JResponse getReading(User currentUser, Difficulty difficulty) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);

        List<ReadingPassage> passages= readingRepository.findAllOrganizationAndDifficulty(organization, difficulty);


        List<ReadingPassageDto> passageDtos = passages.stream()
                .map(passage -> {
                    ReadingPassageDto dto = new ReadingPassageDto();
                    dto.setId(passage.getId());
                    dto.setDifficulty(passage.getDifficulty());
                    dto.setTitle(passage.getTitle());
                    dto.setDescription(passage.getDescription());
                    dto.setContent(passage.getContent());
                    dto.setActive(passage.isActive());

                    dto.setQuestions(passage.toQuestionDto());
                    return dto;
                })
                .collect(Collectors.toList());

        return JResponse.success(passageDtos);
    }




    @Override
    public JResponse getListening(Integer moduleId, Difficulty difficulty) {
        Page<ReadingPassageDto> readingPassages = readingRepository.findAllOrganizationAndDifficulty(null, difficulty,
                PageRequest.of(0, 10));
        return null;
    }


}
