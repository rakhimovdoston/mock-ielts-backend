package com.search.teacher.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class OrgResponse {
    private String name;
    private String description;
    private String registrationNumber;
    private String address;
    private String city;
    private String phoneNumber;
    private String email;
    private String website;
    private String imageUrl;
    private String contactPerson;
    private String contactPersonPhone;
    private String contactPersonEmail;
    private LocalDate establishedDate;
    private String owner;
}
