package com.search.teacher.service.html;

import com.search.teacher.model.entities.ExamScore;
import com.search.teacher.utils.DateUtils;
import com.search.teacher.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;

@Service
public class HtmlFileService {

    @Autowired
    private SpringTemplateEngine templateEngine;

    public String generateMockResultHtml(Date testDate, ExamScore examScore, String fullName) {
        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("testDate", DateUtils.formatMockResult(testDate));
        context.setVariable("overall", Utils.countOverall(examScore));
        context.setVariable("listening", examScore.getListening());
        context.setVariable("reading", examScore.getReading());
        context.setVariable("writing", examScore.getWriting());
        context.setVariable("speaking", examScore.getSpeaking());
        context.setVariable("listeningCount", examScore.getListeningCount());
        context.setVariable("readingCount", examScore.getReadingCount());
        return templateEngine.process("mock-answer-result.html", context);
    }
}
