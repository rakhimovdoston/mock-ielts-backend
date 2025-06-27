package com.search.teacher.service;

import com.search.teacher.dto.ImageDto;
import com.search.teacher.exception.BadRequestException;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.model.entities.Image;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.enums.ImageType;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.ImageRepository;
import com.search.teacher.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

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
    private final FileUploadService fileUploadService;

    public JResponse uploadPhoto(User currentUser, MultipartFile file, String type) {
        ImageDto image = fileUploadService.fileUpload(currentUser, file, type, true);
        return JResponse.success(image);
    }

    public JResponse deleteAudioFile(User currentUser, Long id) {
        Image image = Optional.ofNullable(imageRepository.findByIdAndUserId(id, currentUser.getId()))
                .orElseThrow(() -> new NotfoundException("Image file not found"));

        fileUploadService.removeObject(image, image.getBucket());
        return JResponse.success();
    }

    public JResponse deleteAudioByUrl(User currentUser, String url) {
        Image image = imageRepository.findByUrl(url);
        if (image != null) {
            if (image.getUserId().equals(currentUser.getId())) {
                fileUploadService.removeObject(image, image.getBucket());
                imageRepository.delete(image);
                return JResponse.success();
            } else {
                throw new NotfoundException("Image file not found");
            }
        }
        return JResponse.error(404, "Image file not found");
    }
}
