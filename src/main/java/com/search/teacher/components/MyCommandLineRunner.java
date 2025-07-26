package com.search.teacher.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.teacher.model.entities.*;
import com.search.teacher.model.enums.RoleType;
import com.search.teacher.model.response.AIResponse;
import com.search.teacher.repository.ReadingRepository;
import com.search.teacher.repository.RoleRepository;
import com.search.teacher.repository.UserRepository;
import com.search.teacher.service.ai.AIService;
import com.search.teacher.service.FileUploadService;
import com.search.teacher.service.exam.BookingService;
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

    @Override
    public void run(String... args) throws Exception {
//
//        String taskOne = "The table and chart provide a breakdown of the total expenditure and the average amount of money spent by students per week while studying abroad in four countries.  \n" +
//                "\n" +
//                "Summarise the information by selecting and reporting the main features, and make comparisons where relevant. ";
//        String image = "https://cdn.ieltsmockexam.uz/modules/eb59eef4-0b5a-480a-b772-383968ec23cepng.png";
//        String taskOneUserAnswer = "The provided table and chart illustrate the total weekly expenditure and the breakdown of average weekly spending (accommodation, tuition, and living costs) for students studying abroad in four different countries: A, B, C, and D.\n" +
//                "\n" +
//                "Overall, Country A is the most expensive destination for students, both in terms of total weekly expenditure and individual cost categories, while Country D is consistently the most affordable. Accommodation and tuition fees generally constitute the largest portions of student spending across all countries.\n" +
//                "\n" +
//                "Country A has the highest total weekly expenditure at $875. Country B follows with $725, while Country C is significantly lower at $540. Country D is the least expensive, with a total weekly expenditure of $450.\n" +
//                "\n" +
//                "Accommodation costs are highest in Country A at $350 per week. Country B has the second-highest at $300. Countries C and D have relatively lower accommodation costs, at $280 and $235 respectively. Country A also has the highest tuition fees, at $350 per week. Country B's tuition is considerably lower at $100, which is the same as Country C. Country D has slightly higher tuition at $120. Living costs show a varied pattern. Country A has the highest living costs at $175. Country B's living costs are $225, placing it second highest. Countries C and D have the lowest living costs at $160 and $95 respectively, indicating that living expenses in Country D are particularly low compared to other categories.\n" +
//                "\n" +
//                "Country A consistently ranks as the most expensive in all three categories and overall expenditure. Notably, its tuition fees are significantly higher than the other three countries. Conversely, Country D offers the lowest overall costs, driven largely by its comparatively low living expenses and accommodation. Countries B and C fall in the middle, with Country B being more expensive than Country C across all categories except tuition, where they are equal.\n";
//
//
//        String taskTwo = "Nowadays, more people move away from their friends and families for work.  \n" +
//                "\n" +
//                "Do the advantages outweigh the disadvantages?";
//        String taskTwoUserAnswer = "Some people think that old people should not stop from working after their retirement age if they are able to do so. While this idea might sound good in order for the elderly not become isolated from the society and share their experience to others, I think it might pose some challenges to their younger counterparts. \n" +
//                "\n" +
//                "Continuing to work after growing old can offer several advantages to the old. First of them is the chance to socialize with people and be away from loneliness. Old people often become isolated from the world when they retire. They tend to live away from their family in this fast-paced world, as a result of which they might become bored and stressed. In the cases of being left without care, it sounds sensible for them to engage with their work they are accustomed to. This allows those senior workers to enjoy their lives with full of people around them and feel important as usual. In addition to such enjoyment, old people can share valuable insights about productive working to newcomers, which can be helpful for both sides. \n" +
//                "\n" +
//                "In my opinion, however, the idea of old people still working can have a threat for young people. Since old people are likely to be skillful and experienced enough to work effectively, they can easily surpass the youth who have just started their jobs. A fierce competition emerges, leading many young people lose their jobs, which is negative for the future. When people finish their jobs at certain ages, such as 60 or 65, they will probably have already contributed a lot to their employed companies, so it is youngsters' time to step in. Therefore, old people have every right to take a rest and be looked after by their younger family members. \n" +
//                "\n" +
//                "In conclusion, old people may deal with isolation by working and pass their experience to the next generation, I firmly believe that it is better for them to leave the rest to young people after retirement age, instead of occupying their positions and making them jobless. ";
//
//        AIResponse<JsonNode> answer = aiService.getOverallReply(taskOne, taskTwo, taskOneUserAnswer, taskTwoUserAnswer, image);
//        logger.info("AI Answers: {}", answer.getData());
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
