package com.search.teacher.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record OrganizationRequest(Long id,
                                  @NotBlank String name,
                                  String registrationNumber,
                                  String address,
                                  String city,
                                  String phoneNumber,
                                  String email,
                                  String website,
                                  Long logoId) {
}
