package com.search.teacher.Techlearner.service.user;

import com.search.teacher.Techlearner.dto.request.teacher.AddCertificate;
import com.search.teacher.Techlearner.dto.request.teacher.TeacherRequest;
import com.search.teacher.Techlearner.dto.response.SaveResponse;
import com.search.teacher.Techlearner.dto.response.TeacherResponse;
import com.search.teacher.Techlearner.model.entities.Teacher;
import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.model.response.JResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeacherService {
    SaveResponse newTeacher(User user, TeacherRequest request);

    SaveResponse uploadFile(User currentUser, MultipartFile file);

    JResponse allDegrees();

    TeacherResponse getTeacher(User currentUser);

    Teacher findByIdAndActive(Long teacherId);

    SaveResponse updateTeacher(User user, TeacherRequest request);

    JResponse allTopics();

    void updateRatingTeacher(Teacher teacher, List<Double> ratings);

    JResponse addCertificate(Teacher currentTeacher, AddCertificate certificateRequest);
}
