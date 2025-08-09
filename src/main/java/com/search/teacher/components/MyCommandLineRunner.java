package com.search.teacher.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.teacher.model.entities.*;
import com.search.teacher.model.enums.RoleType;
import com.search.teacher.model.response.AIResponse;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.*;
import com.search.teacher.service.EskizSmsService;
import com.search.teacher.service.ai.AIService;
import com.search.teacher.service.FileUploadService;
import com.search.teacher.service.exam.BookingService;
import com.search.teacher.service.exam.SendAnswerServices;
import com.search.teacher.service.html.HtmlFileService;
import com.search.teacher.service.impl.ExamServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MyCommandLineRunner implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReadingRepository repository;
    private final ExamServiceImpl examService;
    private final ObjectMapper mapper = new ObjectMapper();
    private final FileUploadService fileUploadService;
    private final BookingService bookingService;
    private final AIService aiService;
    private final HtmlFileService htmlFileService;
    private final MockTestExamRepository mockTestExamRepository;
    private final WritingRepository writingRepository;
    private final UserWritingAnswerRepository userWritingAnswerRepository;
    private final EskizSmsService eskizSmsService;
    private final ExamServiceImpl examServiceImpl;
    private final SendAnswerServices sendAnswerServices;

    @Override
    public void run(String... args) throws Exception {
    }

    private void saveRole() {
        insertRole(RoleType.ROLE_TEACHER.name());
        insertRole(RoleType.ROLE_ADMIN.name());
        insertRole(RoleType.ROLE_SUPER_ADMIN.name());
        insertRole(RoleType.ROLE_USER.name());
    }

    private void insertRole(String name) {
        Role role = roleRepository.findByName(name);
        if (role != null)
            return;
        role = new Role();
        role.setName(name);
        roleRepository.save(role);
        logger.info("Role {} saved", name);
    }

    private void saveUser() {
        User user = userRepository.findByUsername("Everestexams");
        if (user != null)
            return;
        user = new User();
        user.setActive(true);
        Role role = roleRepository.findByName(RoleType.ROLE_ADMIN.name());
        user.setEmail("sherzodanorkulovv@gmail.com");
        user.setFirstname("Sherzod");
        user.setLastname("Anorqulov");
        user.getRoles().add(role);
        user.setPassword(passwordEncoder.encode("ExaminationS7090"));
        user.setUsername("Everestexams");
        userRepository.save(user);
    }

    private void testUser() {
        User user = userRepository.findByUsername("testUser");
        if (user != null)
            return;
        user = new User();
        user.setActive(true);
        Role role = roleRepository.findByName(RoleType.ROLE_USER.name());
        user.setUsername("testUser");
        user.setEmail("email@gmail.com");
        user.setFirstname("Test");
        user.setLastname("User");
        user.setPassword(passwordEncoder.encode("test123456"));
        user.getRoles().add(role);
        userRepository.save(user);
        logger.info("User {} saved", user.getUsername());
    }
}
