package com.search.teacher.controller.modules;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.modules.ListeningAnswerDto;
import com.search.teacher.dto.modules.ListeningDto;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.modules.ListeningService;
import com.search.teacher.utils.SecurityUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Package com.search.teacher.controller.modules
 * Created by doston.rakhimov
 * Date: 23/10/24
 * Time: 14:58
 **/
@RestController
@RequestMapping("api/v1/listening")
@Api(tags = "IELTS Listening module API")
@RequiredArgsConstructor
public class ListeningController {

    private final SecurityUtils securityUtils;
    private final ListeningService listeningService;

    @PostMapping("save")
    public JResponse saveListening(@RequestBody ListeningDto dto) {

        return listeningService.saveListening(securityUtils.getCurrentUser(), dto);
    }

    @DeleteMapping("delete/listening/{byId}")
    public JResponse deleteListening(@PathVariable Long byId) {
        return listeningService.deleteListening(securityUtils.getCurrentUser(), byId);
    }

    @PostMapping("instructor/{listeningId}/save")
    public JResponse saveListeningInstructor(@PathVariable Long listeningId, @RequestBody ListeningAnswerDto dto) {
        return listeningService.saveQuestionListening(securityUtils.getCurrentUser(), listeningId, dto);
    }

    @GetMapping("all")
    public JResponse getAllListening(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                     @RequestParam(name = "size", defaultValue = "10", required = false) int size,
                                     @RequestParam(name = "part", defaultValue = "all", required = false) String partName) {
        ModuleFilter filter = new ModuleFilter();
        filter.setType(partName);
        filter.setPage(page);
        filter.setSize(size);
        return listeningService.getAllListening(securityUtils.getCurrentUser(), filter);
    }
}
