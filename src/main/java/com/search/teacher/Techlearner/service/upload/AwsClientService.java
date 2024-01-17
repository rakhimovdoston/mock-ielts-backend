package com.search.teacher.Techlearner.service.upload;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AwsClientService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private AmazonS3 amazonS3;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initAmazonService() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("us-east-1")
                .build();
    }

    public String awsUpload(String filename, MultipartFile file) {
        String response = null;
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());
            amazonS3.putObject(bucketName, "profile/" + filename, file.getInputStream(), objectMetadata);
            response = amazonS3.getUrl(bucketName, "profile/" + filename).toExternalForm();
            logger.info("File uploaded in aws s3");
        } catch (IOException e) {
            logger.error("Image Upload Failed: {}",  e.getMessage());
        }
        return response;
    }

    public String getImage(String filename) {
        S3Object object = amazonS3.getObject(bucketName, filename);
        return "";
    }
}
