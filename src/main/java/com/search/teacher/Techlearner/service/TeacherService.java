package com.search.teacher.Techlearner.service;

import com.search.teacher.Techlearner.dto.request.TeacherRequest;
import com.search.teacher.Techlearner.dto.response.SaveResponse;
import com.search.teacher.Techlearner.dto.response.TeacherResponse;
import com.search.teacher.Techlearner.model.entities.Teacher;
import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.model.response.JResponse;
import org.springframework.web.multipart.MultipartFile;

public interface TeacherService {
    SaveResponse newTeacher(User user, TeacherRequest request);

    SaveResponse uploadFile(User currentUser, MultipartFile file);

    JResponse allDegrees();

    TeacherResponse getTeacher(Long id);

    Teacher findByIdAndActive(Long teacherId);
}
