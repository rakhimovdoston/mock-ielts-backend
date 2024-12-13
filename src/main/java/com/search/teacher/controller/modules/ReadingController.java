package com.search.teacher.controller.modules;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.modules.PassageConfirmDto;
import com.search.teacher.dto.modules.RQuestionAnswerDto;
import com.search.teacher.dto.modules.ReadingPassageDto;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.modules.ReadingService;
import com.search.teacher.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Package com.search.teacher.controller.modules
 * Created by doston.rakhimov
 * Date: 21/06/24
 * Time: 15:05
 **/
@RestController
@RequestMapping("api/v1/reading")
@Tag(name = "Reading Module API")
@RequiredArgsConstructor
public class ReadingController {

    private final ReadingService readingService;
    private final SecurityUtils securityUtils;

    @PostMapping("passage/create")
    public ResponseEntity<JResponse> createReadingPassage(@RequestBody ReadingPassageDto passage) {
        return ResponseEntity.ok(readingService.createPassage(securityUtils.getCurrentUser(), passage));
    }

    @PostMapping("update/passage")
    public JResponse updateReadingPassage(@RequestBody ReadingPassageDto passage) {
        return readingService.updatePassage(securityUtils.getCurrentUser(), passage);
    }

    @PostMapping("save/{passageId}/passage-answer")
    public JResponse readingAnswer(@PathVariable(name = "passageId") Long questionId, @RequestBody RQuestionAnswerDto answer) {
        return readingService.saveReadingAnswer(securityUtils.getCurrentUser(), questionId, answer);
    }

    @PostMapping("passage/confirm")
    public JResponse confirmReadingPassage(@RequestBody PassageConfirmDto confirm) {
        return readingService.confirmPassage(securityUtils.getCurrentUser(), confirm);
    }

    @PutMapping("update/{passageId}/answer")
    public JResponse updateReadingAnswer(@PathVariable("passageId") Long readingId, @RequestBody RQuestionAnswerDto answer) {
        if (answer.getId() == null) return JResponse.error(400, "Please select Question Answer");
        return readingService.updateReadingAnswer(securityUtils.getCurrentUser(), readingId, answer);
    }

    @DeleteMapping("delete/passage/{id}")
    public JResponse deleteReadingPassage(@PathVariable("id") Long readingId) {
        return readingService.deleteReadingPassage(securityUtils.getCurrentUser(), readingId);
    }

    @DeleteMapping("delete/passage/{passageId}/answer")
    public JResponse deleteReadingAnswer(@PathVariable("passageId") Long passageId,
                                         @RequestParam(name = "questionId") Long questionId,
                                         @RequestParam(name = "type") String type) {
        return readingService.deleteReadingAnswer(securityUtils.getCurrentUser(), passageId, questionId, type);
    }

    @GetMapping("passage/{id}")
    public JResponse getReadingById(@PathVariable(name = "id") Long id,
                                    @RequestParam(name = "withAnswer", required = false, defaultValue = "0") boolean withAnswer) {
        return readingService.getReadingById(securityUtils.getCurrentUser(), id, withAnswer);
    }

    @GetMapping("passage/{id}/questions")
    public JResponse getReadingQuestions(@PathVariable(name = "id") Long passageId) {

        return readingService.getPassageQuestion(securityUtils.getCurrentUser(), passageId);
    }

    @GetMapping("passage/all")
    public JResponse getAllReadings(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                    @RequestParam(name = "size", defaultValue = "10", required = false) int size,
                                    @RequestParam(name = "type", defaultValue = "all", required = false) String type) {
        ModuleFilter moduleFilter = new ModuleFilter();
        moduleFilter.setType(type);
        moduleFilter.setPage(page);
        moduleFilter.setSize(size);
        return readingService.getAllReadingPassage(securityUtils.getCurrentUser(), moduleFilter);
    }

}
