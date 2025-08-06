package com.search.teacher.service.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.teacher.dto.ai.WritingAIFeedback;
import com.search.teacher.model.entities.*;
import com.search.teacher.model.response.AIResponse;
import com.search.teacher.repository.*;
import com.search.teacher.utils.StringUtils;
import com.search.teacher.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WritingCheckingService {

    private final Logger logger = LoggerFactory.getLogger(WritingCheckingService.class);

    private final ExamScoreRepository examScoreRepository;
    private final WritingRepository writingRepository;
    private final ObjectMapper objectMapper;
    private final CheckWritingRepository checkWritingRepository;
    private final AIService aiService;
    private final MockTestExamRepository mockTestExamRepository;
    private final UserRepository userRepository;

    public WritingCheckingService(ExamScoreRepository examScoreRepository, WritingRepository writingRepository, ObjectMapper objectMapper, CheckWritingRepository checkWritingRepository, AIService aiService, MockTestExamRepository mockTestExamRepository, UserRepository userRepository) {
        this.examScoreRepository = examScoreRepository;
        this.writingRepository = writingRepository;
        this.objectMapper = objectMapper;
        this.checkWritingRepository = checkWritingRepository;
        this.aiService = aiService;
        this.mockTestExamRepository = mockTestExamRepository;
        this.userRepository = userRepository;
    }

    @Async("writingExecutor")
    public void checkWritingAsync(Long examId, Long userId) {
        MockTestExam mockTestExam = mockTestExamRepository.findById(examId).orElse(null);
        if (mockTestExam == null) {
            logger.warn("Mock test exam with id {} not found", examId);
            return;
        }

        User currentUser = userRepository.findById(userId).orElse(null);
        if (currentUser == null) {
            logger.warn("User with id {} not found", userId);
            return;
        }
        ExamScore score = mockTestExam.getScore();
        if (score == null) {
            score = new ExamScore();
            score.setMockTestExam(mockTestExam);
        }
        Double writingScore = checkWritingWithAI(mockTestExam);
        score.setWriting(String.valueOf(writingScore));
        score.setAssessmentByUserId(currentUser.getId());
        examScoreRepository.save(score);
        logger.info("Writing score for exam {} is {} userId: {}", mockTestExam.getId(), writingScore, currentUser.getId());
    }

    public Double checkWritingWithAI(MockTestExam mockTestExam) {
        List<UserWritingAnswer> userWritingAnswer = mockTestExam.getWritingAnswers();
        List<Writing> writings = writingRepository.findAllById(mockTestExam.getWritings());

        double taskOneScore = 0.0;
        double taskTwoScore = 0.0;

        Writing taskOne = writings.stream().filter(Writing::isTask).findFirst().orElse(null);

        if (taskOne != null) {
            UserWritingAnswer answerOne = userWritingAnswer.stream().filter(ans -> ans.getWritingId().equals(taskOne.getId())).findFirst().orElse(null);
            if (answerOne != null) {
                taskOneScore = checkWritingModule(answerOne, taskOne);
            }
        }

        Writing taskTwo = writings.stream().filter(writing -> !writing.isTask()).findFirst().orElse(null);

        if (taskTwo != null) {
            UserWritingAnswer answerTwo = userWritingAnswer.stream().filter(ans -> ans.getWritingId().equals(taskTwo.getId())).findFirst().orElse(null);
            if (answerTwo != null) {
                taskTwoScore = checkWritingModule(answerTwo, taskTwo);
            }
        }
        double totalScore = taskOneScore + 2 * taskTwoScore;
        return Utils.roundToNearestHalfBand(totalScore / 3);
    }

    private Double checkWritingModule(UserWritingAnswer answer, Writing writing) {
        if (answer.getCheckWriting() != null) {
            return answer.getCheckWriting().getScore();
        }

        if (StringUtils.isNullOrEmpty(answer.getAnswer())) {
            return 0.0;
        }

        String imageDescription = "";
        if (writing.isTask() && writing.getImage() != null && writing.getImageDescription() == null) {
            imageDescription = aiService.getDescriptionImage(writing.getImage());
            if (imageDescription == null) {
                logger.warn("AI could not get image description for writing id {}", writing.getId());
                return 0.0;
            }
            writing.setImageDescription(imageDescription);
            writingRepository.save(writing);
        } else {
            imageDescription = writing.getImageDescription();
        }

        AIResponse<JsonNode> taskOneResponse = aiService.checkWriting(answer.getAnswer(), writing.getTitle(), imageDescription, writing.isTask());
        if (!taskOneResponse.isSuccess()) {
            logger.error("Check Writing {} Method: {}", writing.isTask(), taskOneResponse.getMessage());
            return 0.0;
        }
        JsonNode data = taskOneResponse.getData();
        WritingAIFeedback aiFeedback = null;
        try {
            if (data.isTextual()) {
                aiFeedback = objectMapper.readValue(data.asText(), WritingAIFeedback.class);
            } else {
                aiFeedback = objectMapper.treeToValue(data, WritingAIFeedback.class);
            }
        } catch (JsonProcessingException e) {
            logger.warn("AI Response could not be parsed into WritingAIResponse for Writing id {}, UserWriting id {} Error: {}", writing.getId(), answer.getId(), e.getMessage());
        }

        if (aiFeedback == null) return 0.0;
        return saveCheckWriting(answer, aiFeedback);
    }

    private double saveCheckWriting(UserWritingAnswer answer, WritingAIFeedback feedback) {
        CheckWriting checkWriting = new CheckWriting();
        checkWriting.setUserWritingAnswer(answer);
        checkWriting.setResponse(feedback);
        checkWriting.setScore(feedback.getOverallScore());
        checkWriting.setSummary(feedback.getSummary());
        checkWritingRepository.save(checkWriting);
        double score = feedback.getLexicalResource().getScore() + // 7.5
                (feedback.getTaskAchievement() != null ?
                        feedback.getTaskAchievement().getScore() :
                        feedback.getTaskResponse().getScore()) + // 7
                feedback.getCoherenceAndCohesion().getScore() + // 6
                feedback.getGrammaticalRangeAndAccuracy().getScore(); //5
        score = score / 4;
        return Utils.roundToNearestWriting(score);
    }
}
