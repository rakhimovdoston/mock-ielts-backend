package com.search.teacher.Techlearner.config.service;

import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.model.enums.Status;
import com.search.teacher.Techlearner.repository.UserRepository;
import com.search.teacher.Techlearner.utils.ResponseMessage;
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
