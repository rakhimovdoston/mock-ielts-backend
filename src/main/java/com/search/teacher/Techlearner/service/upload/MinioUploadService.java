package com.search.teacher.Techlearner.service.upload;

import com.search.teacher.Techlearner.exception.BadRequestException;
import com.search.teacher.Techlearner.model.entities.Images;
import com.search.teacher.Techlearner.model.enums.ImageType;
import com.search.teacher.Techlearner.utils.Utils;
import io.minio.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MinioUploadService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${minio.url}")
    private String serverUrl;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;

    private MinioClient minioClient;

    @PostConstruct
    private void init() {
        minioClient = MinioClient.builder()
                .endpoint(serverUrl)
                .credentials(accessKey, secretKey)
                .build();
    }

    public String upload(MultipartFile file, String filename, ImageType imageType) {
        String bucketName = Utils.getBucketWithType(imageType);
        boolean found = checkBucket(bucketName);
        if (!found) {
            return null;
        }
        try {
            PutObjectArgs args = PutObjectArgs.builder()
                    .contentType(file.getContentType())
                    .bucket(bucketName)
                    .object(filename)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build();
            ObjectWriteResponse response = minioClient.putObject(args);
            return response.etag();
        } catch (Exception e) {
            logger.error("Upload image failed: {}", e.getMessage());
        }
        return null;
    }

    public byte[] downloadImage(Images images) {
        try {
            GetObjectArgs download = GetObjectArgs
                    .builder()
                    .bucket(images.getBucket())
                    .object(images.getFilename())
                    .build();
            GetObjectResponse response = minioClient.getObject(download);
            return response.readAllBytes();
        } catch (Exception e) {
            throw new BadRequestException("Download image failed");
        }
    }

    private boolean checkBucket(String bucketName) {
        try {
            boolean isExistBucket = minioClient.bucketExists(BucketExistsArgs
                    .builder()
                    .bucket(bucketName)
                    .build());

            if (!isExistBucket) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            return true;

        } catch (Exception e) {
            logger.error("Error in check bucket", e);
            return false;
        }
    }
}
