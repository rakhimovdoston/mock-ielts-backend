package com.search.teacher.controller.modules;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.modules.writing.WritingDto;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.modules.WritingService;
import com.search.teacher.utils.SecurityUtils;
import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Package com.search.teacher.controller.modules
 * Created by doston.rakhimov
 * Date: 22/10/24
 * Time: 17:45
 **/
@RestController
@RequestMapping("api/v1/writing")
@Api(tags = "Writing Module API")
@RequiredArgsConstructor
public class WritingController {

    private final WritingService writingService;
    private final SecurityUtils securityUtils;

    @PostMapping("save")
    public JResponse saveWriting(@RequestBody @Valid WritingDto writingDto) {
        return writingService.saveWritingContent(securityUtils.getCurrentUser(), writingDto);
    }

    @PutMapping("update")
    public JResponse updateWriting(@RequestBody @Valid WritingDto writingDto) {
        return writingService.updateWriting(securityUtils.getCurrentUser(), writingDto);
    }

    @GetMapping("get/{id}")
    public JResponse getWritingById(@PathVariable("id") Long id) {
        return writingService.getWritingModule(securityUtils.getCurrentUser(), id);
    }

    @DeleteMapping("delete/{byId}")
    public JResponse deleteWriting(@PathVariable("byId") Long byId) {
        return writingService.deleteWritingModule(securityUtils.getCurrentUser(), byId);
    }

    @GetMapping("all")
    public JResponse getAllWriting(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                   @RequestParam(name = "size", defaultValue = "10") Integer size,
                                   @RequestParam(name = "name", defaultValue = "all") String type) {
        ModuleFilter filter = new ModuleFilter();
        filter.setPage(page);
        filter.setSize(size);
        filter.setType(type);
        return writingService.getAllWriting(securityUtils.getCurrentUser(), filter);
    }
}
