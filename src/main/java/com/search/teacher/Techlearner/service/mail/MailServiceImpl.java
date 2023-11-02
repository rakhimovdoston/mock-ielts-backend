package com.search.teacher.Techlearner.service.mail;

import com.search.teacher.Techlearner.service.html.HtmlFileService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailSendService {

    @Autowired
    private JavaMailSender emailSender;

    private final HtmlFileService htmlFileService;
    @Override
    public void sendConfirmRegister(String email, String code) {
        try {
            String htmlContent = htmlFileService.confirmationHtmlContent(email, code);
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom("rdoston22@gmail.com");
            helper.setTo(email);
            helper.setSubject("Your confirmation code for teacher search");
            helper.setText(htmlContent, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
