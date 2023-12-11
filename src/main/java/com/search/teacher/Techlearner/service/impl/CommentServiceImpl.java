package com.search.teacher.Techlearner.service.impl;

import com.search.teacher.Techlearner.dto.request.CommentRequest;
import com.search.teacher.Techlearner.model.entities.Comment;
import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.repository.CommentRepository;
import com.search.teacher.Techlearner.service.CommentService;
import com.search.teacher.Techlearner.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CommentRepository commentRepository;
    private final TeacherService teacherService;

    @Override
    public JResponse createComment(User user, CommentRequest request) {

        Comment comment = new Comment();
        return null;
    }
}
