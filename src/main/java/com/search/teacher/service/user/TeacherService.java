package com.search.teacher.service.user;

import com.search.teacher.dto.request.teacher.AddCertificate;
import com.search.teacher.dto.request.teacher.TeacherRequest;
import com.search.teacher.dto.response.SaveResponse;
import com.search.teacher.dto.response.TeacherResponse;
import com.search.teacher.model.entities.Teacher;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeacherService {
    SaveResponse newTeacher(User user, TeacherRequest request);

    JResponse allDegrees();

    TeacherResponse getTeacher(User currentUser);

    Teacher findByIdAndActive(Long teacherId);

    SaveResponse updateTeacher(User user, TeacherRequest request);

    JResponse allTopics();

    void updateRatingTeacher(Teacher teacher, List<Double> ratings);

    JResponse addCertificate(Teacher currentTeacher, AddCertificate certificateRequest);

    JResponse deleteCertificate(Teacher currentTeacher, Long certificateId);
}
