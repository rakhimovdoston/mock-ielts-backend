package com.search.teacher.Techlearner.utils;

import com.search.teacher.Techlearner.model.entities.Teacher;
import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.repository.TeacherRepository;
import com.search.teacher.Techlearner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;

    public User currentUser() {
        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            String phoneNumber = (String) authentication.getPrincipal();
            user = userRepository.findByEmail(phoneNumber);
        }
        return user;
    }

    public Teacher getCurrentTeacher() {
        return teacherRepository.findTeacherByUser(currentUser());
    }
}
