package com.search.teacher.Techlearner.controller.test;

import com.search.teacher.Techlearner.controller.test.model.Person;
import com.search.teacher.Techlearner.controller.test.model.RandomUserResponse;
import com.search.teacher.Techlearner.model.entities.Images;
import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.model.enums.ImageType;
import com.search.teacher.Techlearner.model.enums.RoleType;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.repository.ImageRepository;
import com.search.teacher.Techlearner.repository.RoleRepository;
import com.search.teacher.Techlearner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RestTestClientService restTestClientService;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public JResponse getAllUsers(ReqParam reqParam) {

        RandomUserResponse response = restTestClientService.getAllUsers(reqParam);
        if (response.isError())
            return JResponse.error(400, response.getError());

        List<Person> personList = response.getResults();
        StringBuilder userIds = new StringBuilder();
        for (Person person: personList) {
            User user = new User();
            user.setFirstname(person.getName().getFirst());
            user.setLastname(person.getName().getLast());
            user.setEmail(person.getEmail());
            user.setPassword(passwordEncoder.encode(person.getLogin().getPassword()));
            user.setRole(roleRepository.findByName(RoleType.ROLE_STUDENT));
            userRepository.save(user);
            Images images = new Images();
            images.setUser(user);
            images.setType(ImageType.PROFILE_PICTURE);
            images.setUrl(person.getPicture().getLarge());
            imageRepository.save(images);
            userIds.append(user.getId()).append(",");
        }

        logger.info("Users Ids: {}", userIds);

        return JResponse.success(response);
    }
}
