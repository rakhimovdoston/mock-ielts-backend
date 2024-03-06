package com.search.teacher.Techlearner.service.user;

import com.search.teacher.Techlearner.dto.AuthenticationRequest;
import com.search.teacher.Techlearner.dto.UserDto;
import com.search.teacher.Techlearner.dto.request.ConfirmationRequest;
import com.search.teacher.Techlearner.dto.request.ForgotPasswordReq;
import com.search.teacher.Techlearner.dto.request.ResendRequest;
import com.search.teacher.Techlearner.dto.request.ResetPasswordRequest;
import com.search.teacher.Techlearner.dto.response.AuthenticationResponse;
import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.model.response.JResponse;

import java.util.List;

public interface UserService {

    JResponse registerUser(UserDto userDto);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    JResponse confirmationUser(ConfirmationRequest request);

    JResponse resendEmailCode(ResendRequest request);

    JResponse resetPassword(ResetPasswordRequest req);

    JResponse forgotPassword(ForgotPasswordReq request);

    List<User> getAllUsers();
}
