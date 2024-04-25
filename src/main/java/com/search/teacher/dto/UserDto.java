package com.search.teacher.dto;

import com.search.teacher.model.entities.User;
import jakarta.validation.constraints.NotNull;

public record UserDto(Long id,
                      String firstname,
                      String lastname,
                      @NotNull
                      String email,
                      @NotNull
                      String role,
                      @NotNull
                      String password) {

    public User toUser() {
        User user = new User();
        user.setEmail(email);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        return user;
    }
}
