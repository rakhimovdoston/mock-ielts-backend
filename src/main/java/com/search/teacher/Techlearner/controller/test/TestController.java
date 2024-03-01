package com.search.teacher.Techlearner.controller.test;

import com.search.teacher.Techlearner.model.response.JResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("")
    public JResponse getAllUsers(@RequestParam(name = "count", required = false, defaultValue = "10") int count,
                                 @RequestParam(name = "gender", required = false, defaultValue = "all") String gender,
                                 @RequestParam(name = "seed", required = false) String seed,
                                 @RequestParam(name = "format", required = false) String format) {
        return testService.getAllUsers(new ReqParam(count, gender, seed, format));
    }



}
