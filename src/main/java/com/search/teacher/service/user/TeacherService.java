package com.search.teacher.service.user;

import com.search.teacher.dto.request.teacher.AddCertificate;
import com.search.teacher.dto.request.teacher.TeacherRequest;
import com.search.teacher.dto.response.SaveResponse;
import com.search.teacher.dto.response.TeacherResponse;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;

import java.util.List;

public interface TeacherService {
    SaveResponse newTeacher(User user, TeacherRequest request);

    JResponse allDegrees();

    TeacherResponse getTeacher(User currentUser);

    SaveResponse updateTeacher(User user, TeacherRequest request);

    JResponse allTopics();
}
