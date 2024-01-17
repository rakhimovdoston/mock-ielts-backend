package com.search.teacher.Techlearner.controller.post;

import com.search.teacher.Techlearner.dto.request.CommentRequest;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.service.CommentService;
import com.search.teacher.Techlearner.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
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
        JResponse response = commentService.getAllComments(securityUtils.getCurrentTeacher());
        return ResponseEntity.ok(response);
    }
}
