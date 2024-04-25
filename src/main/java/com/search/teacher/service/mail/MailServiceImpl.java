package com.search.teacher.service.mail;

import com.search.teacher.config.rabbit.EmailPayload;
import com.search.teacher.model.entities.User;
import com.search.teacher.repository.UserRepository;
import com.search.teacher.service.html.HtmlFileService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailSendService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JavaMailSender emailSender;

    private final HtmlFileService htmlFileService;
    private final UserRepository userRepository;

    @Override
    public void sendConfirmRegister(EmailPayload emailPayload) {
        User user = userRepository.findByEmail(emailPayload.getEmail());
        if (user == null) {
            logger.info("{} does not exist.", emailPayload.getEmail());
            return;
        }
        String htmlContent = htmlFileService.confirmationHtmlContent(emailPayload.getEmail(), emailPayload.getCode());
        sendEmail(htmlContent,
                emailPayload.getEmail(),
                "Your confirmation code for teacher search",
                "static/images/header_logo.png",
                "header_logo",
                true);
    }

    @Override
    public void sendConfirmForgot(EmailPayload emailPayload) {
        User user = userRepository.findByEmail(emailPayload.getEmail());
        if (user == null) {
            logger.info("{} does not exist.", emailPayload.getEmail());
            return;
        }
        String htmlContent = htmlFileService.confirmationForgotPasswordHtmlContent(emailPayload.getEmail(), emailPayload.getCode());
        sendEmail(htmlContent,
                emailPayload.getEmail(),
                "Your confirmation code for forgot password",
                "static/images/header_logo.png",
                "header_logo",
                true);
    }

    private void sendEmail(String htmlContent, String receiverEmail, String subject, String imageUrl, String contentId, boolean multipart) {

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, multipart, CharEncoding.UTF_8);
            helper.setFrom("rdoston22@gmail.com");
            helper.setTo(receiverEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            if (imageUrl != null && contentId != null) {
                ClassPathResource resource = new ClassPathResource(imageUrl);
                helper.addInline(contentId, resource);
            }
            emailSender.send(message);
            logger.info("Email sent.");
        } catch (MessagingException e) {
            logger.error("Send email error: {}", e.getMessage());
        }
    }
}
