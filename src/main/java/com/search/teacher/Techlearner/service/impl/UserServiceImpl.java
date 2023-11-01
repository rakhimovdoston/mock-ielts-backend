package com.search.teacher.Techlearner.service.impl;

import com.search.teacher.Techlearner.dto.UserDto;
import com.search.teacher.Techlearner.dto.request.ConfirmationRequest;
import com.search.teacher.Techlearner.dto.response.RegisterResponse;
import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.model.enums.RoleType;
import com.search.teacher.Techlearner.model.enums.Status;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.repository.RoleRepository;
import com.search.teacher.Techlearner.repository.UserRepository;
import com.search.teacher.Techlearner.service.mail.MailSendService;
import com.search.teacher.Techlearner.service.UserService;
import com.search.teacher.Techlearner.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSendService mailSendService;

    @Override
    public JResponse registerUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.email())) {
            return JResponse.error(400, "This email has already been registered!");
        }
        User user = userDto.toUser();
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setRole(roleRepository.findByName(RoleType.ROLE_STUDENT));
        user.setStatus(Status.confirm);
        String confirmationCode = getRandomCode(100000, 999999);
        user.setCode(confirmationCode);
        userRepository.save(user);
        logger.info("User saved: {}", user.getId());
        mailSendService.sendConfirmRegister(user.getEmail(), confirmationCode);
        return JResponse.success(new RegisterResponse(user.getEmail()));
    }

    @Override
    public JResponse confirmationUser(ConfirmationRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return JResponse.error(400, "This email has already been registered!");
        }

        if (!DateUtils.isExpirationCode(user.getCreatedDate()))
            return  JResponse.error(400, "The time to enter the code has expired.");

        if (user.getCode().equals(request.getCode())) {
            return JResponse.error(400, "You have entered an incorrect code");
        }

        user.setCode(null);
        user.setStatus(Status.active);
        user.setActive(true);
        userRepository.save(user);
        return JResponse.success("User is verified");
    }

    private String getRandomCode(int min, int max) {
        Random random = new Random();
        return String.valueOf(random.nextInt(min, max));
    }
}
