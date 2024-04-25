package com.search.teacher.dto.request.teacher;

import com.search.teacher.dto.request.EducationRequest;
import com.search.teacher.dto.request.ExperienceRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TeacherRequest {
    private Long id;
    private String title;
    private String description;
    private String phoneNumber;
    private List<Long> topics = new ArrayList<>();
    private List<Long> images = new ArrayList<>();
    private List<EducationRequest> educations = new ArrayList<>();
    private List<AddCertificate> certificate = new ArrayList<>(); // IELTS, TOEFL and other
    private List<ExperienceRequest> experiences = new ArrayList<>();
}
