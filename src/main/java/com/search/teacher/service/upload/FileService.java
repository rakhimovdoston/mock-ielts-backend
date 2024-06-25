package com.search.teacher.service.upload;

import com.search.teacher.dto.response.SaveResponse;
import com.search.teacher.model.entities.Images;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.enums.ImageType;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.ImageRepository;
import com.search.teacher.utils.ResponseMessage;
import com.search.teacher.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.search.teacher.exception.NotfoundException;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ImageRepository imageRepository;
    private final MinioUploadService minioUploadService;

    public JResponse uploadImage(User user, String type, MultipartFile file) {
        Images images = new Images();
        ImageType imageType = ImageType.getValue(type);
        images.setType(imageType);
        images.setUser(user);
        images.setSize(file.getSize());
        images.setContentType(file.getContentType());
        String filename = getFilename(Objects.requireNonNull(file.getOriginalFilename()));
        images.setFilename(filename);
        images.setBucket(Utils.getBucketWithType(imageType));
        imageRepository.save(images);
        String url = minioUploadService.upload(file, filename, images.getType());
        if (url == null) return JResponse.error(500, "Internal server error");
        images.setUrl(url);
        imageRepository.save(images);
        return JResponse.success(new SaveResponse(images.getId(), images.getFilename()));
    }

    public byte[] downloadImage(User user, String filename) {
        Images images = imageRepository.findByUserAndFilename(user, filename);
        if (images == null) {
            throw new NotfoundException(ResponseMessage.IMAGE_NOT_FOUND);
        }
        return minioUploadService.downloadImage(images);
    }

    private String getFilename(String originalFilename) {
        String filename = originalFilename.replaceAll(" ", "");
        return filename.substring(0, filename.lastIndexOf(".")) + UUID.randomUUID() + "." + filename.substring(filename.lastIndexOf(".") + 1);
    }

    public JResponse uploadFile(MultipartFile file) {
        return JResponse.success();
    }
}
