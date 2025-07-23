package com.search.teacher.service.impl;

import com.search.teacher.dto.filter.UserFilter;
import com.search.teacher.dto.request.history.EmailAnswerRequest;
import com.search.teacher.dto.request.history.ScoreRequest;
import com.search.teacher.dto.request.module.AICheckerRequest;
import com.search.teacher.dto.request.module.ModuleAnswersRequest;
import com.search.teacher.dto.request.module.ModuleQuestionAnswerRequest;
import com.search.teacher.dto.request.history.ModuleScoreRequest;
import com.search.teacher.dto.request.test.TestUserAnswerRequest;
import com.search.teacher.dto.request.test.WritingTestAnswerRequest;
import com.search.teacher.dto.request.test.WritingTestRequest;
import com.search.teacher.dto.response.ExamResponse;
import com.search.teacher.dto.response.SaveResponse;
import com.search.teacher.dto.response.history.MockExamResponse;
import com.search.teacher.dto.response.history.ReadingHistoryResponse;
import com.search.teacher.dto.response.history.WritingHistoryResponse;
import com.search.teacher.dto.response.history.WritingUserAnswerResponse;
import com.search.teacher.dto.response.module.ModuleAnswerResponse;
import com.search.teacher.dto.response.module.QuestionResponse;
import com.search.teacher.dto.response.module.WritingResponse;
import com.search.teacher.dto.response.test.ListeningTestResponse;
import com.search.teacher.dto.response.test.ReadingTestResponse;
import com.search.teacher.exception.BadRequestException;
import com.search.teacher.exception.NotfoundException;
import com.search.teacher.mapper.ModuleAnswerMapper;
import com.search.teacher.mapper.WritingMapper;
import com.search.teacher.model.entities.*;
import com.search.teacher.model.enums.Status;
import com.search.teacher.model.response.ErrorMessages;
import com.search.teacher.model.response.JResponse;
import com.search.teacher.repository.*;
import com.search.teacher.service.EmailService;
import com.search.teacher.service.exam.BookingService;
import com.search.teacher.service.exam.ExamService;
import com.search.teacher.utils.Constants;
import com.search.teacher.utils.ExamUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final MockTestExamRepository mockTestExamRepository;
    private final ReadingRepository readingRepository;
    private final ListeningRepository listeningRepository;
    private final WritingRepository writingRepository;
    private final UserExamAnswerRepository userExamAnswerRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final UserWritingAnswerRepository userWritingAnswerRepository;
    private final ExamScoreRepository examScoreRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ScoreRepository scoreRepository;
    private final BookingService bookingService;

    @Override
    public JResponse getExam(User currentUser, String id) {
        MockTestExam mockTestExam = mockTestExamRepository.findByExamUniqueIdAndUser(id, currentUser);
        if (mockTestExam == null) {
            return JResponse.error(404, "This exam not found.");
        }
        Date nowDate = new Date();
        long now = nowDate.getTime();

        if (mockTestExam.getStartDate() == null) {
            mockTestExam.setStartDate(nowDate);
        }
        long remainingDuration = ExamUtils.calculateRemainingDuration(now, mockTestExam.getStartDate().getTime(), Constants.EXAM_DURATIONS);
        if (remainingDuration < 0) {
            mockTestExam.setStatus(Status.lated.name());
        }
        boolean listening = userExamAnswerRepository.existsByMockTestExamAndListeningIdIn(mockTestExam, mockTestExam.getListening());
        boolean reading = userExamAnswerRepository.existsByMockTestExamAndReadingIdIn(mockTestExam, mockTestExam.getReadings());
        boolean writing = userWritingAnswerRepository.existsByMockTestExamAndWritingIdIn(mockTestExam, mockTestExam.getWritings());
        Booking booking = mockTestExam.getBooking();
        if (booking == null) {
            booking = new Booking();
            booking.setUserId(currentUser.getId());
            booking.setMockTestExam(mockTestExam);
            booking.setStatus(Status.CREATED.name());
            bookingService.saveBooking(booking);
        }
        if (listening && reading && writing) {
            mockTestExam.setSubmittedDate(new Date());
            mockTestExam.setStatus(Status.closed.name());
            booking.setStatus(Status.COMPLETED.name());
            bookingService.saveBooking(booking);
        } else {
            if (!booking.getStatus().equals(Status.IN_COMPLETED.name()) && !booking.getStatus().equals(Status.COMPLETED.name())) {
                booking.setStatus(Status.IN_COMPLETED.name());
                bookingService.saveBooking(booking);
            }
        }
        mockTestExamRepository.save(mockTestExam);
        return JResponse.success(new ExamResponse(
                mockTestExam.getId(),
                listening,
                reading,
                writing,
                remainingDuration));
    }

    @Override
    public JResponse setExamsToUser(User user) {
        MockTestExam oldMockExam = mockTestExamRepository.findByActiveIsTrueAndUserAndStatus(user, Status.opened.name());
        if (oldMockExam != null) {
            return JResponse.success(new SaveResponse(oldMockExam.getExamUniqueId()));
        }

        List<MockTestExam> mockTestExams = mockTestExamRepository.findAllByUser(user);
        List<Long> readingIds = extractExamIds(mockTestExams, MockTestExam::getReadings);
        List<Long> writingIds = extractExamIds(mockTestExams, MockTestExam::getWritings);
        List<Long> listeningIds = extractExamIds(mockTestExams, MockTestExam::getListening);

        List<Reading> readings = user.getUsername().equals("testUser") ?
                readingRepository.findAllByTypeIn(List.of("easy", "medium", "hard"))
                : readingRepository.findAllRandomAndIdNotInAndUserIn(List.of("easy", "medium", "hard"), readingIds.isEmpty() ? List.of(0L) : readingIds, user.getUserId() != null ? List.of(user.getUserId()) : List.of(0L));
        if (readings.isEmpty() || readings.size() < 3) {
            return JResponse.error(404, ErrorMessages.NO_READING_LEFT);
        }
        List<Listening> listenings = user.getUsername().equals("testUser") ?
                listeningRepository.findAllByTypeIn(List.of("part_1", "part_2", "part_3", "part_4")) :
                listeningRepository.findAllRandomAndIdNotInAndUserIn(List.of("part_1", "part_2", "part_3", "part_4"), listeningIds.isEmpty() ? List.of(0L) : listeningIds, user.getUserId() != null ? List.of(user.getUserId()) : List.of(0L));
        if (listenings.isEmpty() || listenings.size() < 4) {
            return JResponse.error(404, ErrorMessages.NO_LISTENING_LEFT);
        }
        List<Writing> writings = user.getUsername().equals("testUser") ?
                writingRepository.findAllByTypeIn(List.of(true, false)) : writingRepository.findAllRandomAndIdNotInAndUserIn(List.of(true, false), writingIds.isEmpty() ? List.of(0L) : writingIds, user.getUserId() != null ? List.of(user.getUserId()) : List.of(0L));
        if (writings.isEmpty() || writings.size() < 2) {
            return JResponse.error(404, ErrorMessages.NO_WRITING_LEFT);
        }

        MockTestExam mockTestExam = new MockTestExam();
        Booking booking = bookingService.getUserExistBooking(user);
        if (booking != null && booking.getStatus().equals(Status.CREATED.name())) {
            booking.setStatus(Status.PROCESS.name());
            bookingService.saveBooking(booking);
        }
        mockTestExam.setStatus(Status.opened.name());
        mockTestExam.setExamUniqueId(UUID.randomUUID().toString().replaceAll("-", ""));
        mockTestExam.setActive(true);
        mockTestExam.setBooking(booking);
        mockTestExam.setUser(user);
        mockTestExam.setReadings(readings.stream().map(Reading::getId).toList());
        mockTestExam.setWritings(writings.stream().map(Writing::getId).toList());
        mockTestExam.setListening(listenings.stream().map(Listening::getId).toList());
        mockTestExamRepository.save(mockTestExam);
        return JResponse.success(new SaveResponse(mockTestExam.getExamUniqueId()));
    }

    @Override
    public JResponse getExamQuestionByModule(User currentUser, String id, String type) {
        MockTestExam mockTestExam = mockTestExamRepository.findByExamUniqueIdAndUser(id, currentUser);
        if (mockTestExam == null) {
            return JResponse.error(404, "This exam not found.");
        }
        return switch (type.toLowerCase()) {
            case "reading" -> getReadingQuestionList(mockTestExam);
            case "writing" -> getWritingQuestions(mockTestExam);
            default -> getListeningQuestionList(mockTestExam);
        };
    }

    @Override
    public JResponse saveModuleAnswers(User currentUser, String id, TestUserAnswerRequest request) {
        MockTestExam mockTestExam = mockTestExamRepository.findByExamUniqueIdAndUser(id, currentUser);
        if (mockTestExam == null) {
            return JResponse.error(404, "This exam not found.");
        }
        for (ModuleAnswersRequest answer : request.questionAnswers()) {
            UserExamAnswers userExamAnswers = new UserExamAnswers();
            userExamAnswers.setMockTestExam(mockTestExam);
            userExamAnswers.setUserId(currentUser.getId());
            if (request.type().equals("reading")) {
                userExamAnswers.setReadingId(answer.passageId());
            } else {
                userExamAnswers.setListeningId(answer.passageId());
            }
            userExamAnswerRepository.save(userExamAnswers);
            saveUserAnswers(answer.answers(), userExamAnswers);
            userExamAnswerRepository.save(userExamAnswers);
        }

        return JResponse.success();
    }

    @Override
    public JResponse saveWritingModuleAnswer(User currentUser, String id, WritingTestRequest request) {
        MockTestExam mockTestExam = mockTestExamRepository.findByExamUniqueIdAndUser(id, currentUser);
        if (mockTestExam == null) {
            return JResponse.error(404, "This exam not found.");
        }
        for (WritingTestAnswerRequest answer : request.answers()) {
            UserWritingAnswer userWritingAnswer = new UserWritingAnswer();
            userWritingAnswer.setMockTestExam(mockTestExam);
            userWritingAnswer.setUserId(currentUser.getId());
            userWritingAnswer.setAnswer(answer.answer());
            userWritingAnswer.setWritingId(answer.id());
            userWritingAnswerRepository.save(userWritingAnswer);
        }
        return JResponse.success();
    }

    @Override
    public JResponse setScoreToUser(User currentUser, Long userId, ScoreRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotfoundException("User not found"));

        if (!user.getUserId().equals(currentUser.getId()))
            return JResponse.error(403, "You can't set score to yourself.");

        MockTestExam mockTestExam = mockTestExamRepository.findByIdAndUser(request.examId(), user);
        ExamScore score = mockTestExam.getScore();
        if (request.type().equals("writing")) {
            score.setWriting(request.score());
        } else if (request.type().equals("speaking")) {
            score.setSpeaking(request.score());
        }
        examScoreRepository.save(score);
        return JResponse.success();
    }

    @Override
    public JResponse sendAnswerToEmail(User currentUser, EmailAnswerRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotfoundException("User not found"));

        if (!user.getUserId().equals(currentUser.getId()))
            return JResponse.error(403, "You can't send answer to that user.");

        MockTestExam mockTestExam = mockTestExamRepository.findByIdAndUser(request.examId(), user);
        if (mockTestExam == null || mockTestExam.getScore() == null) {
            return JResponse.error(404, "The answers for this exam were not sent to the user, No such exam was found.");
        }

        ExamScore score = mockTestExam.getScore();
        Date testDate = mockTestExam.getTestDate() != null ? mockTestExam.getTestDate() : mockTestExam.getSubmittedDate() != null ? mockTestExam.getSubmittedDate() : mockTestExam.getUpdatedDate();
        String response = emailService.sendMockExamResult(user, testDate, score);
        score.setStatus(response);
        examScoreRepository.save(score);
        return JResponse.success();
    }

    @Override
    public JResponse checkWriting(User currentUser, AICheckerRequest request) {
        MockTestExam mockTestExam = mockTestExamRepository.findByIdAndUser(request.examId(), currentUser);
        if (mockTestExam == null) {
            return JResponse.error(404, "This exam not found.");
        }
        List<UserWritingAnswer> writingsAnswers = mockTestExam.getWritingAnswers();

        return null;
    }

    @Override
    public JResponse allExams(User currentUser, UserFilter filter, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotfoundException("User not found"));

        Page<MockTestExam> exams = mockTestExamRepository.findAllByUserOrderByCreatedDateDesc(user, PageRequest.of(filter.getPage(), filter.getSize()));
        if (exams.isEmpty()) {
            return JResponse.error(404, "No exams found.");
        }
        List<MockExamResponse> responses = new ArrayList<>();
        for (MockTestExam exam : exams) {
            MockExamResponse response = new MockExamResponse();
            ExamScore score = exam.getScore();
            Booking booking = exam.getBooking();
            if (score == null && booking != null && booking.getStatus().equals(Status.COMPLETED.name())) {
                score = setScore(exam, currentUser);

                response.setSpeaking(score.getSpeaking());
                response.setWriting(score.getWriting());
                response.setReading(score.getReading());
                response.setListening(score.getListening());
                response.setStatus(score.getStatus());
                response.setExamStatus(getExamStatus(booking.getStatus()));
            }
            response.setStartDate(exam.getStartDate());
            response.setEndDate(exam.getSubmittedDate());
            response.setId(exam.getId());
            responses.add(response);
        }
        return JResponse.success(responses);
    }

    private String getExamStatus(String status) {
        return switch (status) {
            case "COMPLETED" -> "Completed";
            case "PROCESS" -> "Processing";
            case "CREATED" -> "Created";
            default -> "Unknown";
        };
    }

    private ExamScore setScore(MockTestExam mockTestExam, User currentUser) {
        ExamScore score = new ExamScore();
        int reading = checkAnswers(mockTestExam.getReadings(), "reading", mockTestExam.getUserExamAnswers());
        int listening = checkAnswers(mockTestExam.getListening(), "listening", mockTestExam.getUserExamAnswers());
        score.setReading(getBall("reading", reading, scoreRepository.findAll()));
        score.setListening(getBall("listening", listening, scoreRepository.findAll()));
        score.setReadingCount(reading);
        score.setListeningCount(listening);
        score.setAssessmentByUserId(currentUser.getId());
        score.setMockTestExam(mockTestExam);
        examScoreRepository.save(score);
        return score;
    }

    private String getBall(String type, int count, List<Score> scores) {
        for (Score score : scores) {
            if (type.equals("reading")) {
                String reading = score.getReading();
                if (checkBall(count, reading)) return score.getBand();
            } else if (type.equals("listening")) {
                String listening = score.getListening();
                if (checkBall(count, listening)) return score.getBand();
            }
        }
        return "0.0";
    }

    private boolean checkBall(int count, String listening) {
        if (listening.contains("-")) {
            String[] parts = listening.split("-");
            int min = Integer.parseInt(parts[0]);
            int max = Integer.parseInt(parts[1]);
            return count >= min && max >= count;
        } else {
            int ball = Integer.parseInt(listening);
            return ball == count;
        }
    }

    private int checkAnswers(List<Long> moduleIds, String type, List<UserExamAnswers> userExamAnswers) {
        List<ModuleAnswer> answers;
        List<UserAnswers> userAnswers;
        if (type.equals("reading")) {
            List<Reading> readings = readingRepository.findAllById(moduleIds);
            answers = readings.stream().map(Reading::getAnswers).flatMap(List::stream).toList();
            userAnswers = userExamAnswers.stream()
                    .filter(answer -> answer.getReadingId() != null)
                    .map(UserExamAnswers::getAnswers)
                    .flatMap(List::stream)
                    .toList();
        } else {
            List<Listening> listenings = listeningRepository.findAllById(moduleIds);
            answers = listenings.stream().map(Listening::getAnswers).flatMap(List::stream).toList();
            userAnswers = userExamAnswers.stream()
                    .filter(answer -> answer.getListeningId() != null)
                    .map(UserExamAnswers::getAnswers)
                    .flatMap(List::stream)
                    .toList();
        }
        return countAnswer(answers, userAnswers);
    }

    public int countAnswer(List<ModuleAnswer> answers, List<UserAnswers> userAnswers) {
        int count = 0;
        for (ModuleAnswer answer : answers) {
            if (answer.getKey() != null && checkKey(answer, userAnswers)) {
                count++;
            } else if (answer.getKeys() != null)
                count += checkKeys(answer, userAnswers);
        }
        return count;
    }

    private boolean checkKey(ModuleAnswer answer, List<UserAnswers> userAnswers) {
        for (UserAnswers userAnswer : userAnswers) {
            if (answer.getValue().contains("; ")) {
                String[] parts = answer.getValue().split("; ");
                if (answer.getKey().equals(userAnswer.getKey())) {
                    for (String part : parts) {
                        if (part.trim().equalsIgnoreCase(userAnswer.getValue().trim()))
                            return true;
                    }
                }
            } else {
                if (userAnswer.getKey() != null && answer.getKey().equals(userAnswer.getKey())) {
                    return answer.getValue().trim().equalsIgnoreCase(userAnswer.getValue().trim());
                }
            }
        }
        return false;
    }

    private int checkKeys(ModuleAnswer answer, List<UserAnswers> userAnswers) {
        int count = 0;
        userAnswers = userAnswers.stream().filter(userAnswer -> userAnswer.getKeys() != null).toList();
        for (UserAnswers userAnswer : userAnswers) {
            if (answer.getKeys().equals(userAnswer.getKeys())) {
                for (String value : userAnswer.getValues()) {
                    if (answer.getValues().contains(value.trim())) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    @Override
    public JResponse giveScoreModule(User currentUser, ModuleScoreRequest request) {
        MockTestExam mockTestExam = mockTestExamRepository.findByIdAndUser(request.examId(), currentUser);
        if (mockTestExam == null) {
            return JResponse.error(404, "This exam not found.");
        }

        ExamScore score = mockTestExam.getScore();
        if (score == null) {
            score = new ExamScore();
            score.setMockTestExam(mockTestExam);
        }

        if (request.moduleType().equals("reading")) {
            score.setReading(request.score());
        } else if (request.moduleType().equals("writing")) {
            score.setWriting(request.score());
        } else if (request.moduleType().equals("listening")) {
            score.setListening(request.score());
        }
        score.setDescription(request.description());
        score.setAssessmentByUserId(currentUser.getId());

        examScoreRepository.save(score);
        return JResponse.success();
    }

    @Override
    public JResponse getMockExamHistory(User currentUser, Long id, String type) {
        MockTestExam mockTestExam = mockTestExamRepository.findById(id)
                .orElseThrow(() -> new NotfoundException("Mock test exam not found"));

        if (!mockTestExam.getUser().getUserId().equals(currentUser.getId())) {
            throw new BadRequestException("You can't get mock exam history.");
        }
        return switch (type) {
            case "listening" -> getReadingHistory(mockTestExam);
            case "reading" -> getListeningHistory(mockTestExam);
            default -> getWritingHistory(mockTestExam);
        };
    }

    private JResponse getListeningHistory(MockTestExam mockTestExam) {
        List<Reading> readings = readingRepository.findAllById(mockTestExam.getReadings());
        List<ModuleAnswer> userExamAnswers = readings.stream().flatMap(reading -> reading.getAnswers().stream()).toList();
        List<UserAnswers> answers = mockTestExam.getUserExamAnswers().stream().filter(exam -> exam.getReadingId() != null).flatMap(userExamAnswer -> userExamAnswer.getAnswers().stream()).toList();
        List<ModuleAnswerResponse> answerResponse = ModuleAnswerMapper.INSTANCE.toList(userExamAnswers);
        List<ModuleAnswerResponse> userAnswerResponse = new ArrayList<>();
        for (UserAnswers userAnswer : answers) {
            ModuleAnswerResponse response = new ModuleAnswerResponse();
            response.setKey(userAnswer.getKey());
            response.setValues(userAnswer.getValues());
            response.setKeys(userAnswer.getKeys());
            response.setValue(userAnswer.getValue());
            userAnswerResponse.add(response);
        }
        sortKeyAndKeys(answerResponse);
        ReadingHistoryResponse response = new ReadingHistoryResponse();
        response.setAnswers(answerResponse);
        response.setUserAnswers(userAnswerResponse);
        return JResponse.success(response);
    }

    private void sortKeyAndKeys(List<ModuleAnswerResponse> sortableItems) {
        sortableItems.sort((a, b) -> {
            Long keyA = a.getKey();
            Long keyB = b.getKey();

            if (keyA != null && keyB != null) {
                return Long.compare(keyA, keyB);
            }

            if (keyA != null) {
                return -1;
            }

            if (keyB != null) {
                return 1;
            }

            String keysA = a.getKeys();
            String keysB = b.getKeys();

            int numA = extractFirstNumber(keysA);
            int numB = extractFirstNumber(keysB);

            return Integer.compare(numA, numB);
        });
    }

    private static int extractFirstNumber(String keyRange) {
        if (keyRange == null) return Integer.MAX_VALUE;
        try {
            String[] parts = keyRange.split("-");
            return Integer.parseInt(parts[0].trim());
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    private JResponse getReadingHistory(MockTestExam mockTestExam) {
        List<Listening> readings = listeningRepository.findAllById(mockTestExam.getListening());
        List<ModuleAnswer> userExamAnswers = readings.stream().flatMap(reading -> reading.getAnswers().stream()).toList();
        List<UserAnswers> answers = mockTestExam.getUserExamAnswers().stream().filter(exam -> exam.getListeningId() != null).flatMap(userExamAnswer -> userExamAnswer.getAnswers().stream()).toList();
        List<ModuleAnswerResponse> answerResponse = ModuleAnswerMapper.INSTANCE.toList(userExamAnswers);
        List<ModuleAnswerResponse> userAnswerResponse = new ArrayList<>();
        for (UserAnswers userAnswer : answers) {
            ModuleAnswerResponse response = new ModuleAnswerResponse();
            response.setKey(userAnswer.getKey());
            response.setValues(userAnswer.getValues());
            response.setKeys(userAnswer.getKeys());
            response.setValue(userAnswer.getValue());
            userAnswerResponse.add(response);
        }
        ReadingHistoryResponse response = new ReadingHistoryResponse();
        response.setAnswers(answerResponse);
        response.setUserAnswers(userAnswerResponse);
        return JResponse.success(response);
    }

    private JResponse getWritingHistory(MockTestExam mockTestExam) {
        List<UserWritingAnswer> writingAnswers = mockTestExam.getWritingAnswers();
        List<Writing> writings = writingRepository.findAllById(mockTestExam.getWritings());
        List<WritingResponse> questions = WritingMapper.INSTANCE.toList(writings);
        WritingHistoryResponse response = getWritingHistoryResponse(writingAnswers, questions);
        return JResponse.success(response);
    }

    @NotNull
    private static WritingHistoryResponse getWritingHistoryResponse(List<UserWritingAnswer> writingAnswers, List<WritingResponse> questions) {
        List<WritingUserAnswerResponse> answers = new ArrayList<>();
        for (UserWritingAnswer answer : writingAnswers) {
            WritingUserAnswerResponse response = new WritingUserAnswerResponse();
            response.setId(answer.getId());
            response.setAnswer(answer.getAnswer());
            response.setWritingId(answer.getWritingId());
            answers.add(response);
        }

        WritingHistoryResponse response = new WritingHistoryResponse();
        questions.sort((a, b) -> (int) (a.getId() - b.getId()));
        response.setQuestions(questions);
        response.setAnswers(answers);
        return response;
    }

    private void saveUserAnswers(List<ModuleQuestionAnswerRequest> answers, UserExamAnswers userExamAnswers) {
        for (ModuleQuestionAnswerRequest answer : answers) {
            UserAnswers userAnswer = new UserAnswers();
            userAnswer.setUserExamAnswers(userExamAnswers);
            userAnswer.setKeys(answer.keys());
            userAnswer.setValues(answer.values());
            userAnswer.setKey(answer.key());
            userAnswer.setValue(answer.value());
            userAnswerRepository.save(userAnswer);
        }
    }

    private JResponse getListeningQuestionList(MockTestExam mockTestExam) {
        List<Listening> listenings = listeningRepository.findAllById(mockTestExam.getListening());
        if (listenings.isEmpty()) {
            return JResponse.error(404, "No listening questions found.");
        }
        List<ListeningTestResponse> responses = new ArrayList<>();
        var part1 = listenings.stream().filter(l -> l.getType().equals("part_1")).findFirst().orElse(new Listening());
        var part2 = listenings.stream().filter(l -> l.getType().equals("part_2")).findFirst().orElse(new Listening());
        var part3 = listenings.stream().filter(l -> l.getType().equals("part_3")).findFirst().orElse(new Listening());
        var part4 = listenings.stream().filter(l -> l.getType().equals("part_4")).findFirst().orElse(new Listening());
        partToResponse(responses, part1);
        partToResponse(responses, part2);
        partToResponse(responses, part3);
        partToResponse(responses, part4);
        return JResponse.success(responses);
    }

    private void partToResponse(List<ListeningTestResponse> responses, Listening part1) {
        ListeningTestResponse response = new ListeningTestResponse();
        response.setId(part1.getId());
        response.setType(part1.getType());
        response.setAudio(part1.getAudio());
        response.setQuestions(questionList(part1.getQuestions()));
        responses.add(response);
    }

    private JResponse getReadingQuestionList(MockTestExam mockTestExam) {
        List<Reading> readings = readingRepository.findAllById(mockTestExam.getReadings());
        if (readings.isEmpty()) {
            return JResponse.error(404, "No reading questions found.");
        }
        var easy = readings.stream().filter(l -> l.getType().equals("easy")).findFirst().orElse(new Reading());
        var medium = readings.stream().filter(l -> l.getType().equals("medium")).findFirst().orElse(new Reading());
        var hard = readings.stream().filter(l -> l.getType().equals("hard")).findFirst().orElse(new Reading());
        List<ReadingTestResponse> responses = new ArrayList<>();
        partToResponse(responses, easy);
        partToResponse(responses, medium);
        partToResponse(responses, hard);
        return JResponse.success(responses);
    }

    private void partToResponse(List<ReadingTestResponse> responses, Reading reading) {
        ReadingTestResponse response = new ReadingTestResponse();
        response.setId(reading.getId());
        response.setType(reading.getType());
        response.setContent(reading.getContent());
        response.setQuestions(questionList(reading.getQuestions()));
        responses.add(response);
    }

    private JResponse getWritingQuestions(MockTestExam mockTestExam) {
        List<Writing> writings = writingRepository.findAllById(mockTestExam.getWritings());
        if (writings.isEmpty()) {
            return JResponse.error(404, "No writing questions found.");
        }
        List<WritingResponse> responses = new ArrayList<>();
        var taskOne = writings.stream().filter(Writing::isTask).findFirst().orElse(new Writing());
        var taskTwo = writings.stream().filter(w -> !w.isTask()).findFirst().orElse(new Writing());
        responses.add(WritingMapper.INSTANCE.toResponse(taskOne));
        responses.add(WritingMapper.INSTANCE.toResponse(taskTwo));
        return JResponse.success(responses);
    }

    private List<QuestionResponse> questionList(List<ModuleQuestions> questions) {
        List<QuestionResponse> responses = new ArrayList<>();
        for (ModuleQuestions question : questions) {
            QuestionResponse response = new QuestionResponse();
            response.setId(question.getId());
            response.setType(question.getCategoryName());
            response.setContent(question.getQuestion());
            responses.add(response);
        }
        return responses;
    }

    private List<Long> extractExamIds(List<MockTestExam> exams, Function<MockTestExam, List<Long>> extractor) {
        return exams.stream()
                .map(extractor)
                .flatMap(List::stream)
                .toList();
    }
}
