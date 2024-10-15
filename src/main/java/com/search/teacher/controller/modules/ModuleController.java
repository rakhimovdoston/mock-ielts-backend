package com.search.teacher.controller.modules;

import com.search.teacher.dto.filter.ModuleFilter;
import com.search.teacher.model.enums.ModuleType;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.modules.ModuleService;
import com.search.teacher.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Package com.search.teacher.controller.modules
 * Created by doston.rakhimov
 * Date: 25/06/24
 * Time: 10:00
 **/
@RestController
@RequestMapping("api/v1/module")
@RequiredArgsConstructor
public class ModuleController {

    private final SecurityUtils securityUtils;
    private final ModuleService moduleService;

    @GetMapping("all")
    public ResponseEntity<JResponse> getAllModule(@ParameterObject ModuleFilter filter) {
        JResponse response = moduleService.getAllModules(securityUtils.getCurrentUser(), filter);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getCode()));
    }

    @GetMapping("question-types")
    public ResponseEntity<JResponse> getQuestionTypes(@RequestParam(name = "module_type") ModuleType moduleType) {
        return ResponseEntity.ok(moduleService.getQuestionTypes(moduleType));
    }
}
