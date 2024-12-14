package com.search.teacher.service;

import com.search.teacher.dto.filter.DateFilter;
import com.search.teacher.dto.filter.PageFilter;
import com.search.teacher.dto.filter.PaginationResponse;
import com.search.teacher.dto.request.contest.CreateContestRequest;
import com.search.teacher.dto.response.ContestResponse;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.mapper.ContestMapper;
import com.search.teacher.model.entities.Contest;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.modules.reading.ReadingPassage;
import com.search.teacher.model.enums.Difficulty;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.ContestRepository;
import com.search.teacher.repository.modules.ListeningModuleRepository;
import com.search.teacher.repository.modules.ReadingRepository;
import com.search.teacher.repository.modules.WritingModuleRepository;
import com.search.teacher.service.organization.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Package com.search.teacher.service
 * Created by doston.rakhimov
 * Date: 23/10/24
 * Time: 12:24
 **/
@Service
@RequiredArgsConstructor
public class ContestService {
    private final Logger logger = LoggerFactory.getLogger(ContestService.class);
    private final ContestRepository contestRepository;
    private final ReadingRepository readingRepository;
    private final ListeningModuleRepository listeningModuleRepository;
    private final WritingModuleRepository writingModuleRepository;
    private final OrganizationService organizationService;

    public JResponse generate(User currentUser, CreateContestRequest request) {

        var readingIds = request.getReadingIds();
        Organization organization = organizationService.getOrganisationByOwner(currentUser);
        var difficulties = readingRepository.findDifficultiesByIdInAndOrganization(readingIds, organization);
        if (difficulties.size() != 3) {
            return JResponse.error(400, "There should be 3 different levels of difficulty in the reading passage. Please place them correctly.");
        }

        difficulties = listeningModuleRepository.findDifficultyByIdAndOrganization(request.getListeningIds(), organization);
        if (difficulties.size() != 4) {
            return JResponse.error(400, "There should be 4 different levels of difficulty in the listening module. Please place them correctly.");
        }

        var writings = writingModuleRepository.findAllByActiveIsTrueAndIdInAndOrganization(request.getWritingIds(), organization);
        if (writings.get(0).isTaskOne() == writings.get(1).isTaskOne()) {
            return JResponse.error(400, "The Writing module should have task 1 and task 2. Please place them correctly.");
        }

        Contest contest = new Contest();
        contest.setActive(true);
        contest.setName(request.getName());
        contest.setDescription(request.getDescription());
        contest.setStartDate(new Date());
        contest.setReadingPassageIds(readingIds);
        contest.setWritingPassageIds(request.getWritingIds());
        contest.setListeningPassageIds(request.getListeningIds());
        contestRepository.save(contest);
        return JResponse.success();
    }

    public JResponse getAllExistContest(User currentUser, DateFilter filter) {
        PageRequest request = PageRequest.of(filter.getPage(), filter.getSize());
        var contests = contestRepository.findAllByUser(currentUser, request);

        if (contests.isEmpty()) {
            throw new NotfoundException("No contests found.");
        }

        PaginationResponse response = new PaginationResponse();
        response.setData(ContestMapper.INSTANCE.contestsToContestResponses(contests.getContent()));
        response.setCurrentSize(filter.getSize());
        response.setCurrentPage(filter.getPage());
        response.setTotalSizes(contests.getTotalElements());
        response.setTotalPages(contests.getTotalPages());
        return JResponse.success(response);
    }

    public JResponse existContestById(User currentUser, Long id) {
        Contest contest = contestRepository.findByIdAndUser(id, currentUser).orElseThrow(() -> new NotfoundException("Contest not found."));

        ContestResponse response = ContestMapper.INSTANCE.contestToContestResponse(contest);
        response.setReadingPassages(null);
        response.setListeningModules(null);
        response.setWritings(null);
        return JResponse.success(response);
    }

    public JResponse changeStatusOfContest(User currentUser, Long id, boolean status) {
        Contest contest = contestRepository.findByIdAndUser(id, currentUser).orElseThrow(() -> new NotfoundException("Contest not found."));
        contest.setActive(status);
        contestRepository.save(contest);
        return JResponse.success();
    }
}
