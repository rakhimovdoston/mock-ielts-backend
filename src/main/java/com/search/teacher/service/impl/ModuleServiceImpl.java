package com.search.teacher.service.impl;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.filter.PaginationResponse;
import com.search.teacher.dto.modules.QuestionTypeDto;
import com.search.teacher.dto.modules.ReadingDto;
import com.search.teacher.mapper.QuestionTypesMapper;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.modules.reading.QuestionTypes;
import com.search.teacher.model.entities.modules.reading.ReadingPassage;
import com.search.teacher.model.enums.ModuleType;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.OrganizationRepository;
import com.search.teacher.repository.modules.QuestionTypesRepository;
import com.search.teacher.repository.modules.ReadingRepository;
import com.search.teacher.service.modules.ModuleService;
import com.search.teacher.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public JResponse getQuestionTypes(ModuleType type) {
        List<QuestionTypes> questionTypes = questionTypesRepository.findAllByModuleTypeAndActiveIsTrue(type.name());
        var response = questionTypesMapper.questionTypesToQuestionTypeDtoList(questionTypes);
        return JResponse.success(response);
    }
}
