package com.search.teacher.Techlearner.service.impl;

import com.search.teacher.Techlearner.components.Constants;
import com.search.teacher.Techlearner.config.rabbit.RabbitMqProducer;
import com.search.teacher.Techlearner.dto.AuthenticationRequest;
import com.search.teacher.Techlearner.dto.UserDto;
import com.search.teacher.Techlearner.dto.request.ConfirmationRequest;
import com.search.teacher.Techlearner.dto.request.ForgotPasswordReq;
import com.search.teacher.Techlearner.dto.request.ResendRequest;
import com.search.teacher.Techlearner.dto.request.ResetPasswordRequest;
import com.search.teacher.Techlearner.dto.response.AuthenticationResponse;
import com.search.teacher.Techlearner.dto.response.RegisterResponse;
import com.search.teacher.Techlearner.dto.response.SaveResponse;
import com.search.teacher.Techlearner.exception.BadRequestException;
import com.search.teacher.Techlearner.exception.NotfoundException;
import com.search.teacher.Techlearner.model.entities.Role;
import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.model.enums.RoleType;
import com.search.teacher.Techlearner.model.enums.Status;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.repository.RoleRepository;
import com.search.teacher.Techlearner.repository.UserRepository;
import com.search.teacher.Techlearner.service.UserSession;
import com.search.teacher.Techlearner.service.user.UserService;
import com.search.teacher.Techlearner.service.user.UserTokenService;
import com.search.teacher.Techlearner.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserSession userSession;
    private final UserTokenService userTokenService;
    private final RabbitMqProducer rabbitMqProducer;
    private final CacheManager cacheManager;

    @Override
    public JResponse registerUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.email())) {
            throw new BadRequestException(JResponse.error(400, "This email has already been registered!"));
        }
        User user = userDto.toUser();
        user.setPassword(passwordEncoder.encode(userDto.password()));
        Role role = roleRepository.findByName(RoleType.getRoleByName(userDto.role()));
        if (role == null) throw new NotfoundException("Role not found");
        user.setRole(role);
        user.setStatus(Status.confirm);
        String confirmationCode = getRandomCode(100000, 999999);
        user.setCode(confirmationCode);
        userRepository.save(user);
        logger.info("User saved: {}", user.getId());
        rabbitMqProducer.sendNotificationToEmail(user.getEmail(), confirmationCode);
        return JResponse.success(new RegisterResponse(user.getEmail()));
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = userRepository.findByEmail(request.email());
        if (user == null) throw new UsernameNotFoundException("Username or password incorrect");

        return userTokenService.generateToken(user);
    }

    @Override
    public JResponse confirmationUser(ConfirmationRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new NotfoundException("This email not found user");
        }

        if (!DateUtils.isExpirationCode(user.getCreatedDate()))
            throw new BadRequestException("The time to enter the code has expired.");

        if (!user.getCode().equals(request.getCode())) {
            throw new BadRequestException("You have entered an incorrect code");
        }

        user.setCode(null);
        user.setStatus(Status.active);
        user.setActive(true);
        userRepository.save(user);
        return JResponse.success(new SaveResponse(user.getId()));
    }

    @Override
    public JResponse resendEmailCode(ResendRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return JResponse.error(400, "This email not exist!");
        }

        if (user.isActive() && user.getStatus() == Status.active && !request.isForgotPassword()) {
            return JResponse.error(200, "This user already verified");
        }
        String code = getRandomCode(100000, 999999);
        user.setCode(code);
        user.setForgotPassword(request.isForgotPassword());
        user.setUpdatedDate(new Date());
        userRepository.save(user);
        if (user.isForgotPassword()) {
            rabbitMqProducer.sendForgotPasswordEmail(user.getEmail(), code);
        } else {
            rabbitMqProducer.sendNotificationToEmail(user.getEmail(), code);
        }
        return JResponse.success(new RegisterResponse(user.getEmail()));
    }

    @Override
    public JResponse resetPassword(ResetPasswordRequest req) {
        User user = userSession.getUser();
        boolean matches = passwordEncoder.matches(req.oldPassword(), req.newPassword());
        if (matches) {
            if (req.newPassword().equals(req.confirmPassword())) {
                user.setPassword(passwordEncoder.encode(req.newPassword()));
                userRepository.save(user);
                return new JResponse(200, "Password updated");
            } else {
                return new JResponse(401, "non matched password");
            }
        }
        return new JResponse(401, "Incorrect password");
    }

    @Override
    public JResponse forgotPassword(ForgotPasswordReq request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return JResponse.error(400, "This email not exist!");
        }

        if (!StringUtils.isEmpty(user.getCode())) {
            if (request.getPassword().equals(request.getConfirmPassword())) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setCode(null);
                user.setForgotPassword(false);
                userRepository.save(user);
                return JResponse.success("Password updated");
            } else return JResponse.error(400, "Password non match");
        }
        return JResponse.error(400, "You should confirm your password");
    }

    @Override
    @Cacheable(cacheNames = Constants.USER_EMAIL)
    public List<User> getAllUsers() {
        logger.info("Get all users");
        return userRepository.findAll();
    }

    private String getRandomCode(int min, int max) {
        Random random = new Random();
        return String.valueOf(random.nextInt(min, max));
    }
}
