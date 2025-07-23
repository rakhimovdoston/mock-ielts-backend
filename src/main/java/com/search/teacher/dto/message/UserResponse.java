package com.search.teacher.dto.message;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private String username;
    private List<String> roles = new ArrayList<>();
    private String image;
    private String status;
    private Date testStartDate;
    private String password;
}
