package com.search.teacher.controller;

import com.search.teacher.exception.BadRequestException;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.FileService;
import com.search.teacher.utils.SecurityUtils;
import com.search.teacher.utils.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.service.SecurityService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Package com.search.teacher.controller
 * Created by doston.rakhimov
 * Date: 17/10/24
 * Time: 17:45
 **/
@RestController
@RequestMapping("api/v1/file")
@Api(tags = "FILE API")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final SecurityUtils securityUtils;

    @PostMapping(value = "photo", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiOperation("Uploading Photo file")
    public JResponse uploadPhoto(@RequestPart MultipartFile file,
                                 @RequestPart String type) {
        if (file.isEmpty())
            throw new BadRequestException("File is not empty");

        if (!Utils.isImage(file.getContentType()))
            throw new BadRequestException("Please upload only photo or image");

        return fileService.uploadPhoto(securityUtils.getCurrentUser(), file, type);
    }
}
