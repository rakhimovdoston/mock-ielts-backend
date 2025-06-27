package com.search.teacher.controller.ielts;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.request.module.WritingRequest;
import com.search.teacher.exception.BadRequestException;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.module.WritingService;
import com.search.teacher.utils.SecurityUtils;
import com.search.teacher.utils.Utils;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/writing")
public class WritingController {
    private final WritingService writingService;
    private final SecurityUtils securityUtils;

    public WritingController(WritingService writingService, SecurityUtils securityUtils) {
        this.writingService = writingService;
        this.securityUtils = securityUtils;
    }

    @PostMapping("save")
    public JResponse saveWriting(@RequestBody WritingRequest request) {
        if (request.task() && !StringUtils.hasText(request.image()))
            return JResponse.error(400, "Please upload image");

        return writingService.saveWriting(securityUtils.getCurrentUser(), request);
    }

    @PutMapping("update/{byId}")
    public JResponse updateWriting(@PathVariable Long byId, @RequestBody WritingRequest request) {
        if (request.task() && !StringUtils.hasText(request.image()))
            return JResponse.error(400, "Please upload image");

        return writingService.updateWriting(securityUtils.getCurrentUser(), byId, request);
    }

    @PostMapping(value = "photo/{byId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiOperation("Uploading Photo file")
    public JResponse uploadPhoto(@PathVariable Long byId,
                                 @RequestPart MultipartFile file,
                                 @RequestPart String type) {
        if (file.isEmpty())
            throw new BadRequestException("File is not empty");

        if (!Utils.isImage(file.getContentType()))
            throw new BadRequestException("Please upload only photo or image");

        return writingService.uploadPhoto(securityUtils.getCurrentUser(), byId, file, type);
    }

    @GetMapping("get/{id}")
    public JResponse getWriting(@PathVariable(name = "id") Long id) {
        return writingService.getWritingById(securityUtils.getCurrentUser(), id);
    }

    @DeleteMapping("delete/{byId}")
    public JResponse deleteListening(@PathVariable(name = "byId") Long listeningId) {
        return writingService.deleteWriting(securityUtils.getCurrentUser(), listeningId);
    }

    @GetMapping("all")
    public JResponse getAllWriting(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                   @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                   @RequestParam(name = "type", required = false, defaultValue = "all") String type) {
        ModuleFilter filter = new ModuleFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setType(type);
        return writingService.getAllWritings(securityUtils.getCurrentUser(), filter);
    }
}
