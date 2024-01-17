package com.search.teacher.Techlearner.controller.news;

import com.search.teacher.Techlearner.model.response.JResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

    @PostMapping
    public JResponse getAllNews() {
        return JResponse.success();
    }
}
