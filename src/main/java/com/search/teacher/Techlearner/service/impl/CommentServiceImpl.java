package com.search.teacher.Techlearner.service.impl;

import com.search.teacher.Techlearner.dto.post.CommentDto;
import com.search.teacher.Techlearner.dto.request.CommentRequest;
import com.search.teacher.Techlearner.mapper.CommentMapper;
import com.search.teacher.Techlearner.model.entities.Comment;
import com.search.teacher.Techlearner.model.entities.Images;
import com.search.teacher.Techlearner.model.entities.Teacher;
import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.repository.CommentRepository;
import com.search.teacher.Techlearner.service.CommentService;
import com.search.teacher.Techlearner.service.user.TeacherService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CommentRepository commentRepository;
    private final TeacherService teacherService;
    private final CommentMapper commentMapper;

    @Override
    public JResponse createComment(User user, CommentRequest request) {
        Comment comment = new Comment();
        Teacher teacher = teacherService.findByIdAndActive(request.getTeacherId());
        comment.setTeacher(teacher);
        comment.setUser(user);
        comment.setTitle(request.getTitle());
        commentRepository.save(comment);
        return JResponse.success();
    }

    @Override
    public JResponse getAllComments(Teacher teacher) {
        List<Comment> comments = commentRepository.findAllByTeacher(teacher);
        List<CommentDto> commentDtoList = comments.stream()
                .map(comment -> {
                    CommentDto commentDto = new CommentDto();
                    commentDto.setContent(comment.getTitle());
                    commentDto.setId(commentDto.getId());
                    User user = comment.getUser();
                    commentDto.setUserEmail(user.getEmail());
                    List<Images> images = user.getImages();
                    if (!images.isEmpty())
                        commentDto.setUserProfileImage(images.get(0).getUrl());
                    return commentDto;
                })
                .toList();
        return JResponse.success(commentDtoList);
    }
}
