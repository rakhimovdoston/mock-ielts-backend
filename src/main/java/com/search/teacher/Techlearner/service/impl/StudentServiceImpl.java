package com.search.teacher.Techlearner.service.impl;

import com.search.teacher.Techlearner.dto.request.StudentRequest;
import com.search.teacher.Techlearner.dto.response.DescribeDto;
import com.search.teacher.Techlearner.dto.response.SaveResponse;
import com.search.teacher.Techlearner.dto.response.StudentResponse;
import com.search.teacher.Techlearner.exception.NotfoundException;
import com.search.teacher.Techlearner.mapper.DescribeMapper;
import com.search.teacher.Techlearner.mapper.GoalsMapper;
import com.search.teacher.Techlearner.mapper.TopicMapper;
import com.search.teacher.Techlearner.mapper.UserMapper;
import com.search.teacher.Techlearner.model.entities.Describe;
import com.search.teacher.Techlearner.model.entities.Goals;
import com.search.teacher.Techlearner.model.entities.Topics;
import com.search.teacher.Techlearner.model.entities.User;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.repository.DescribeRepository;
import com.search.teacher.Techlearner.repository.GoalsRepository;
import com.search.teacher.Techlearner.repository.TopicsRepository;
import com.search.teacher.Techlearner.repository.UserRepository;
import com.search.teacher.Techlearner.service.user.StudentService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final GoalsRepository goalsRepository;
    private final DescribeRepository describeRepository;
    private final TopicsRepository topicsRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final DescribeMapper describeMapper;
    private final TopicMapper topicMapper;
    private final GoalsMapper goalsMapper;

    public StudentServiceImpl(GoalsRepository goalsRepository, DescribeRepository describeRepository, TopicsRepository topicsRepository, UserRepository userRepository, UserMapper userMapper, DescribeMapper describeMapper, TopicMapper topicMapper, GoalsMapper goalsMapper) {
        this.goalsRepository = goalsRepository;
        this.describeRepository = describeRepository;
        this.topicsRepository = topicsRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.describeMapper = describeMapper;
        this.topicMapper = topicMapper;
        this.goalsMapper = goalsMapper;
    }

    @Override
    public Map<String, Object> infoDescription() {
        List<Goals> goals = goalsRepository.findAll();
        List<Describe> describes = describeRepository.findAll();
        List<Topics> topics = topicsRepository.findAll();
        Map<String, Object> map = new HashMap<>();

        map.put("goals", goals);
        map.put("describes", describes);
        map.put("topics", topics);
        return map;
    }

    @Override
    public JResponse saveStudent(StudentRequest requestBody) {
        User user = userRepository.findByEmail(requestBody.getEmail()).orElse(null);
        if (user == null) {
            return JResponse.error(400, "User not found");
        }
        user.setTopics(requestBody.getTopics());
        Describe describe = describeRepository.findById(requestBody.getDescribeId()).get();
        user.setDescribe(describe);
        user.setGoals(requestBody.getGoals());
        userRepository.save(user);
        return JResponse.success(new SaveResponse(user.getId()));
    }

    @Override
    public SaveResponse updateStudent(User currentUser, StudentRequest request) {
        if (!request.getTopics().isEmpty())
            currentUser.setTopics(request.getTopics());

        if (request.getFirstname() != null)
            currentUser.setFirstname(request.getFirstname());

        if (request.getLastname() != null)
            currentUser.setLastname(request.getLastname());

        if (request.getDescribeId() != null) {
            Optional<Describe> describe = describeRepository.findById(request.getDescribeId());
            describe.ifPresent(currentUser::setDescribe);
        }
        if (!request.getGoals().isEmpty())
            currentUser.setGoals(request.getGoals());

        userRepository.save(currentUser);
        return new SaveResponse(currentUser.getId());
    }

    @Override
    public StudentResponse getStudentById(User user) {
        StudentResponse response = userMapper.toResponse(user);
        List<Describe> describes = describeRepository.findAll();
        List<Topics> topics = topicsRepository.findAll();
        List<Goals> goals = goalsRepository.findAll();
        List<DescribeDto> describeDtos = describeMapper.toListDto(describes);
        List<DescribeDto> goalDtos = goalsMapper.toListDto(goals);
        List<DescribeDto> topicDtos = topicMapper.toListDto(topics);

        if (user.getDescribe() != null)
            describeDtos = describeDtos.stream().peek(describe -> {
                if (describe.getId().equals(user.getDescribe().getId()))
                    describe.setActive(true);
            }).collect(Collectors.toList());

        goalDtos = goalDtos.stream().peek(goal -> {
            if (user.getGoals().contains(goal.getId()))
                goal.setActive(true);
        }).collect(Collectors.toList());

        topicDtos = topicDtos.stream().peek(topic -> {
            if (user.getTopics().contains(topic.getId()))
                topic.setActive(true);
        }).collect(Collectors.toList());

        response.setDescribes(describeDtos);
        response.setTopics(topicDtos);
        response.setGoals(goalDtos);
        return response;
    }
}
