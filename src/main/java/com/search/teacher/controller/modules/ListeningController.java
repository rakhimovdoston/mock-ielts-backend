package com.search.teacher.controller.modules;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.modules.ListeningAnswerDto;
import com.search.teacher.dto.modules.ListeningDto;
import com.search.teacher.exception.BadRequestException;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.FileService;
import com.search.teacher.service.modules.ListeningService;
import com.search.teacher.utils.SecurityUtils;
import com.search.teacher.utils.Utils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Package com.search.teacher.controller.modules
 * Created by doston.rakhimov
 * Date: 23/10/24
 * Time: 14:58
 **/
@RestController
@RequestMapping("api/v1/listening")
@Api(tags = "IELTS Listening module API")
@RequiredArgsConstructor
public class ListeningController {

    private final SecurityUtils securityUtils;
    private final ListeningService listeningService;
    private final FileService fileService;

    @PostMapping("save")
    public JResponse saveListening(@RequestBody ListeningDto dto) {

        return listeningService.saveListening(securityUtils.getCurrentUser(), dto);
    }

    @DeleteMapping("delete/listening/{byId}")
    public JResponse deleteListening(@PathVariable Long byId) {
        return listeningService.deleteListening(securityUtils.getCurrentUser(), byId);
    }

    @PostMapping("instructor/{listeningId}/save")
    public JResponse saveListeningInstructor(@PathVariable Long listeningId, @RequestBody ListeningAnswerDto dto) {
        return listeningService.saveQuestionListening(securityUtils.getCurrentUser(), listeningId, dto);
    }

    @GetMapping("byId/{id}")
    public JResponse getListeningById(@PathVariable Long id) {
        return listeningService.getListeningById(securityUtils.getCurrentUser(), id);
    }

    @PostMapping("upload/audio")
    public JResponse uploadAudio(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty() || !Utils.isAudio(file.getContentType())) {
            throw new BadRequestException("Please upload listening audio file");
        }

        return fileService.uploadAudio(securityUtils.getCurrentUser(), file);
    }

    @DeleteMapping("delete/audio/{id}")
    public JResponse deleteAudio(@PathVariable Long id) {
        return fileService.deleteAudioFile(securityUtils.getCurrentUser(), id);
    }

    @GetMapping("audio/{id}")
    public JResponse getListeningAudio(@PathVariable Long id) {
        return fileService.getAudioUrl(securityUtils.getCurrentUser(), id);
    }

    @GetMapping("all")
    public JResponse getAllListening(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                     @RequestParam(name = "size", defaultValue = "10", required = false) int size,
                                     @RequestParam(name = "part", defaultValue = "all", required = false) String partName) {
        ModuleFilter filter = new ModuleFilter();
        filter.setType(partName);
        filter.setPage(page);
        filter.setSize(size);
        return listeningService.getAllListening(securityUtils.getCurrentUser(), filter);
    }
}
