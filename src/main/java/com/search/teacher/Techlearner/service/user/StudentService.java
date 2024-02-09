package com.search.teacher.Techlearner.service.user;

import com.search.teacher.Techlearner.dto.request.StudentRequest;
import com.search.teacher.Techlearner.dto.response.SaveResponse;
import com.search.teacher.Techlearner.dto.response.StudentResponse;
import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.model.response.JResponse;

import java.util.Map;

public interface StudentService {
    Map<String, Object> infoDescription();

    JResponse saveStudent(StudentRequest requestBody);

    SaveResponse updateStudent(User currentUser, StudentRequest request);

    StudentResponse getStudentById(User currentUser);
}
