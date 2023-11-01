package com.search.teacher.Techlearner.service;

import com.search.teacher.Techlearner.dto.UserDto;
import com.search.teacher.Techlearner.dto.request.ConfirmationRequest;
import com.search.teacher.Techlearner.model.response.JResponse;

public interface UserService {

    JResponse registerUser(UserDto userDto);

    JResponse confirmationUser(ConfirmationRequest request);
}
