package com.search.teacher.service.impl;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.filter.PaginationResponse;
import com.search.teacher.dto.modules.ReadingDto;
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

    private final ReadingRepository readingRepository;
    private final OrganizationRepository organizationRepository;
    private final QuestionTypesRepository questionTypesRepository;

    @Override
    public JResponse getAllModules(User currentUser, ModuleFilter filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
        Page<ReadingPassage> readings = readingRepository.findAll(pageable);
        if (readings.getContent().isEmpty()) return JResponse.error(404, "Data Not Found");

        List<ReadingDto> dtos = new ArrayList<>();
        for (ReadingPassage reading : readings.getContent()) {
            ReadingDto dto = new ReadingDto();
            dto.setId(reading.getId());
            dto.setTitle(reading.getTitle());
            dto.setPassage(Utils.getReadingPassageName(reading.getDifficulty()));
            dtos.add(dto);
        }

        PaginationResponse response = new PaginationResponse();
        response.setTotalSizes(readings.getTotalElements());
        response.setTotalPages(readings.getTotalPages());
        response.setCurrentSize(readings.getSize());
        response.setCurrentPage(readings.getNumber());
        response.setData(dtos);
        return JResponse.success(response);
    }

    @Override
    public JResponse getQuestionTypes(ModuleType type) {
        List<QuestionTypes> questionTypes = questionTypesRepository.findAllByModuleTypeAndActiveIsTrue(type);
        return JResponse.success(questionTypes);
    }
}
