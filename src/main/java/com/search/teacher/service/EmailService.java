package com.search.teacher.service;

import com.search.teacher.model.entities.ExamScore;
import com.search.teacher.model.entities.User;
import com.search.teacher.service.html.HtmlFileService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EmailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender mailSender;
    private final HtmlFileService htmlFileService;

    public EmailService(HtmlFileService htmlFileService) {
        this.htmlFileService = htmlFileService;
    }

    public String sendMockExamResult(User user, Date testDate, ExamScore score, String pdfDownloadUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(user.getEmail());
            helper.setSubject("Your Mock Exam Results - Everest CDI");
            String htmlContent = htmlFileService.generateMockResultHtml(testDate, score, user.getFirstname() + " " + user.getLastname(), pdfDownloadUrl);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            return "success";

        } catch (MessagingException e) {
            logger.error("Error sending email: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error with mail service: {}", e.getMessage());
        }
        return "error";
    }
}

