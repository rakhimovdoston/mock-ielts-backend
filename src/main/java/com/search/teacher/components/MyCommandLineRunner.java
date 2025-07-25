package com.search.teacher.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.teacher.model.entities.Role;
import com.search.teacher.model.entities.User;
import com.search.teacher.model.enums.RoleType;
import com.search.teacher.repository.ReadingRepository;
import com.search.teacher.repository.RoleRepository;
import com.search.teacher.repository.UserRepository;
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

    @Override
    public void run(String... args) throws Exception {
//        JsonNode rootNode = mapper.readTree(json);
//        JsonNode answers = rootNode.get("answers");
//        JsonNode userAnswers = rootNode.get("userAnswers");
//        List<ModuleAnswer> moduleAnswers = mapper.readValue(answers.toString(), mapper.getTypeFactory().constructCollectionType(List.class, ModuleAnswer.class));
//        List<UserAnswers> userAnswersList = mapper.readValue(userAnswers.toString(), mapper.getTypeFactory().constructCollectionType(List.class, UserAnswers.class));
//        int count = examService.countAnswer(moduleAnswers, userAnswersList);
//        logger.info("Count: {}", count);
//        testUser();
    }

    private void enterClientUser() {
        User user = userRepository.findByUsername("everbest");
        if (user != null)
            return;
        user = new User();
        user.setActive(true);
        Role role = roleRepository.findByName(RoleType.ROLE_ADMIN.name());
        user.setUsername("everbest");
        user.setEmail("everbest@gmail.com");
        user.setFirstname("Everbest");
        user.setLastname("");
        user.setPassword(passwordEncoder.encode("ever1199best"));
        user.getRoles().add(role);
        userRepository.save(user);
        logger.info("User {} saved", user.getUsername());
    }

    private void saveRole() {
        insertRole(RoleType.ROLE_TEACHER.name());
        insertRole(RoleType.ROLE_ADMIN.name());
        insertRole(RoleType.ROLE_SUPER_ADMIN.name());
        insertRole(RoleType.ROLE_USER.name());
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
        User user = userRepository.findByUsername("doston");
        if (user != null)
            return;
        user = new User();
        user.setActive(true);
        Role role = roleRepository.findByName(RoleType.ROLE_ADMIN.name());
        user.setEmail("rdoston22@gmail.com");
        user.setFirstname("Doston");
        user.setLastname("Rakhimov");
        user.getRoles().add(role);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setUsername("doston");
        userRepository.save(user);
    }
}
