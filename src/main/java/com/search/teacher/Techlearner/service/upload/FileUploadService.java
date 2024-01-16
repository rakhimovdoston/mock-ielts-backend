package com.search.teacher.Techlearner.service.upload;

import com.search.teacher.Techlearner.model.entities.Images;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AwsClientService awsClientService;

    public String uploadImage(Images image, MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            originalFilename = UUID.randomUUID().toString();
        }
        originalFilename = originalFilename.replaceAll(" ", "-");
        String filename = UUID.randomUUID() + "-" + originalFilename;
        String url = awsClientService.awsUpload(filename, file);
        logger.info("Uploaded file to AWS: {}", filename);
        image.setFilename(filename);
        image.setUrl(url);
        return filename;
    }
}
