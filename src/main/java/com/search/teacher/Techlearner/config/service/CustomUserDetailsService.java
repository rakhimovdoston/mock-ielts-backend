package com.search.teacher.Techlearner.config.service;

import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.model.enums.Status;
import com.search.teacher.Techlearner.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null)
            throw new UsernameNotFoundException("User not found");

        if (!user.isActive() || user.getStatus() != Status.active)
            throw new UsernameNotFoundException("User is not active");

        return new UserManager(user);
    }
}
