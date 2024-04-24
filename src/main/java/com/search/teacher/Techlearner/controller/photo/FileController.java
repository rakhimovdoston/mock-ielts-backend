package com.search.teacher.Techlearner.controller.photo;

import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.service.upload.ImageService;
import com.search.teacher.Techlearner.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/file/")
@Tag(name = "File Service")
public class FileController {
    private final ImageService imageService;
    private final SecurityUtils securityUtils;

    public FileController(ImageService imageService, SecurityUtils securityUtils) {
        this.imageService = imageService;
        this.securityUtils = securityUtils;
    }

    @PostMapping(value = "image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public JResponse uploadImage(@RequestParam("file") MultipartFile file,
                                 @RequestParam("type") String type) {
        return imageService.uploadImage(securityUtils.currentUser(), type, file);
    }

    @GetMapping("image/download")
    public byte[] downloadImage(@RequestParam("filename") String filename) {
        return imageService.downloadImage(securityUtils.currentUser(), filename);
    }
}
