package com.search.teacher.service.user;

import com.search.teacher.dto.AuthenticationRequest;
import com.search.teacher.dto.UserDto;
import com.search.teacher.dto.filter.UserFilter;
import com.search.teacher.dto.request.*;
import com.search.teacher.dto.request.test.TestDateRequest;
import com.search.teacher.dto.request.user.UserRequest;
import com.search.teacher.dto.response.AuthenticationResponse;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {
    JResponse authenticate(AuthenticationRequest request);

    JResponse refreshToken(RefreshTokenReq refreshTokenReq);

    JResponse getProfileData(User currentUser);

    JResponse updateProfileData(User currentUser, @Valid UserUpdate userDto);

    JResponse createUsers(User currentUser, UserRequest request);

    boolean checkUsername(String username);

    JResponse allUsersByFilter(User currentUser, UserFilter userFilter);

    JResponse createTeacher(User currentUser, UserRequest request);

    int countUser(User currentUser);

    JResponse getUserBydId(Long id);

    JResponse updateUser(User currentUser, Long id, UserRequest request);

    JResponse deleteUsers(User currentUser, Long id);

    JResponse setTestDate(User currentUser, Long id, TestDateRequest testDate);

    User getUserById(Long id);
}
