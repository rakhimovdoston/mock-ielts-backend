package com.search.teacher.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class OrganizationResponse {
    private Long id;
    private String name;
    private String description;
    private String registrationNumber;
    private String address;
    private String city;
    private String phoneNumber;
    private String email;
    private String website;
    private List<String> images;
    private String contactPerson;
    private String contactPersonPhone;
    private String contactPersonEmail;
    private LocalDate establishedDate;
    private String owner;
}
