package com.search.teacher.service.user;

import com.search.teacher.dto.AuthenticationRequest;
import com.search.teacher.dto.UserDto;
import com.search.teacher.dto.request.ConfirmationRequest;
import com.search.teacher.dto.request.ForgotPasswordReq;
import com.search.teacher.dto.request.ResendRequest;
import com.search.teacher.dto.request.ResetPasswordRequest;
import com.search.teacher.dto.response.AuthenticationResponse;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;

import java.util.List;

public interface UserService {

    JResponse registerUser(UserDto userDto);

    JResponse authenticate(AuthenticationRequest request);

    JResponse confirmationUser(ConfirmationRequest request);

    JResponse resendEmailCode(ResendRequest request);

    JResponse resetPassword(ResetPasswordRequest req);

    JResponse forgotPassword(ForgotPasswordReq request);

    List<User> getAllUsers();
}
