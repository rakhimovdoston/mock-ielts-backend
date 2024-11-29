package com.search.teacher.service;

import com.search.teacher.config.ApplicationProperties;
import com.search.teacher.dto.ImageDto;
import com.search.teacher.model.entities.Image;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.enums.ImageType;
import com.search.teacher.repository.ImageRepository;
import com.search.teacher.utils.Utils;
import io.minio.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service
public class FileUploadService {
    private final Logger log = LoggerFactory.getLogger(FileUploadService.class);
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

    public ImageDto fileUpload(User currentUser, MultipartFile file, String type, boolean pubLic) {
        ImageType imageType = ImageType.valueOf(type);
        Image image = uploadToStorageServer(file, getBucket(imageType), pubLic);
        image.setImageType(imageType);
        image.setUserId(currentUser.getId());
        imageRepository.save(image);
        return new ImageDto(image.getId(), image.getUrl());
    }

    private String getBucket(ImageType imageType) {
        return switch (imageType) {
            case LOGO -> "logos";
            case PROFILE_PICTURE -> "profiles";
            case READING, LISTENING, WRITING, SPEAKING -> "modules";
            default -> "photos";
        };
    }


    public Image uploadToStorageServer(MultipartFile file, String bucket, boolean isPublic) {
        final String originalFilename = file.getOriginalFilename();
        final String fileName = getObjectName(file.getOriginalFilename());
        final String contentType = file.getContentType();

        log.info("Filename: {} -------------------------------------", originalFilename);
        log.info("objectName: {} -------------------------------------", fileName);
        log.info("contentType: {} -------------------------------------", contentType);
        try {
            byte[] bytes = file.getBytes();
            ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            checkBucket(bucket, isPublic);
            this.uploadWithPutObject(
                PutObjectArgs
                    .builder()
                    .bucket(bucket)
                    .object(fileName)
                    .stream(stream, bytes.length, -1)
                    .contentType(file.getContentType())
                    .build()
            );

            Image image = new Image();
            image.setOriginalFilename(originalFilename);
            image.setContentType(contentType);
            image.setSize(file.getSize());
            image.setUrl(StringUtils.join(applicationProperties.getMinio().getHost(), "/", bucket, "/", fileName));
            image.setObjectName(fileName);
            stream.close();
            return image;

        } catch (Exception e) {
            log.error("File Uploaded error: {}", e.getMessage());
            throw new RuntimeException("File upload failed");
        }
    }

    private void checkBucket(String bucket, boolean isPublic) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                minioClient.makeBucket(MakeBucketArgs
                    .builder()
                    .bucket(bucket)
                    .build());

                if (isPublic) {
                    String publicPolicy = "{\n" +
                        "  \"Version\"::" + Utils.getCurrentDateStandardFormat() + ",\n" +
                        "  \"Statement\": [\n" +
                        "    {\n" +
                        "      \"Effect\": \"Allow\",\n" +
                        "      \"Principal\": \"*\",\n" +
                        "      \"Action\": [\n" +
                        "        \"s3:GetObject\"\n" +
                        "      ],\n" +
                        "      \"Resource\": [\n" +
                        "        \"arn:aws:s3:::" + bucket + "/*\"\n" +
                        "      ]\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}";

                    minioClient.setBucketPolicy(SetBucketPolicyArgs
                        .builder()
                        .bucket(bucket)
                        .config(publicPolicy)
                        .build());

                }
            }
        } catch (ErrorResponseException |
                 InsufficientDataException |
                 InternalException |
                 InvalidKeyException |
                 InvalidResponseException |
                 IOException |
                 NoSuchAlgorithmException |
                 ServerException |
                 XmlParserException e) {
            log.error("Creating or setting public policy bucket error: {}", e.getMessage());
            throw new RuntimeException("Creating or Checking bucket failed");
        }
    }

    private void uploadWithPutObject(PutObjectArgs objectArgs) {
        try {
            ObjectWriteResponse response = minioClient.putObject(objectArgs);

            String builder = "\nObject: --------   " + response.object() +
                "\nVersion Id: --------   " + response.versionId() +
                "\nEtag: --------   " + response.etag();
            log.info(builder);
            log.info("Uploaded successfully");

        } catch (Exception e) {
            log.error("Error upload file: {}", e.getMessage());
            throw new RuntimeException("Error upload file");
        }
    }

    private String getObjectName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        final String extension = FilenameUtils.getExtension(originalFilename);
        final String filename = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String newFilename = StringUtils.join(uuid, filename, '.', extension);
        if (newFilename.length() >= 150) {
            newFilename = newFilename.substring(0, 130);
            newFilename = StringUtils.join(newFilename, '.', extension);
        }

        return newFilename;
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
}
