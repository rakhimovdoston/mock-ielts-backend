package com.search.teacher.controller.post;

import com.search.teacher.dto.request.CommentRequest;
import com.search.teacher.dto.request.RatingRequest;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.CommentService;
import com.search.teacher.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
@Tag(name = "Comment Controller")
public class CommentController {

    private final SecurityUtils securityUtils;
    private final CommentService commentService;
    @PostMapping("save")
    public ResponseEntity<JResponse> saveComment(@Valid @RequestBody CommentRequest request) {
        JResponse response = commentService.createComment(securityUtils.currentUser(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("all")
    public ResponseEntity<JResponse> getAllComments() {
        JResponse response = commentService.getAllCommentsByUser(securityUtils.currentUser());
        return ResponseEntity.ok(response);
    }

    @PostMapping("add-rating")
    public JResponse giveRating(@Valid @RequestBody RatingRequest request) {
        return commentService.giveRating(securityUtils.currentUser(), request);
    }
}
