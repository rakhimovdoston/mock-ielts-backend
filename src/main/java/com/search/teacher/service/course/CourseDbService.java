//package com.search.teacher.service.course;
//
//import com.search.teacher.dto.course.CourseFilter;
//import com.search.teacher.model.entities.Course;
//import com.search.teacher.model.entities.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class CourseDbService {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    public int totalCourses(User user, CourseFilter courseFilter) {
//        String query = "select count(*) from course where teacher_id=" + user.getId() + filter(courseFilter, false);
//        return jdbcTemplate.queryForObject(query, Integer.class);
//    }
//
//    public List<Course> getAllCourses(User currentUser, CourseFilter filter) {
//        String query = "select * from course where teacher_id=" + currentUser.getId() + filter(filter, true);
//        return jdbcTemplate.queryForObject(query, List.class);
//    }
//
//    private String filter(CourseFilter courseFilter, boolean isPageable) {
//        StringBuilder builder = new StringBuilder();
//        if (courseFilter.getCategory() != null && !courseFilter.getCategory().equals("")) {
//            builder.append(" and category like '%").append(courseFilter.getCategory()).append("%'");
//        }
//        if (isPageable) {
//            builder
//                    .append(" OFFSET ")
//                    .append(courseFilter.getPage())
//                    .append(" ROW FETCH FIRST ")
//                    .append(courseFilter.getSize())
//                    .append(" ROW ONLY ");
//        }
//
//        builder.append(";");
//        return builder.toString();
//    }
//}
