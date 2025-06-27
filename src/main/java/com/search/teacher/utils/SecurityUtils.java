package com.search.teacher.utils;

import com.search.teacher.model.entities.User;
import com.search.teacher.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            String phoneNumber = (String) authentication.getPrincipal();
            user = userRepository.findByUsername(phoneNumber);
        }
        return user;
    }
}
