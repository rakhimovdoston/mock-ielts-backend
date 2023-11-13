package com.search.teacher.Techlearner.dto.request;

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
    private List<Long> topics;
    private List<Long> images;
    private List<EducationRequest> educations = new ArrayList<>();
    private List<String> certificate = new ArrayList<>(); // IELTS, TOEFL and other
    private List<ExperienceRequest> experiences = new ArrayList<>();
}
