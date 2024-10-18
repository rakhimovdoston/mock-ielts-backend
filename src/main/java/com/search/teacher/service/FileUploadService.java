package com.search.teacher.service;

import com.search.teacher.config.ApplicationProperties;
import com.search.teacher.dto.ImageDTO;
import com.search.teacher.exception.BadRequestException;
import com.search.teacher.model.entities.Image;
import com.search.teacher.model.enums.ImageType;
import com.search.teacher.repository.ImageRepository;
import io.minio.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@Slf4j
public class FileUploadService {
    private final ImageRepository imageRepository;
    private final ApplicationProperties applicationProperties;
    private final MinioClient minioClient;

    public FileUploadService(ImageRepository imageRepository, ApplicationProperties applicationProperties, MinioClient minioClient) {
        this.imageRepository = imageRepository;
        this.applicationProperties = applicationProperties;
        this.minioClient = minioClient;
    }


    public void removeObject(Image image, String bucket) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(applicationProperties.getMinio().getApplicationName())
                    .object(image.getObjectName())
                    .build());
            imageRepository.deleteById(image.getId());
        } catch (ErrorResponseException |
                 InsufficientDataException |
                 InternalException |
                 InvalidKeyException |
                 InvalidResponseException |
                 IOException |
                 NoSuchAlgorithmException |
                 ServerException |
                 XmlParserException e) {
            log.error(e.getMessage(), e);
        }
    }

    public Image fileUpload(MultipartFile file, String type, boolean isPublic) {
        this.validateFile(file);
        ImageType imageType = ImageType.valueOf(type);
        ImageDTO imageDTO;
        try {
            imageDTO = uploadToStorageServer(file.getBytes(), file.getOriginalFilename(), file.getContentType(), getBucket(imageType), isPublic);
        } catch (Exception e) {
            log.error("An unaccepted error has occurred while uploading file: ", e);
            throw new RuntimeException("An unaccepted error has occurred while uploading file");
        }
        Image image = wrap(file, imageDTO);
        image.setImageType(imageType);
        imageRepository.save(image);
        return image;
    }

    private String getBucket(ImageType imageType) {
        return switch (imageType) {
            case LOGO -> "logos";
            case PROFILE_PICTURE -> "profiles";
            case READING, LISTENING, WRITING, SPEAKING -> "modules";
            default -> "photos";
        };
    }

    private Image wrap(MultipartFile file, ImageDTO imageDTO) {
        Image image = new Image();
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setUrl(imageDTO.getUrl());
        image.setObjectName(imageDTO.getObjectName());
        return image;
    }


    public ImageDTO uploadToStorageServer(byte[] file, String fileName, String contentType, String bucket, boolean isPublic) {
        String objectName = this.getObjectName(fileName, isPublic);

        ByteArrayInputStream stream = new ByteArrayInputStream(file);

        log.info("FILENAME: {} -------------------------------------", fileName);
        log.info("objectName: {} -------------------------------------", objectName);
        log.info("contentType: {} -------------------------------------", contentType);
        try {
            this.uploadWithPutObject(
                    PutObjectArgs
                            .builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(stream, file.length, -1)
                            .contentType(contentType)
                            .build()
            );

            ImageDTO uploadDTO = new ImageDTO();
            uploadDTO.setUrl(StringUtils.join(applicationProperties.getMinio().getHost(), "/", applicationProperties.getMinio().getApplicationName(), "/", objectName));
            uploadDTO.setObjectName(fileName);
            log.info("Image uploaded successfully");
            return uploadDTO;
        } catch (Exception e) {
            log.error("Close uploaded file error: {}", e.getMessage());
        } finally {
            try {
                stream.close();
            } catch (Exception e) {
                log.error("Close uploaded file error: {}", e.getMessage());
            }
        }
        return null;
    }

    private void uploadWithPutObject(PutObjectArgs objectArgs) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(objectArgs.bucket()).build())) {
                throw new RuntimeException("Bucket does not exist");
            }
            Optional.ofNullable(this.minioClient.putObject(objectArgs)).map(ObjectWriteResponse::etag);
        } catch (Exception e) {
            log.error("Error upload file: {}", e.getMessage());
            throw new RuntimeException("Error upload file");
        }
    }

    private String getObjectName(final String fileName, final boolean isPublic) {
        final UUID uuid = UUID.randomUUID();
        final String extension = FilenameUtils.getExtension(fileName);
        return StringUtils.join(isPublic ? "public/" : "", uuid.toString(), StringUtils.isEmpty(extension) ? "" : '.' + extension);
    }

    private String encodeFileName(String originalFilename) {
        String fileName = null;
        try {
            fileName = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return fileName;
    }

    protected void validateFile(MultipartFile file) {

        if (StringUtils.isEmpty(file.getOriginalFilename())) {
            throw new BadRequestException("FileName required");
        }
    }
}