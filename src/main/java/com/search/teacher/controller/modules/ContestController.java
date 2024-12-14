package com.search.teacher.controller.modules;

import com.search.teacher.dto.filter.DateFilter;
import com.search.teacher.dto.filter.PageFilter;
import com.search.teacher.dto.request.contest.CreateContestRequest;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.ContestService;
import com.search.teacher.utils.DateUtils;
import com.search.teacher.utils.SecurityUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Package com.search.teacher.controller.modules
 * Created by doston.rakhimov
 * Date: 22/10/24
 * Time: 11:48
 **/
@RestController
@RequestMapping("api/v1/contest")
@Api(tags = "Contest API")
@RequiredArgsConstructor
public class ContestController {

    private final ContestService contestService;
    private final SecurityUtils securityUtils;

    @PostMapping("generate")
    public JResponse generateContest(@RequestBody CreateContestRequest request) {
        if (request.getReadingIds().size() != 3)
            return JResponse.error(400, "Please note that there can only be 3 passages in a Reading passage.");

        if (request.getListeningIds().size() != 4)
            return JResponse.error(400, "Please note that there can only be 4 passages in a Reading passage.");

        if (request.getWritingIds().size() != 2)
            return JResponse.error(400, "Please note that there can only be 4 passages in a Reading passage.");

        return contestService.generate(securityUtils.getCurrentUser(), request);
    }

    @GetMapping("/all")
    public JResponse getAllContest(@RequestParam(name = "page", required = false, defaultValue = "0") int page, @RequestParam(name = "size", required = false, defaultValue = "10") int size, @RequestParam(name = "startDate", required = false) String startDate, @RequestParam(name = "endDate", required = false) String endDate) {

        if (startDate == null && endDate == null) {
            var currentDate = new Date();
            startDate = DateUtils.dateToStringForFilter(currentDate, -1);
            endDate = DateUtils.dateToStringForFilter(currentDate, 0);
        }
        if (startDate == null) {
            startDate = DateUtils.dateToStringForFilter(DateUtils.convertStringToDate(endDate), -1);
        }

        if (endDate == null) {
            endDate = DateUtils.dateToStringForFilter(DateUtils.convertStringToDate(startDate), 1);
        }

        DateFilter filter = new DateFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);
        return contestService.getAllExistContest(securityUtils.getCurrentUser(), filter);
    }

    @PutMapping("change-contest/{id}")
    public JResponse changeContest(@PathVariable Long id, @RequestParam(value = "status", defaultValue = "false") boolean status) {
        return contestService.changeStatusOfContest(securityUtils.getCurrentUser(), id, status);
    }

    @GetMapping("byId/{id}")
    public JResponse getContestById(@PathVariable("id") Long id) {
        return contestService.existContestById(securityUtils.getCurrentUser(), id);
    }
}
