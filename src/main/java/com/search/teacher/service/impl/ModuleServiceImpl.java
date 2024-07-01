package com.search.teacher.service.impl;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.filter.PaginationResponse;
import com.search.teacher.dto.modules.ReadingDto;
import com.search.teacher.dto.response.SaveResponse;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.modules.QuestionTypes;
import com.search.teacher.model.entities.modules.reading.ReadingPassage;
import com.search.teacher.model.enums.ModuleType;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.OrganizationRepository;
import com.search.teacher.repository.modules.QuestionTypesRepository;
import com.search.teacher.repository.modules.ReadingRepository;
import com.search.teacher.repository.modules.SpeakingRepository;
import com.search.teacher.repository.modules.WritingRepository;
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

    private final SpeakingRepository speakingRepository;
    private final ReadingRepository readingRepository;
    private final WritingRepository writingRepository;
    private final OrganizationRepository organizationRepository;
    private final QuestionTypesRepository questionTypesRepository;

    @Override
    public JResponse saveReading(User user, ReadingDto dto) {
        ReadingPassage reading = new ReadingPassage();
        readingRepository.save(reading);
        return JResponse.success(new SaveResponse(reading.getId()));
    }

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
    public JResponse getReadingById(User currentUser, Long id) {
        ReadingPassage passage = readingRepository.findById(id).orElseThrow(() -> new NotfoundException("Reading not found this id: " + id));

        Organization organization = organizationRepository.findByOwner(currentUser);
        if (organization == null) return JResponse.error(400, "This reading is not for you.");

        if (!passage.getOrganization().getId().equals(organization.getId()))
            return JResponse.error(400, "This reading is not for you.");

        return JResponse.success();
    }

    @Override
    public JResponse getQuestionTypes(String type) {
        ModuleType moduleType = ModuleType.getModuleType(type);
        List<QuestionTypes> questionTypes = questionTypesRepository.findAllByModuleType(moduleType);
        return JResponse.success(questionTypes);
    }
}
