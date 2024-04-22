package com.search.teacher.Techlearner.service.impl;

import com.search.teacher.Techlearner.components.Constants;
import com.search.teacher.Techlearner.dto.request.EducationRequest;
import com.search.teacher.Techlearner.dto.request.ExperienceRequest;
import com.search.teacher.Techlearner.dto.request.TeacherRequest;
import com.search.teacher.Techlearner.dto.response.DescribeDto;
import com.search.teacher.Techlearner.dto.response.SaveResponse;
import com.search.teacher.Techlearner.dto.response.TeacherResponse;
import com.search.teacher.Techlearner.exception.NotfoundException;
import com.search.teacher.Techlearner.mapper.TeacherMapper;
import com.search.teacher.Techlearner.mapper.TopicMapper;
import com.search.teacher.Techlearner.model.entities.*;
import com.search.teacher.Techlearner.model.enums.Degree;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.repository.*;
import com.search.teacher.Techlearner.service.user.TeacherService;
import com.search.teacher.Techlearner.service.upload.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
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
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final FileUploadService fileUploadService;
    private final TopicMapper topicMapper;

    @Override
    public SaveResponse newTeacher(User user, TeacherRequest request) {
        Teacher teacher = new Teacher();
        teacher.setTitle(request.getTitle());
        teacher.setPhoneNumber(request.getPhoneNumber());
        teacher.setFirstname(user.getFirstname());
        teacher.setLastname(user.getLastname());
        teacher.setEmail(user.getEmail());
        teacher.setUser(user);
        teacher.setDescription(request.getDescription());
        teacher.setActive(true);
        teacher.setTopics(request.getTopics());
        teacherRepository.save(teacher);
        if (!request.getImages().isEmpty()) {
            List<Images> images = imageRepository.findAllByIdIn(request.getImages());
            for (Images image : images) {
                image.setUser(user);
                imageRepository.save(image);
            }
        }

        if (!request.getEducations().isEmpty()) {
            for (EducationRequest educationRequest: request.getEducations()) {
                Education education = new Education();
                education.setDegree(Degree.getDegree(educationRequest.getDegree()));
                education.setUrl(educationRequest.getUrl());
                education.setName(educationRequest.getName());
                education.setFaculty(educationRequest.getFaculty());
                education.setEntry(educationRequest.getEntry());
                education.setEnd(educationRequest.getEnd());
                education.setTeacher(teacher);
                educationRepository.save(education);
            }
        }
        if (!request.getExperiences().isEmpty()) {
            for (ExperienceRequest experienceRequest: request.getExperiences()) {
                Experience experience = new Experience();
                experience.setDescription(experienceRequest.getDescription());
                experience.setTitle(request.getTitle());
                experience.setEnds(experienceRequest.getEnd());
                experience.setEntry(experienceRequest.getEntry());
                experience.setCompanyName(experienceRequest.getCompanyName());
                experience.setTeacher(teacher);
                experienceRepository.save(experience);
            }
        }
        return new SaveResponse(teacher.getId());
    }

    @Override
    public SaveResponse uploadFile(User currentUser, MultipartFile file) {
        Images image = new Images();
        image.setActive(true);
        image.setSize(file.getSize());
        image.setContentType(file.getContentType());
        fileUploadService.uploadImage(image, file);
        image.setUser(currentUser);
        imageRepository.save(image);
        return new SaveResponse(image.getId());
    }

    @Override
    public JResponse allDegrees() {
        return JResponse.success(Degree.values());
    }

    @Override
    public TeacherResponse getTeacher(User currentUser) {
        Teacher teacher = teacherRepository.findByUser(currentUser);
        if (teacher == null)
            throw new NotfoundException("Teacher not found");
        TeacherResponse response = teacherMapper.toResponse(teacher);
        List<DescribeDto> topics = topicMapper.toListDto(topicsRepository.findAll());
        topics = topics.stream().peek(topic -> {
            if (teacher.getTopics().contains(topic.getId()))
                topic.setActive(true);
        }).collect(Collectors.toList());
        response.setTopics(topics);
        return response;
    }

    @Override
    @Cacheable(cacheNames = Constants.TEACHER_BY_ID, key = "#teacherId")
    public Teacher findByIdAndActive(Long teacherId) {
        return teacherRepository.findNotFoundTeacher(teacherId, true);
    }

    @Override
    public SaveResponse updateTeacher(User user, TeacherRequest request) {
        return null;
    }

    @Override
    public JResponse allTopics() {
        List<Topics> topics = topicsRepository.findAll();
        return JResponse.success(topics);
    }

    @Override
    public void updateRatingTeacher(Teacher teacher, List<Double> ratings) {
        double sumRatings = ratings.stream().mapToDouble(rating -> rating).sum();
        teacher.setRating(sumRatings / ratings.size());
        teacherRepository.save(teacher);
    }
}
