package com.search.teacher.Techlearner.service.course;

import com.search.teacher.Techlearner.dto.course.CourseFilter;
import com.search.teacher.Techlearner.model.entities.Course;
import com.search.teacher.Techlearner.model.entities.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseDbService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int totalCourses(Teacher teacher, CourseFilter courseFilter) {
        String query = "select count(*) from course where teacher_id=" + teacher.getId() + filter(courseFilter, false);
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public List<Course> getAllCourses(Teacher currentTeacher, CourseFilter filter) {
        String query = "select * from course where teacher_id=" + currentTeacher.getId() + filter(filter, true);
        return jdbcTemplate.queryForObject(query, List.class);
    }

    private String filter(CourseFilter courseFilter, boolean isPageable) {
        StringBuilder builder = new StringBuilder();
        if (courseFilter.getCategory() != null && !courseFilter.getCategory().equals("")) {
            builder.append(" and category like '%").append(courseFilter.getCategory()).append("%'");
        }
        if (isPageable) {
            builder
                    .append(" OFFSET ")
                    .append(courseFilter.getPage())
                    .append(" ROW FETCH FIRST ")
                    .append(courseFilter.getSize())
                    .append(" ROW ONLY ");
        }

        builder.append(";");
        return builder.toString();
    }
}
