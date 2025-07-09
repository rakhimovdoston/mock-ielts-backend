package com.search.teacher.controller.ielts;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.request.module.DeleteQuestionRequest;
import com.search.teacher.dto.request.module.ModuleAnswersRequest;
import com.search.teacher.dto.request.module.QuestionRequest;
import com.search.teacher.dto.request.module.ReadingPassageRequest;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.module.ReadingService;
import com.search.teacher.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/reading")
@Tag(name = "IELTS Reading Controller", description = "Operations related to IELTS Reading module management")
public class ReadingController {

    private final ReadingService readingService;
    private final SecurityUtils securityUtils;

    public ReadingController(ReadingService readingService, SecurityUtils securityUtils) {
        this.readingService = readingService;
        this.securityUtils = securityUtils;
    }

    @Operation(
            summary = "Create a new Reading Passage",
            description = "Adds a new passage to the reading module",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Passage created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content)
            }
    )
    @PostMapping("create/passage")
    public JResponse createPassage(@RequestBody ReadingPassageRequest passage) {
        return readingService.createPassage(securityUtils.getCurrentUser(), passage);
    }

    @Operation(
            summary = "Save a question for a Reading Passage",
            description = "Adds a new question to a specific Reading passage",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Question added successfully"),
                    @ApiResponse(responseCode = "404", description = "Reading passage not found", content = @Content)
            }
    )
    @PostMapping("passage/{reading}/save/question")
    public JResponse saveQuestion(
            @PathVariable(name = "reading") Long readingId,
            @RequestBody QuestionRequest passage) {
        return readingService.savePassageQuestion(securityUtils.getCurrentUser(), passage, readingId);
    }

    @Operation(
            summary = "Update questions in a Reading Passage",
            description = "Updates the questions of a specific Reading passage",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Questions updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Reading passage not found", content = @Content)
            }
    )
    @PutMapping("update/{byId}/questions")
    public JResponse updateListeningQuestions(@PathVariable(name = "byId") Long readingId, @RequestBody DeleteQuestionRequest request) {
        return readingService.updateListening(securityUtils.getCurrentUser(), readingId, request);
    }

    @Operation(
            summary = "Get a Reading Passage by ID",
            description = "Fetches the details of a Reading passage by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reading passage retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Reading passage not found", content = @Content)
            }
    )
    @GetMapping("passage/{readingId}")
    public JResponse getPassage(@PathVariable(name = "readingId") Long readingId) {
        return readingService.getPassage(securityUtils.getCurrentUser(), readingId);
    }

    @Operation(
            summary = "Get questions for a Reading Passage",
            description = "Fetches all questions for a specific Reading passage by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Questions retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Reading passage not found", content = @Content)
            }
    )
    @GetMapping("passage/{readingId}/questions")
    public JResponse getPassageQuestions(@PathVariable(name = "readingId") Long readingId) {
        return readingService.getPassageQuestion(securityUtils.getCurrentUser(), readingId);
    }

    @Operation(
            summary = "Save answers for a Reading Module",
            description = "Saves answers provided for a reading module",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Answers saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content)
            }
    )
    @PostMapping("answers")
    public JResponse saveReadingAnswers(@RequestBody ModuleAnswersRequest answers) {
        return readingService.saveModuleAnswers(securityUtils.getCurrentUser(), answers);
    }

    @Operation(
            summary = "Delete a Reading Passage by ID",
            description = "Deletes a Reading passage by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reading passage deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Reading passage not found", content = @Content)
            }
    )
    @DeleteMapping("delete/{byId}")
    public JResponse deleteListening(@PathVariable(name = "byId") Long listeningId) {
        return readingService.deleteListening(securityUtils.getCurrentUser(), listeningId);
    }

    @Operation(
            summary = "Delete questions from a Reading Passage",
            description = "Deletes one or more questions from a specific Reading passage",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Questions deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Reading passage not found", content = @Content)
            }
    )
    @PostMapping("delete/{byId}/questions")
    public JResponse deleteListeningQuestions(@PathVariable(name = "byId") Long readingId, @RequestBody DeleteQuestionRequest request) {
        return readingService.deleteListeningQuestion(securityUtils.getCurrentUser(), readingId, request);
    }

    @Operation(
            summary = "Get all Reading Passages",
            description = "Fetches a paginated list of all Reading passages, optionally filtered by type",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reading passages retrieved successfully")
            }
    )
    @GetMapping("all")
    public JResponse getAllPassage(
            @Parameter(description = "Page number for pagination")
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size for pagination")
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @Parameter(description = "Type of Reading passage to filter by")

            @RequestParam(name = "type", required = false, defaultValue = "all") String type
    ) {
        ModuleFilter filter = new ModuleFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setType(type);
        return readingService.getAllPassage(securityUtils.getCurrentUser(), filter);
    }
}
