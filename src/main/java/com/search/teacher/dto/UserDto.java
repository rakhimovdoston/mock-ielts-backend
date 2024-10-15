package com.search.teacher.dto;

import com.search.teacher.model.entities.User;
import com.search.teacher.model.enums.Type;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDto(Long id,
                      String name,
                      String firstname,
                      String lastname,
                      @NotNull(message = "Email must not be null")
                      @Email(message = "Email should be valid")
                      String email,
                      String description,
                      @NotNull(message = "Password must not be null")
                      @Size(min = 6, message = "Password must be at least 6 characters long")
                      String password) {

    public User toUser() {
        User user = new User();
        user.setEmail(email);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        return user;
    }
}
