package com.search.teacher.controller.modules;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.modules.ListeningAnswerDto;
import com.search.teacher.dto.modules.ListeningDto;
import com.search.teacher.dto.modules.PassageConfirmDto;
import com.search.teacher.dto.modules.listening.FileDto;
import com.search.teacher.dto.modules.listening.ListeningUpdateRequest;
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

    @PostMapping("update")
    public JResponse updateListening(@RequestBody ListeningUpdateRequest request) {
        return listeningService.updateListening(securityUtils.getCurrentUser(), request);
    }

    @DeleteMapping("delete/{byId}")
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

    @GetMapping("answers/{id}")
    public JResponse getListeningAnswers(@PathVariable Long id) {
        return listeningService.getAnswerListening(securityUtils.getCurrentUser(), id);
    }

    @DeleteMapping("delete/question/{listeningId}")
    public JResponse deleteListeningQuestion(@PathVariable Long listeningId,
                                             @RequestParam Long questionId,
                                             @RequestParam String type) {
        return listeningService.deleteListeningQuestion(securityUtils.getCurrentUser(), listeningId, questionId, type);
    }

    @PostMapping("confirm")
    public JResponse confirmReadingPassage(@RequestBody PassageConfirmDto confirm) {
        return listeningService.confirmListening(securityUtils.getCurrentUser(), confirm);
    }

    @PostMapping("upload/audio")
    public JResponse uploadAudio(@RequestPart("file") MultipartFile file,
                                 @RequestPart("listeningPart") String listeningPart,
                                 @RequestParam(name = "id", required = false) Integer listeningId) {

        if (!Utils.isAudio(file.getContentType())) {
            throw new BadRequestException("Please upload listening audio file");
        }

        FileDto fileDto = new FileDto();
        fileDto.setListeningId(listeningId);
        fileDto.setFile(file);
        fileDto.setListeningPart(listeningPart);
        return listeningService.uploadAudio(securityUtils.getCurrentUser(), fileDto);
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
