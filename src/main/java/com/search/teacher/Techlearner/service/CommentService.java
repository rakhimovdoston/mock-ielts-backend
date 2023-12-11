package com.search.teacher.Techlearner.service;

import com.search.teacher.Techlearner.dto.request.CommentRequest;
import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.model.response.JResponse;

public interface CommentService {
    JResponse createComment(User user, CommentRequest request);
}
