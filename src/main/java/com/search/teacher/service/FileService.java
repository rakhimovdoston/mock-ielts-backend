package com.search.teacher.service;

import com.search.teacher.model.entities.User;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.ImageRepository;
import com.search.teacher.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Package com.search.teacher.service
 * Created by doston.rakhimov
 * Date: 17/10/24
 * Time: 17:46
 **/
@Service
@RequiredArgsConstructor
public class FileService {

    private final UserService userService;
    private final ImageRepository imageRepository;

    public JResponse uploadPhoto(User currentUser, MultipartFile file, String type) {
        return null;
    }
}
