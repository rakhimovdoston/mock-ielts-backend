package com.search.teacher.service.user;

import com.search.teacher.dto.filter.UserFilter;
import com.search.teacher.dto.request.StudentRequest;
import com.search.teacher.dto.response.SaveResponse;
import com.search.teacher.dto.response.StudentResponse;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;

import java.util.Map;

public interface StudentService {
    Map<String, Object> infoDescription();

    JResponse saveStudent(StudentRequest requestBody);

    SaveResponse updateStudent(User currentUser, StudentRequest request);

    StudentResponse getStudentById(User currentUser);

    JResponse getUsersList(UserFilter filter);
}
