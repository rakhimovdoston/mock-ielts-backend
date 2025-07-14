package com.search.teacher.controller.ielts;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.request.module.DeleteQuestionRequest;
import com.search.teacher.dto.request.module.ModuleAnswersRequest;
import com.search.teacher.dto.request.module.QuestionRequest;
import com.search.teacher.dto.request.module.ReadingPassageRequest;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.module.ReadingService;
import com.search.teacher.utils.SecurityUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/reading")
public class ReadingController {

    private final ReadingService readingService;
    private final SecurityUtils securityUtils;

    public ReadingController(ReadingService readingService, SecurityUtils securityUtils) {
        this.readingService = readingService;
        this.securityUtils = securityUtils;
    }

    @PostMapping("create/passage")
    public JResponse createPassage(@RequestBody ReadingPassageRequest passage) {
        return readingService.createPassage(securityUtils.getCurrentUser(), passage);
    }

    @PutMapping("update/{byId}")
    public JResponse updateReading(@PathVariable(name = "byId") Long readingId, @RequestBody ReadingPassageRequest passage) {
        return readingService.updateReadingPassage(securityUtils.getCurrentUser(), readingId, passage);
    }

    @PostMapping("passage/{reading}/save/question")
    public JResponse saveQuestion(
            @PathVariable(name = "reading") Long readingId,
            @RequestBody QuestionRequest passage) {
        return readingService.savePassageQuestion(securityUtils.getCurrentUser(), passage, readingId);
    }

    @PutMapping("update/{byId}/questions")
    public JResponse updateListeningQuestions(@PathVariable(name = "byId") Long readingId, @RequestBody DeleteQuestionRequest request) {
        return readingService.updateListening(securityUtils.getCurrentUser(), readingId, request);
    }

    @GetMapping("passage/{readingId}")
    public JResponse getPassage(@PathVariable(name = "readingId") Long readingId) {
        return readingService.getPassage(securityUtils.getCurrentUser(), readingId);
    }

    @GetMapping("passage/{readingId}/questions")
    public JResponse getPassageQuestions(@PathVariable(name = "readingId") Long readingId) {
        return readingService.getPassageQuestion(securityUtils.getCurrentUser(), readingId);
    }

    @PostMapping("answers")
    public JResponse saveReadingAnswers(@RequestBody ModuleAnswersRequest answers) {
        return readingService.saveModuleAnswers(securityUtils.getCurrentUser(), answers);
    }

    @DeleteMapping("delete/{byId}")
    public JResponse deleteListening(@PathVariable(name = "byId") Long listeningId) {
        return readingService.deleteListening(securityUtils.getCurrentUser(), listeningId);
    }

    @PostMapping("delete/{byId}/questions")
    public JResponse deleteListeningQuestions(@PathVariable(name = "byId") Long readingId, @RequestBody DeleteQuestionRequest request) {
        return readingService.deleteListeningQuestion(securityUtils.getCurrentUser(), readingId, request);
    }

    @GetMapping("all")
    public JResponse getAllPassage(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "type", required = false, defaultValue = "all") String type
    ) {
        ModuleFilter filter = new ModuleFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setType(type);
        return readingService.getAllPassage(securityUtils.getCurrentUser(), filter);
    }
}
