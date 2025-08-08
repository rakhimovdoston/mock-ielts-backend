package com.search.teacher.components;

import com.search.teacher.service.exam.ExamService;
import com.search.teacher.service.exam.SendAnswerServices;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Scheduling {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SendAnswerServices services;
    private final ExamService examService;


    @Scheduled(cron = "0 0 21 * * *")
    public void sendAnswerMessaging() {
//        services.sendAnswers();
        logger.info("Sending answers to messaging");
    }

    @Scheduled(cron = "0 0 5 * * *")
    public void checkAllExamScoreRecalculated() {
        logger.info("Checking all exam score recalculated");
        services.refreshAnswer();
    }
}
