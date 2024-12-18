package com.search.teacher.service;

import com.search.teacher.dto.ImageDto;
import com.search.teacher.dto.modules.listening.ListeningResponse;
import com.search.teacher.exception.BadRequestException;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.model.entities.Image;
import com.search.teacher.model.entities.Organization;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.entities.modules.reading.ReadingQuestion;
import com.search.teacher.model.enums.ImageType;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.ImageRepository;
import com.search.teacher.service.modules.ListeningService;
import com.search.teacher.service.organization.OrganizationService;
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
    private final OrganizationService organizationService;

    public JResponse uploadPhoto(User currentUser, MultipartFile file, String type) {
        ImageDto image = fileUploadService.fileUpload(currentUser, file, type, true);
        return JResponse.success(image);
    }

    public String checkFilename(String audio, ImageType imageType) {
        Image image = Optional.ofNullable(imageRepository.findByObjectNameAndImageType(audio, imageType))
            .orElseThrow(() -> new BadRequestException("Please upload Listening Audio"));

        return image.getUrl();
    }

    public Image saveImageToQuestion(String imageUrl) {
        return Optional.ofNullable(imageRepository.findByObjectNameAndImageType(imageUrl, ImageType.READING))
            .orElseThrow(() -> new BadRequestException("Please upload Reading Passage"));
    }

    public JResponse getAudioUrl(User currentUser, Long id) {
        Image image = Optional.ofNullable(imageRepository.findByIdAndUserId(id, currentUser.getId()))
            .orElseThrow(() -> new NotfoundException("Audio file not found"));

        ImageDto imageDto = new ImageDto(image.getId(), image.getUrl(), image.getOriginalFilename());
        return JResponse.success(imageDto);
    }

    public JResponse deleteAudioFile(User currentUser, Long id) {
        Image image = Optional.ofNullable(imageRepository.findByIdAndUserId(id, currentUser.getId()))
            .orElseThrow(() -> new NotfoundException("Audio file not found"));

        fileUploadService.removeObject(image, image.getBucket());
        return JResponse.success();
    }

    public Image audioUpload(User currentUser, MultipartFile file) {
        Image image = new Image();
        image.setImageType(ImageType.AUDIO);
        image.setSize(file.getSize());
        image.setActive(true);
        image.setUserId(currentUser.getId());
        image.setOriginalFilename(file.getOriginalFilename());
        imageRepository.save(image);
        fileUploadService.uploadAudio(file, image);
        imageRepository.save(image);
        return image;
    }

    public void removeAudio(Long userId, Image audio) {
        Image image = Optional.ofNullable(imageRepository.findByIdAndUserId(audio.getId(), userId))
            .orElseThrow(() -> new NotfoundException("Audio file not found"));

        fileUploadService.removeObject(image, image.getBucket());
        imageRepository.delete(image);
    }
}
