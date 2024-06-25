//package com.search.teacher.service.course;
//
//import com.search.teacher.dto.course.CourseDto;
//import com.search.teacher.dto.course.CourseFilter;
//import com.search.teacher.dto.course.CourseResponse;
//import com.search.teacher.dto.course.LessonDto;
//import com.search.teacher.dto.filter.PaginationResponse;
//import com.search.teacher.dto.response.SaveResponse;
//import com.search.teacher.mapper.TeacherMapper;
//import com.search.teacher.model.entities.Category;
//import com.search.teacher.model.entities.Course;
//import com.search.teacher.model.entities.Lesson;
//import com.search.teacher.model.entities.User;
//import com.search.teacher.model.response.JResponse;
//import com.search.teacher.repository.CategoryRepository;
//import com.search.teacher.repository.CourseRepository;
//import com.search.teacher.repository.LessonRepository;
//import com.search.teacher.utils.ResponseMessage;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class CourseService {
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    private final CourseRepository courseRepository;
//    private final CategoryRepository categoryRepository;
//    private final LessonRepository lessonRepository;
//    private final CourseDbService courseDbService;
//    private final TeacherMapper teacherMapper;
//
//    @Transactional(readOnly = true)
//    public JResponse getAllCourses(User currentUser, CourseFilter filter) {
//        int total = courseDbService.totalCourses(currentUser, filter);
//        List<Course> courses = courseDbService.getAllCourses(currentUser, filter);
//        if (courses.isEmpty())
//            return JResponse.error(404, "Your have not a course");
//        List<CourseResponse> courseResponses = getCourseResponses(courses);
//        PaginationResponse response = new PaginationResponse();
//        response.setCurrentSize(filter.getSize());
//        response.setCurrentPage(filter.getPage());
//        response.setData(courseResponses);
//        response.setTotalSizes(total);
//        response.setTotalPages(total / filter.getSize() + 1);
//        return JResponse.success(response);
//    }
//
//    private static List<CourseResponse> getCourseResponses(List<Course> courses) {
//        List<CourseResponse> courseResponses = new ArrayList<>();
//        for (Course course : courses) {
//            CourseResponse courseResponse = new CourseResponse();
//            courseResponse.setId(course.getId());
//            courseResponse.setDescription(course.getDescription());
//            courseResponse.setTitle(course.getTitle());
//            courseResponse.setCategory(course.getCategory().getName());
//            courseResponse.setPrice(course.getPrice());
//            courseResponse.setDiscount(course.getDiscount());
//            courseResponses.add(courseResponse);
//        }
//        return courseResponses;
//    }
//
//    @Transactional
//    public JResponse addCourse(User currentTeacher, CourseDto dto) {
//        Optional<Category> category = categoryRepository.findById(dto.getCategoryId());
//        if (category.isEmpty()) {
//            return JResponse.error(400, ResponseMessage.CATEGORY_NOT_FOUND);
//        }
//        Course course = new Course();
//        course.setTitle(dto.getTitle());
//        course.setDescription(dto.getDescription());
//        course.setImg(dto.getImg());
//        course.setPrice(dto.getPrice());
//        course.setDiscount(dto.getDiscount() == null ? 0 : dto.getDiscount());
//        course.setLessons(new ArrayList<>());
////        course.setTeacher(currentTeacher);
//        courseRepository.save(course);
//        if (!dto.getLessons().isEmpty()) {
//            setCourse(course, dto.getLessons());
//            courseRepository.save(course);
//        }
//
//        return JResponse.success(new SaveResponse(course.getId()));
//    }
//
//    @Transactional
//    public JResponse updateCourse(Long id, User currentTeacher, CourseDto dto) {
//        Course course = courseRepository.findByTeacherAndId(currentTeacher, id);
//        if (course == null) {
//            return JResponse.error(400, ResponseMessage.COURSE_NOT_FOUND);
//        }
//
//        course.setTitle(dto.getTitle());
//        course.setDescription(dto.getDescription());
//        course.setImg(dto.getImg());
//        course.setPrice(dto.getPrice());
//        course.setDiscount(dto.getDiscount() == null ? 0 : dto.getDiscount());
//        courseRepository.save(course);
//        return JResponse.success(ResponseMessage.OPERATION_SUCCESSFUL);
//    }
//
//    @Transactional
//    public JResponse addLessonToCourse(Long courseId, Teacher currentTeacher, LessonDto dto) {
//
//        JResponse response = checkLessonNull(dto);
//        if (response.isError()) {
//            return response;
//        }
//        Course course = courseRepository.findByTeacherAndId(currentTeacher, courseId);
//        if (course == null) {
//            return JResponse.error(400, ResponseMessage.COURSE_NOT_FOUND);
//        }
//
//        Lesson lesson = new Lesson();
//        lesson.setTitle(dto.getTitle());
//        lesson.setImage(dto.getImage());
//        lesson.setDescription(dto.getDescription());
//        lesson.setCourse(course);
//        lessonRepository.save(lesson);
//        return JResponse.success(ResponseMessage.OPERATION_SUCCESSFUL);
//    }
//
//    @Transactional
//    public JResponse updateLesson(Long courseId, Long lessonId, Teacher currentTeacher, LessonDto dto) {
//        JResponse response = checkLessonNull(dto);
//        if (response.isError()) {
//            return response;
//        }
//        Course course = courseRepository.findByTeacherAndId(currentTeacher, courseId);
//        if (course == null) {
//            return JResponse.error(400, ResponseMessage.COURSE_NOT_FOUND);
//        }
//        Lesson lesson = lessonRepository.findByCourseAndId(course, lessonId);
//        if (lesson == null) {
//            return JResponse.error(400, ResponseMessage.LESSON_NOT_FOUND);
//        }
//
//        lesson.setTitle(dto.getTitle());
//        lesson.setImage(dto.getImage());
//        lesson.setDescription(dto.getDescription());
//        lessonRepository.save(lesson);
//        return JResponse.success(ResponseMessage.OPERATION_SUCCESSFUL);
//    }
//
//
//    public JResponse deleteLesson(Long courseId, Long lessonId, Teacher currentTeacher) {
//        Course course = courseRepository.findByTeacherAndId(currentTeacher, courseId);
//        if (course == null) {
//            return JResponse.error(400, ResponseMessage.COURSE_NOT_FOUND);
//        }
//        Lesson lesson = lessonRepository.findByCourseAndId(course, lessonId);
//        if (lesson == null) {
//            return JResponse.error(400, ResponseMessage.LESSON_NOT_FOUND);
//        }
//        lessonRepository.delete(lesson);
//        return JResponse.success(ResponseMessage.OPERATION_SUCCESSFUL);
//    }
//
//    private void setCourse(Course course, List<LessonDto> lessons) {
//        for (LessonDto dto : lessons) {
//            JResponse response = checkLessonNull(dto);
//            if (response.isError()) {
//                return;
//            }
//            Lesson lesson = new Lesson();
//            lesson.setCourse(course);
//            lesson.setImage(dto.getImage());
//            lesson.setTitle(dto.getTitle());
//            lesson.setDescription(dto.getDescription());
//            course.getLessons().add(lesson);
//        }
//    }
//
//    private JResponse checkLessonNull(LessonDto dto) {
//        if (dto.getTitle() == null) {
//            return JResponse.error(400, "Lesson title is required, Please fill this field");
//        }
//
//        if (dto.getDescription() == null) {
//            return JResponse.error(400, "Lesson description is required, Please fill this field");
//        }
//
//        return JResponse.success();
//    }
//}
