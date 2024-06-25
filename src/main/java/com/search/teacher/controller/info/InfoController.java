package com.search.teacher.controller.info;

import com.search.teacher.model.response.JResponse;
import com.search.teacher.service.user.StudentService;
import com.search.teacher.service.user.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/v1/info")
@RequiredArgsConstructor
public class InfoController {

    private final StudentService studentService;

    @GetMapping("description")
    public JResponse infoDescription() {
        Map<String, Object> map = studentService.infoDescription();
        return JResponse.success(map);
    }

//    @GetMapping("topic-info")
//    public JResponse getTopics() {
//        return teacherService.allTopics();
//    }
//
//    @GetMapping("degree")
//    public JResponse getDegree() {
//        return teacherService.allDegrees();
//    }
}
