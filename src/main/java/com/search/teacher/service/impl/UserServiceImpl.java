package com.search.teacher.service.impl;

import com.search.teacher.dto.AuthenticationRequest;
import com.search.teacher.dto.filter.PaginationResponse;
import com.search.teacher.dto.filter.UserFilter;
import com.search.teacher.dto.message.UserResponse;
import com.search.teacher.dto.request.*;
import com.search.teacher.dto.request.test.TestDateRequest;
import com.search.teacher.dto.request.user.UserRequest;
import com.search.teacher.exception.BadRequestException;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.mapper.UserMapper;
import com.search.teacher.model.entities.MockTestExam;
import com.search.teacher.model.entities.Role;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.UserToken;
import com.search.teacher.model.enums.RoleType;
import com.search.teacher.model.enums.Status;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.MockTestExamRepository;
import com.search.teacher.repository.RoleRepository;
import com.search.teacher.repository.UserRepository;
import com.search.teacher.service.exam.ExamService;
import com.search.teacher.service.user.UserService;
import com.search.teacher.service.user.UserTokenService;
import com.search.teacher.utils.DateUtils;
import com.search.teacher.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserTokenService userTokenService;
    private final ExamService examService;
    private final RoleRepository roleRepository;
    private final MockTestExamRepository mockTestExamRepository;

    @Override
    public JResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByUsername(request.username());
        if (user == null) return JResponse.error(401, ResponseMessage.INCORRECT_USERNAME_PASSWORD);
        if (!passwordEncoder.matches(request.password(), user.getPassword()))
            return JResponse.error(401, ResponseMessage.INCORRECT_USERNAME_PASSWORD);
        if (!user.isActive())
            return JResponse.error(401, ResponseMessage.INCORRECT_USERNAME_PASSWORD);

        if (user.getRoles().stream().anyMatch(role -> !role.getName().equals(RoleType.ROLE_ADMIN.name())) && user.getUserId() != null) {
            if (!DateUtils.checkTestStartTime(user.getTestStartDate())) {
                return JResponse.error(401, ResponseMessage.INCORRECT_USERNAME_PASSWORD);
            }
        }
        return userTokenService.generateToken(user);
    }

    @Override
    public JResponse getProfileData(User currentUser) {
        Set<Role> roles = currentUser.getRoles();
        UserResponse response = UserResponse.builder()
                .id(currentUser.getId())
                .image("")
                .email(currentUser.getEmail())
                .firstname(currentUser.getFirstname())
                .lastname(currentUser.getLastname())
                .roles(roles.stream().map(Role::getName).toList())
                .build();
        return JResponse.success(response);
    }

    @Override
    public JResponse updateProfileData(User currentUser, UserUpdate userDto) {
        currentUser.setFirstname(userDto.getFirstname());
        currentUser.setLastname(userDto.getLastname());
        userRepository.save(currentUser);
        return JResponse.success();
    }

    @Override
    public JResponse createUsers(User currentUser, UserRequest request) {
        if (checkUsername(request.username()))
            return JResponse.error(400, "Username is already taken");

        User newUser = new User();
        newUser.setActive(true);
        Role role = roleRepository.findByName(RoleType.ROLE_USER.name());
        newUser.setUsername(request.username());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setEmail(request.email());
        newUser.setFirstname(request.firstname());
        newUser.setLastname(request.lastname());
        newUser.setPhone(request.phone());
        newUser.setShowPassword(request.password());
        newUser.getRoles().add(role);
        newUser.setUserId(currentUser.getId());
        userRepository.save(newUser);
        return JResponse.success();
    }

    @Override
    public boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public JResponse allUsersByFilter(User currentUser, UserFilter userFilter) {
        int countAllUsers = userRepository.countUsers(List.of(currentUser.getId()), userFilter);
        List<User> users = userRepository.findAllUsersByModuleFilter(List.of(currentUser.getId()), userFilter);
        PaginationResponse response = new PaginationResponse();
        response.setCurrentPage(userFilter.getPage());
        response.setCurrentSize(userFilter.getSize());
        response.setTotalPages((int) Math.ceil((double) countAllUsers / userFilter.getSize()));
        response.setTotalSizes(countAllUsers);
        var usersDto = UserMapper.INSTANCE.toList(users);
//        for (User user : users) {
//            UserResponse dto = UserMapper.INSTANCE.toResponse(user);
//            if (!DateUtils.checkTestStartTime(dto.getTestStartDate())) {
//                dto.setTestStartDate(null);
//                user.setTestStartDate(null);
//                userRepository.save(user);
//            }
//        }
        response.setData(usersDto);
        return JResponse.success(response);
    }

    @Override
    public JResponse createTeacher(User currentUser, UserRequest request) {
        if (checkUsername(request.username()))
            return JResponse.error(400, "Username is already taken");

        User newUser = new User();
        newUser.setActive(true);
        Role role = roleRepository.findByName(RoleType.ROLE_TEACHER.name());
        newUser.setUsername(request.username());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setEmail(request.email());
        newUser.setFirstname(request.firstname());
        newUser.setLastname(request.lastname());
        newUser.setPhone(request.phone());
        newUser.setShowPassword(request.password());
        newUser.getRoles().add(role);
        newUser.setUserId(currentUser.getId());
        userRepository.save(newUser);
        return JResponse.success();
    }

    @Override
    public int countUser(User currentUser) {
        return userRepository.countAllByUserIdIn(List.of(currentUser.getId()));
    }

    @Override
    public JResponse getUserBydId(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotfoundException("User Not found"));
        UserResponse response = UserMapper.INSTANCE.toResponse(user);
        return JResponse.success(response);
    }

    @Override
    public JResponse updateUser(User currentUser, Long id, UserRequest request) {
        User user = getUserByIdAndUser(id, currentUser);
        user.setFirstname(request.firstname());
        user.setLastname(request.lastname());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
        return JResponse.success();
    }

    @Override
    public JResponse deleteUsers(User currentUser, Long id) {
        User user = getUserByIdAndUser(id, currentUser);
        user.setActive(false);
        userRepository.save(user);
        return JResponse.success();
    }

    @Override
    public JResponse setTestDate(User currentUser, Long id, TestDateRequest testDate) {
        User user = getUserByIdAndUser(id, currentUser);
        user.setTestStartDate(testDate.date());
        userRepository.save(user);
        return JResponse.success();
    }

    @Override
    public JResponse refreshToken(RefreshTokenReq refreshTokenReq) {
        UserToken userToken = userTokenService.findByRefreshToken(refreshTokenReq.getRefreshToken());
        if (userToken == null) throw new BadRequestException("Refresh token expired.");

        if (!DateUtils.isExpirationToken(userToken.getExpireDate()))
            throw new BadRequestException("Refresh token expired");

        return userTokenService.generateToken(userToken.getUser());
    }

    private String getRandomCode(int min, int max) {
        Random random = new Random();
        return String.valueOf(random.nextInt(min, max));
    }

    private User getUserByIdAndUser(Long id, User user) {
        User userById = userRepository.findByIdAndUserId(id, user.getId());
        if (userById == null) throw new NotfoundException("User Not found");
        return userById;
    }
}
