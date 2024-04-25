package com.search.teacher.dto.response;

import com.search.teacher.dto.ImageDto;
import com.search.teacher.dto.request.EducationRequest;
import com.search.teacher.dto.request.ExperienceRequest;
import com.search.teacher.dto.response.teacher.CertificateResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TeacherResponse {
    private Long id;
    private String title;
    private String description;
    private String firstname;
    private String lastname;
    private Double rating;
    private String profession;
    private String email;
    private String phoneNumber;
    private List<DescribeDto> topics = new ArrayList<>();
    private List<ImageDto> images = new ArrayList<>();
    private List<EducationRequest> educations = new ArrayList<>();
    private List<CertificateResponse> certificates = new ArrayList<>();
    private List<ExperienceRequest> experiences = new ArrayList<>();
}
