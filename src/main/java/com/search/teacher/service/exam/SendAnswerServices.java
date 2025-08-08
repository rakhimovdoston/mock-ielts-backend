package com.search.teacher.service.exam;

import com.search.teacher.dto.request.RefreshAnswerRequest;
import com.search.teacher.dto.request.history.EmailAnswerRequest;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.model.entities.*;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.*;
import com.search.teacher.service.EmailService;
import com.search.teacher.service.EskizSmsService;
import com.search.teacher.utils.DateUtils;
import com.search.teacher.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SendAnswerServices {

    private final ExamService examService;
    @Value("${sms.pdf.url}")
    private String downloadUrl;

    int batchSize = 10;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(SendAnswerServices.class);

    private final SendAnswerRepository sendAnswerRepository;
    private final UserRepository userRepository;
    private final MockTestExamRepository mockTestExamRepository;
    private final EmailService emailService;
    private final ExamScoreRepository examScoreRepository;
    private final SmsInfoRepository smsInfoRepository;
    private final EskizSmsService eskizSmsService;

    public void sendAnswers() {
        List<SendAnswers> answers = sendAnswerRepository.findAllByStatusAndDate("waiting", LocalDate.now());
        if (answers.isEmpty()) {
            logger.info("No answers to send");
            return;
        }
        int counter = 0;
        for (SendAnswers answer : answers) {
            String response = sendAnswerToEmail(answer.getUserId(), answer.getExamUniqueId(), answer);
            answer.setStatus(response);
            sendAnswerRepository.save(answer);
            counter++;
            if (counter % batchSize == 0) {
                try {
                    Thread.sleep(2000); // 2 sekund uxlatish
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public String sendAnswerToEmail(Long userId, String examUniqueId, SendAnswers answer) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotfoundException("Student not found"));

        MockTestExam mockTestExam = mockTestExamRepository.findByExamUniqueIdAndUser(examUniqueId, user);
        if (mockTestExam == null || mockTestExam.getScore() == null) {
            return "error";
        }

        ExamScore score = mockTestExam.getScore();
        Date testDate = mockTestExam.getTestDate() != null ? mockTestExam.getTestDate() : mockTestExam.getSubmittedDate() != null ? mockTestExam.getSubmittedDate() : mockTestExam.getUpdatedDate();
        String response = emailService.sendMockExamResult(user, testDate, score, downloadUrl + mockTestExam.getExamUniqueId());
        score.setStatus(response);
        examScoreRepository.save(score);
        String smsResponse = sendSmsTo(user, mockTestExam, testDate, response);

        answer.setEmailResponse(response);
        answer.setSmsResponse(smsResponse);
        sendAnswerRepository.save(answer);
        return "success";
    }

    private String sendSmsTo(User user, MockTestExam exam, Date testDate, String emailResponse) {
        if (user.getPhone() == null) {
            logger.warn("User {} has no phone number, skipping SMS sending", user.getId());
            return "error";
        }
        String phoneNumber = user.getPhone().replaceAll("[^\\d]", "");
        if (!phoneNumber.startsWith("+"))
            phoneNumber = "+" + phoneNumber;
        SmsInfo smsInfo = smsInfoRepository.findByPhoneNumberAndMockTestId(phoneNumber, exam.getId());
        if (smsInfo != null) {
            logger.info("SMS already sent for user {} for exam {}", user.getId(), exam.getId());
            return "error";
        } else {
            smsInfo = new SmsInfo();
            smsInfo.setEmailStatus(emailResponse);
            smsInfo.setMockTestId(exam.getId());
            smsInfo.setPhoneNumber(phoneNumber);
            smsInfo.setUserId(user.getId());
            smsInfo.setSmsStatus("error");
            smsInfoRepository.save(smsInfo);
        }
        ExamScore score = exam.getScore();
        String messageTemplate = """
                %s
                Thank you for choosing EVEREST CDI MOCK , here are the results of %s
                
                Overall: %s
                Listening: %s (%s/40)
                Reading: %s (%s/40)
                Writing: %s
                Speaking: %s
                Download the mock exam result here: %s
                """;
        String url = downloadUrl + exam.getExamUniqueId();
        String fullName = user.getFirstname() + " " + user.getLastname();
        String message = String.format(messageTemplate,
                fullName.toUpperCase(), // full name
                DateUtils.formatMockResult(testDate), // test date
                Utils.countOverall(score), // overall
                score.getListening(), // listening score
                score.getListeningCount(), // listening count
                score.getReading(), // reading score
                score.getReadingCount(), // reading count
                score.getWriting(), // writing score
                score.getSpeaking(), // speaking score
                url
        );

        String response = eskizSmsService.sendSms(phoneNumber, message);
        smsInfo.setSmsStatus(response);
        smsInfo.setSmsMessage(message);
        smsInfoRepository.save(smsInfo);
        logger.info("SMS sent to {} for exam {}, response SMS: {}", phoneNumber, exam.getExamUniqueId(), response);
        return response;
    }

    public void refreshAnswer() {
        int pageSize = 50;
        int pageNumber = 0;
        Page<ExamScore> page;

        do {
            page = examScoreRepository.findAllByListeningAndReading(
                    PageRequest.of(pageNumber, pageSize)
            );

            logger.info("Pages: {}, Number: {}, NumbersElements: {}", page, pageNumber, page.getNumberOfElements());
            List<ExamScore> examScores = page.getContent();
            int counter = 0;
            for (ExamScore score : examScores) {
                MockTestExam mockTestExam = mockTestExamRepository.findByScore(score);
                if (mockTestExam == null || !Objects.equals(mockTestExam.getStatus(), "closed")) {
                    logger.warn("Mock test exam not found for score {}", score.getId());
                    continue;
                }
                User user = new User();
                user.setId(mockTestExam.getUser().getUserId());
                user.setUsername("system");
                examService.refreshUserAnswer(user, new EmailAnswerRequest(mockTestExam.getUser().getId(), mockTestExam.getId()));
                counter++;
                if (counter % batchSize == 0) {
                    try {
                        logger.info("Refreshing answers waiting answers: ", counter);
                        Thread.sleep(2000); // 2 sekund uxlatish
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            pageNumber++;
        } while (!page.isLast());
    }

    public JResponse refreshAnswer(RefreshAnswerRequest request) {
        if (request.ids() != null && !request.ids().isEmpty()) {
            for (Long id : request.ids()) {
                Optional<MockTestExam> optional = mockTestExamRepository.findById(id);
                if (optional.isEmpty()) {
                    logger.warn("Mock test exam not found for id: {}", id);
                    continue;
                }
                MockTestExam mockTestExam = optional.get();
                User user = new User();
                user.setId(mockTestExam.getUser().getUserId());
                user.setUsername("system");
                examService.refreshUserAnswer(user, new EmailAnswerRequest(mockTestExam.getUser().getId(), mockTestExam.getId()));
            }
        } else {
            return JResponse.error(400, "Ids not found");
        }
        return JResponse.success("Refreshing answers success");
    }
}
