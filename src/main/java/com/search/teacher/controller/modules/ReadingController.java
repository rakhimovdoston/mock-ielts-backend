package com.search.teacher.controller.modules;

import com.search.teacher.dto.modules.RQuestionAnswerDto;
import com.search.teacher.dto.modules.ReadingPassageDto;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.modules.ReadingService;
import com.search.teacher.utils.SecurityUtils;
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

    @PostMapping("save/passage-answer")
    public JResponse readingAnswer(@RequestParam(name = "question_id") Long questionId, @RequestBody RQuestionAnswerDto answer) {
        return readingService.saveReadingAnswer(securityUtils.getCurrentUser(), questionId, answer);
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

    @DeleteMapping("delete/passage/{passageId}/answer/{answerId}")
    public JResponse deleteReadingAnswer(@PathVariable("passageId") Long passageId, @PathVariable("answerId") Long answerId) {
        return readingService.deleteReadingAnswer(securityUtils.getCurrentUser(), passageId, answerId);
    }

    @GetMapping("get/{id}")
    public JResponse getReadingById(@PathVariable(name = "id") Long id) {
        return readingService.getReadingById(securityUtils.getCurrentUser(), id);
    }

}
