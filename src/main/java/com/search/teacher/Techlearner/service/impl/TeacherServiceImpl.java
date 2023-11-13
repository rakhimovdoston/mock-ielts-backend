package com.search.teacher.Techlearner.service.impl;

import com.search.teacher.Techlearner.dto.request.TeacherRequest;
import com.search.teacher.Techlearner.dto.response.DescribeDto;
import com.search.teacher.Techlearner.dto.response.SaveResponse;
import com.search.teacher.Techlearner.dto.response.TeacherResponse;
import com.search.teacher.Techlearner.exception.NotfoundException;
import com.search.teacher.Techlearner.mapper.TeacherMapper;
import com.search.teacher.Techlearner.mapper.TopicMapper;
import com.search.teacher.Techlearner.model.entities.Images;
import com.search.teacher.Techlearner.model.entities.Teacher;
import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.model.enums.Degree;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.repository.ImageRepository;
import com.search.teacher.Techlearner.repository.TeacherRepository;
import com.search.teacher.Techlearner.repository.TopicsRepository;
import com.search.teacher.Techlearner.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TeacherRepository teacherRepository;
    private final ImageRepository imageRepository;
    private final TopicsRepository topicsRepository;
    private final TeacherMapper teacherMapper;
    private final TopicMapper topicMapper;

    @Override
    public SaveResponse newTeacher(User user, TeacherRequest request) {
        Teacher teacher = new Teacher();
        teacher.setPhoneNumber(request.getPhoneNumber());
        teacher.setFirstname(user.getFirstname());
        teacher.setLastname(user.getLastname());
        teacher.setEmail(user.getEmail());
        teacher.setUser(user);
        teacher.setDescription(request.getDescription());
        teacher.setActive(true);
        teacher.setTopics(request.getTopics());
        teacherRepository.save(teacher);
        List<Images> images = imageRepository.findAllByIdIn(request.getImages());
        for (Images image: images) {
            image.setTeacher(teacher);
            imageRepository.save(image);
        }


        return null;
    }

    @Override
    public SaveResponse uploadFile(MultipartFile file) {
        return null;
    }

    @Override
    public JResponse allDegrees() {
        return JResponse.success(Degree.values());
    }

    @Override
    public TeacherResponse getTeacher(Long id) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        if (optionalTeacher.isEmpty())
            throw new NotfoundException("Teacher not found this id: " + id);
        Teacher teacher = optionalTeacher.get();
        TeacherResponse response = teacherMapper.toResponse(teacher);
        List<DescribeDto> topics = topicMapper.toListDto(topicsRepository.findAll());
        topics = topics.stream().peek(topic -> {
            if (teacher.getTopics().contains(topic.getId()))
                topic.setActive(true);
        }).collect(Collectors.toList());
        response.setTopics(topics);
        return response;
    }
}
