package com.search.teacher.controller.ielts;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.request.module.DeleteQuestionRequest;
import com.search.teacher.dto.request.module.ListeningRequest;
import com.search.teacher.dto.request.module.ModuleAnswersRequest;
import com.search.teacher.dto.request.module.QuestionRequest;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.module.ListeningService;
import com.search.teacher.utils.SecurityUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/listening")
public class ListeningController {

    private final ListeningService listeningService;
    private final SecurityUtils securityUtils;

    public ListeningController(ListeningService listeningService, SecurityUtils securityUtils) {
        this.listeningService = listeningService;
        this.securityUtils = securityUtils;
    }

    @PostMapping("save")
    public JResponse saveListening(@RequestBody ListeningRequest request) {
        return listeningService.createListening(securityUtils.getCurrentUser(), request);
    }

    @GetMapping("get/{id}")
    public JResponse getListening(@PathVariable(name = "id") Long id) {
        return listeningService.getListening(securityUtils.getCurrentUser(), id);
    }

    @PostMapping("/{listeningId}/save/question")
    public JResponse saveListeningQuestion(
            @PathVariable(name = "listeningId") Long listeningId,
            @RequestBody QuestionRequest request) {
        return listeningService.saveListeningQuestion(securityUtils.getCurrentUser(), listeningId, request);
    }

    @GetMapping("/{listeningId}/get")
    public JResponse getListeningQuestions(@PathVariable(name = "listeningId") Long listeningId) {
        return listeningService.getListeningQuestionByListening(securityUtils.getCurrentUser(), listeningId);
    }

    @PutMapping("update/{listeningId}")
    public JResponse updateListening(@PathVariable(name = "listeningId") Long listeningId, @RequestBody ListeningRequest request) {
        return listeningService.updateListeningAudio(securityUtils.getCurrentUser(), listeningId, request);
    }

    @DeleteMapping("delete/{byId}")
    public JResponse deleteListening(@PathVariable(name = "byId") Long listeningId) {
        return listeningService.deleteListening(securityUtils.getCurrentUser(), listeningId);
    }

    @PutMapping("update/{byId}/questions")
    public JResponse updateListeningQuestions(@PathVariable(name = "byId") Long listeningId, @RequestBody DeleteQuestionRequest request) {
        return listeningService.updateListening(securityUtils.getCurrentUser(), listeningId, request);
    }

    @PostMapping("delete/{byId}/questions")
    public JResponse deleteListeningQuestions(@PathVariable(name = "byId") Long listeningId, @RequestBody DeleteQuestionRequest request) {
        return listeningService.deleteListeningQuestion(securityUtils.getCurrentUser(), listeningId, request);
    }

    @PostMapping("answers")
    public JResponse saveListeningAnswers(@RequestBody ModuleAnswersRequest answers) {
        return listeningService.saveModuleAnswers(securityUtils.getCurrentUser(), answers);
    }

    @GetMapping("all")
    public JResponse getAllListening(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "type", required = false, defaultValue = "all") String type
    ) {
        ModuleFilter filter = new ModuleFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setType(type);
        return listeningService.getAllListening(securityUtils.getCurrentUser(), filter);
    }
}
