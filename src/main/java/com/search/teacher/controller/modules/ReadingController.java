package com.search.teacher.controller.modules;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.dto.modules.ReadingDto;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.modules.ModuleService;
import com.search.teacher.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Package com.search.teacher.controller.modules
 * Created by doston.rakhimov
 * Date: 21/06/24
 * Time: 15:05
 **/
@RestController
@RequestMapping("api/v1/reading")
@RequiredArgsConstructor
public class ReadingController {

    private final ModuleService moduleService;
    private final SecurityUtils securityUtils;

    @PostMapping("save")
    public ResponseEntity<JResponse> createReading(@RequestBody ReadingDto reading) {

//        if (reading.getAnswers().isEmpty())
//            return ResponseEntity.badRequest().body(JResponse.error(400, "Please enter your reading answers"));

        JResponse response = moduleService.saveReading(securityUtils.getCurrentUser(), reading);
        return ResponseEntity.ok(response);
    }

    @GetMapping("all")
    public ResponseEntity<JResponse> getAllModule(@ParameterObject ModuleFilter filter) {
        JResponse response = moduleService.getAllModules(securityUtils.getCurrentUser(), filter);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getCode()));
    }

    @GetMapping("byId/{id}")
    public JResponse getReadingById(@PathVariable(name = "id") Long id) {
        return moduleService.getReadingById(securityUtils.getCurrentUser(), id);
    }

}
