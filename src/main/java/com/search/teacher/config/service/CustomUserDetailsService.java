package com.search.teacher.config.service;

import com.search.teacher.model.entities.User;
import com.search.teacher.model.enums.Status;
import com.search.teacher.repository.UserRepository;
import com.search.teacher.utils.ResponseMessage;
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
            throw new UsernameNotFoundException(ResponseMessage.INCORRECT_USERNAME_PASSWORD);

        if (!user.isActive() || user.getStatus() != Status.active)
            throw new UsernameNotFoundException(ResponseMessage.USER_NOT_ACTIVATED);

        return new UserManager(user);
    }
}
