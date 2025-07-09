package com.search.teacher.controller.ielts;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.request.module.DeleteQuestionRequest;
import com.search.teacher.dto.request.module.ListeningRequest;
import com.search.teacher.dto.request.module.ModuleAnswersRequest;
import com.search.teacher.dto.request.module.QuestionRequest;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.module.ListeningService;
import com.search.teacher.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/listening")
@Tag(name = "IELTS Listening Controller", description = "Operations related to IELTS Listening module management")
public class ListeningController {

    private final ListeningService listeningService;
    private final SecurityUtils securityUtils;

    public ListeningController(ListeningService listeningService, SecurityUtils securityUtils) {
        this.listeningService = listeningService;
        this.securityUtils = securityUtils;
    }

    @PostMapping("save")
    @Operation(
            summary = "Save a new Listening Module",
            description = "Creates a new listening module with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listening module created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request body provided", content = @Content)
            }
    )

    public JResponse saveListening(@RequestBody ListeningRequest request) {
        return listeningService.createListening(securityUtils.getCurrentUser(), request);
    }

    @GetMapping("get/{id}")
    @Operation(
            summary = "Get Listening Module by ID",
            description = "Fetches the details of a Listening module by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listening module retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Listening module not found", content = @Content)
            }
    )

    public JResponse getListening(@PathVariable(name = "id") Long id) {
        return listeningService.getListening(securityUtils.getCurrentUser(), id);
    }

    @PostMapping("/{listeningId}/save/question")
    @Operation(
            summary = "Save a question for a Listening Module",
            description = "Adds a question to an existing Listening module",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Question added successfully"),
                    @ApiResponse(responseCode = "404", description = "Listening module not found")
            }
    )
    public JResponse saveListeningQuestion(
            @PathVariable(name = "listeningId") Long listeningId,
            @RequestBody QuestionRequest request) {
        return listeningService.saveListeningQuestion(securityUtils.getCurrentUser(), listeningId, request);
    }

    @GetMapping("/{listeningId}/get")
    @Operation(
            summary = "Get all questions for a Listening module",
            description = "Fetches all questions associated with a specific Listening module",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Questions retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Listening module not found")
            }
    )
    public JResponse getListeningQuestions(@PathVariable(name = "listeningId") Long listeningId) {
        return listeningService.getListeningQuestionByListening(securityUtils.getCurrentUser(), listeningId);
    }

    @Operation(
            summary = "Delete a Listening Module",
            description = "Deletes a specific Listening module by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listening module deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Listening module not found")
            }
    )
    @DeleteMapping("delete/{byId}")
    public JResponse deleteListening(@PathVariable(name = "byId") Long listeningId) {
        return listeningService.deleteListening(securityUtils.getCurrentUser(), listeningId);
    }

    @Operation(
            summary = "Update questions in a Listening module",
            description = "Updates the questions associated with a specific Listening module",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Questions updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Listening module not found")
            }
    )
    @PutMapping("update/{byId}/questions")
    public JResponse updateListeningQuestions(@PathVariable(name = "byId") Long listeningId, @RequestBody DeleteQuestionRequest request) {
        return listeningService.updateListening(securityUtils.getCurrentUser(), listeningId, request);
    }

    @PostMapping("delete/{byId}/questions")
    @Operation(
            summary = "Delete questions from a Listening module",
            description = "Deletes specific questions from a Listening module",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Questions deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Listening module not found")
            }
    )
    public JResponse deleteListeningQuestions(@PathVariable(name = "byId") Long listeningId, @RequestBody DeleteQuestionRequest request) {
        return listeningService.deleteListeningQuestion(securityUtils.getCurrentUser(), listeningId, request);
    }

    @PostMapping("answers")
    @Operation(
            summary = "Save answers for a Listening module",
            description = "Saves answers for a specific Listening module",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Answers saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request body provided", content = @Content)
            }
    )
    public JResponse saveListeningAnswers(@RequestBody ModuleAnswersRequest answers) {
        return listeningService.saveModuleAnswers(securityUtils.getCurrentUser(), answers);
    }

    @GetMapping("all")
    @Operation(
            summary = "Get all Listening modules",
            description = "Fetches a paginated list of all Listening modules with optional filters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listening modules retrieved successfully")
            }
    )
    public JResponse getAllListening(
            @Parameter(description = "Page number for pagination")
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size for pagination")
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @Parameter(description = "Filter by type of Listening module")
            @RequestParam(name = "type", required = false, defaultValue = "all") String type
    ) {
        ModuleFilter filter = new ModuleFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setType(type);
        return listeningService.getAllListening(securityUtils.getCurrentUser(), filter);
    }
}
