package com.search.teacher.Techlearner.controller.course;

import com.search.teacher.Techlearner.dto.course.CourseDto;
import com.search.teacher.Techlearner.dto.course.CourseFilter;
import com.search.teacher.Techlearner.dto.course.LessonDto;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.service.course.CourseService;
import com.search.teacher.Techlearner.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/course/")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final SecurityUtils securityUtils;

    @GetMapping("all")
    public JResponse getAllCourses(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        CourseFilter filter = new CourseFilter();
        filter.setCategory(category);
        filter.setPage(page);
        filter.setSize(size);
        return courseService.getAllCourses(securityUtils.getCurrentTeacher(), filter);
    }

    @PostMapping("add")
    public JResponse addCourse(@RequestBody CourseDto course) {
        return courseService.addCourse(securityUtils.getCurrentTeacher(), course);
    }

    @PutMapping("update/{id}")
    public JResponse updateCourse(@PathVariable Long id, @RequestBody CourseDto course) {
        return courseService.updateCourse(id, securityUtils.getCurrentTeacher(), course);
    }

    @PostMapping("{courseId}/lessons/add")
    public JResponse addLesson(@PathVariable(name = "courseId") Long courseId, @RequestBody LessonDto lesson) {
        return courseService.addLessonToCourse(courseId, securityUtils.getCurrentTeacher(), lesson);
    }

    @PutMapping("{courseId}/lesson/update/{lessonId}")
    public JResponse updateCourseLesson(@PathVariable(name = "courseId") Long courseId, @PathVariable(name = "lessonId") Long lessonId, @RequestBody LessonDto lesson) {
        return courseService.updateLesson(courseId, lessonId, securityUtils.getCurrentTeacher(), lesson);
    }

    @DeleteMapping("{courseId}/lessons/delete/{lessonId}")
    public JResponse deleteLesson(@PathVariable(name = "courseId") Long courseId, @PathVariable(name = "lessonId") Long lessonId) {
        return courseService.deleteLesson(courseId, lessonId, securityUtils.getCurrentTeacher());
    }
}
