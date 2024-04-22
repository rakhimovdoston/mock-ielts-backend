package com.search.teacher.Techlearner.service.impl;

import com.search.teacher.Techlearner.dto.post.CommentDto;
import com.search.teacher.Techlearner.dto.request.CommentRequest;
import com.search.teacher.Techlearner.dto.request.RatingRequest;
import com.search.teacher.Techlearner.mapper.CommentMapper;
import com.search.teacher.Techlearner.model.entities.*;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.repository.CommentRepository;
import com.search.teacher.Techlearner.repository.ImageRepository;
import com.search.teacher.Techlearner.repository.RatingRepository;
import com.search.teacher.Techlearner.service.CommentService;
import com.search.teacher.Techlearner.service.user.TeacherService;
import com.search.teacher.Techlearner.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CommentRepository commentRepository;
    private final TeacherService teacherService;
    private final ImageRepository imageRepository;
    private final CommentMapper commentMapper;
    private final RatingRepository ratingRepository;

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
        if (comments.isEmpty()) {
            return JResponse.error(404, "You don't have any comments");
        }
        List<CommentDto> commentDtoList = toMapping(comments);
        return JResponse.success(commentDtoList);
    }

    @Override
    public JResponse giveRating(User user, RatingRequest request) {
        Teacher teacher = teacherService.findByIdAndActive(request.getTeacherId());
        List<Double> ratings = ratingRepository.findAllByTeacher(teacher).stream().map(Rating::getRating).collect(Collectors.toList());
        ratings.add(request.getRating());
//        update teacher rating
        teacherService.updateRatingTeacher(teacher, ratings);
        Rating rating = new Rating();
        rating.setRating(request.getRating());
        rating.setTeacher(teacher);
        rating.setUser(user);
        ratingRepository.save(rating);
        return JResponse.success(ResponseMessage.OPERATION_SUCCESSFUL);
    }

    @Override
    public JResponse getAllCommentsByUser(User user) {
        List<Comment> comments = commentRepository.findAllByUser(user);
        if (comments.isEmpty()) {
            return JResponse.error(404, "You don't have any comments");
        }
        List<CommentDto> commentDtoList = toMapping(comments);
        return JResponse.success(commentDtoList);
    }

    private List<CommentDto> toMapping(List<Comment> comments) {
        return comments.stream()
                .map(comment -> {
                    CommentDto commentDto = new CommentDto();
                    commentDto.setContent(comment.getTitle());
                    commentDto.setId(commentDto.getId());
                    User user = comment.getUser();
                    List<Images> images = imageRepository.findByUser(user);
                    if (!images.isEmpty())
                        commentDto.setUserProfileImage(images.get(0).getUrl());
                    return commentDto;
                })
                .toList();
    }
}
