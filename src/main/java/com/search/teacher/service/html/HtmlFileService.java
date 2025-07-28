package com.search.teacher.service.html;

import com.search.teacher.dto.QuestionAnswerView;
import com.search.teacher.model.entities.ExamScore;
import com.search.teacher.model.entities.UserWritingAnswer;
import com.search.teacher.model.entities.Writing;
import com.search.teacher.utils.DateUtils;
import com.search.teacher.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HtmlFileService {

    @Autowired
    private SpringTemplateEngine templateEngine;

    public String generateMockResultHtml(Date testDate, ExamScore examScore, String fullName, String pdfDownloadUrl) {
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
        context.setVariable("pdfDownloadUrl", pdfDownloadUrl);
        return templateEngine.process("mock-answer-result.html", context);
    }

    public String generateResultHtmlPdf(List<Writing> writings, List<UserWritingAnswer> answers) {

        List<QuestionAnswerView> questionViews = new ArrayList<>();

        for (Writing question : writings) {
            UserWritingAnswer answer = answers.stream()
                    .filter(a -> a.getWritingId() == question.getId())
                    .findFirst()
                    .orElse(null);

            QuestionAnswerView view = new QuestionAnswerView();
            view.setQuestion(question);
            assert answer != null;
            view.setFeedback(answer.getCheckWriting().getResponse());
            view.setAnswer(answer);
            view.setScore(String.valueOf(answer.getCheckWriting().getScore()));
            view.setSummary(answer.getCheckWriting().getSummary());
            questionViews.add(view);
        }

        Context context = new Context();
        context.setVariable("questionViews", questionViews);
        return templateEngine.process("mock-exam-download.html", context) ;
    }
}
