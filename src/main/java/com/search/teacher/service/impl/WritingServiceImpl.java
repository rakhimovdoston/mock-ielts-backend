package com.search.teacher.service.impl;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.filter.PaginationResponse;
import com.search.teacher.dto.modules.writing.WritingDto;
import com.search.teacher.exception.BadRequestException;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.modules.writing.WritingModule;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.modules.WritingModuleRepository;
import com.search.teacher.service.modules.WritingService;
import com.search.teacher.service.organization.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Package com.search.teacher.service.impl
 * Created by doston.rakhimov
 * Date: 22/10/24
 * Time: 17:50
 **/
@Service
@RequiredArgsConstructor
public class WritingServiceImpl implements WritingService {

    private final Logger log = LoggerFactory.getLogger(WritingServiceImpl.class);
    private final WritingModuleRepository writingModuleRepository;
    private final OrganizationService organizationService;

    @Override
    public JResponse saveWritingContent(User currentUser, WritingDto writingDto) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);

        if (writingDto.isTaskOne() && writingDto.getImageUrl() == null)
            throw new BadRequestException("Please upload graph of Task 1 essay");

        WritingModule module = new WritingModule();
        module.setOrganization(organization);
        module.setContent(writingDto.getContent());
        module.setImageUrl(writingDto.getImageUrl());
        module.setTaskOne(writingDto.isTaskOne());
        module.setActive(true);
        writingModuleRepository.save(module);
        log.info("Writing Module success added");
        return JResponse.success();
    }

    @Override
    public JResponse updateWriting(User currentUser, WritingDto writingDto) {

        Organization organization = organizationService.getOrganisationByOwner(currentUser);

        WritingModule module = writingModuleRepository.findByIdAndActiveTrueAndOrganization(writingDto.getId(), organization);

        if (module == null)
            throw new NotfoundException("Writing Module not found");

        if (!currentUser.equals(organization.getOwner()))
            throw new BadRequestException("This Writings essay is not for you");

        if (writingDto.isTaskOne() && writingDto.getImageUrl() == null)
            throw new BadRequestException("Please upload graph of Task 1 essay");

        module.setContent(writingDto.getContent());
        module.setImageUrl(writingDto.getImageUrl());
        module.setTaskOne(writingDto.isTaskOne());
        writingModuleRepository.save(module);
        return JResponse.success();
    }

    @Override
    public JResponse getWritingModule(User currentUser, Long id) {

        Organization organization = organizationService.getOrganisationByOwner(currentUser);

        WritingModule module = writingModuleRepository.findByIdAndActiveTrueAndOrganization(id, organization);
        if (module == null)
            throw new NotfoundException("Writing Module not found");

        WritingDto dto = new WritingDto();
        dto.setContent(module.getContent());
        dto.setImageUrl(module.getImageUrl());
        dto.setTaskOne(module.isTaskOne());
        return JResponse.success(dto);
    }

    @Override
    public JResponse deleteWritingModule(User currentUser, Long byId) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);

        WritingModule module = writingModuleRepository.findByIdAndActiveTrueAndOrganization(byId, organization);
        if (module == null)
            throw new NotfoundException("Writing Module not found");

        module.setActive(false);
        module.setDeleteDate(new Date());
        writingModuleRepository.save(module);
        return JResponse.success();
    }

    @Override
    public JResponse getAllWriting(User currentUser, ModuleFilter filter) {
        Organization organization = organizationService.getOrganisationByOwner(currentUser);
        PageRequest request = PageRequest.of(filter.getPage(), filter.getSize());
        Page<WritingModule> pages;
        if (filter.getType().equals("all"))
            pages = writingModuleRepository.findAllByActiveTrueAndOrganization(organization, request);
        else
            pages = writingModuleRepository.findAllByActiveTrueAndOrganizationAndTaskOne(organization, filter.getType().equals("task_1"), request);

        List<WritingModule> modules = pages.getContent();
        if (modules.isEmpty())
            return JResponse.error(404, "Data not found");

        List<WritingDto> writings = new ArrayList<>();
        modules.forEach(module -> writings.add(new WritingDto(module)));

        PaginationResponse response = new PaginationResponse();
        response.setCurrentPage(filter.getPage());
        response.setCurrentSize(filter.getSize());
        response.setTotalPages(pages.getTotalPages());
        response.setTotalSizes(pages.getTotalElements());
        response.setData(writings);
        return JResponse.success(response);
    }
}
