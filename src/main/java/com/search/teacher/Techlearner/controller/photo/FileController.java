package com.search.teacher.Techlearner.controller.photo;

import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.service.upload.FileService;
import com.search.teacher.Techlearner.utils.FileUtils;
import com.search.teacher.Techlearner.utils.ResponseMessage;
import com.search.teacher.Techlearner.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/file/")
@Tag(name = "File Service")
public class FileController {
    private final FileService fileService;
    private final SecurityUtils securityUtils;

    public FileController(FileService fileService, SecurityUtils securityUtils) {
        this.fileService = fileService;
        this.securityUtils = securityUtils;
    }

    @PostMapping(value = "photo/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JResponse> uploadImage(@RequestParam("photo") MultipartFile file,
                                      @RequestParam("type") String type) {
        if (!FileUtils.isCheckContent(file.getContentType(), FileUtils.PHOTO_CONTENTS)) {
            return ResponseEntity
                    .badRequest()
                    .body(JResponse.error(400, FileUtils.errorMessage(ResponseMessage.CONTENT_TYPE_INVALID, FileUtils.PHOTO_CONTENTS)));
        }
        return ResponseEntity.ok(fileService.uploadImage(securityUtils.currentUser(), type, file));
    }

    @GetMapping("photo/download")
    public byte[] downloadImage(@RequestParam("filename") String filename) {
        return fileService.downloadImage(securityUtils.currentUser(), filename);
    }

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        if (!FileUtils.isCheckContent(file.getContentType(), FileUtils.PDF_CONTENTS)) {
            return ResponseEntity
                    .badRequest()
                    .body(JResponse.error(400, FileUtils.errorMessage(ResponseMessage.CONTENT_TYPE_INVALID, FileUtils.PDF_CONTENTS)));
        }
        return ResponseEntity.ok(fileService.uploadFile(file));
    }
}
