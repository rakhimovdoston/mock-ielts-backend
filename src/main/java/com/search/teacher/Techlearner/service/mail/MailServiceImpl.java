package com.search.teacher.Techlearner.service.mail;

import com.search.teacher.Techlearner.service.html.HtmlFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailSendService {

    @Autowired
    private JavaMailSender emailSender;

    private final HtmlFileService htmlFileService;
    @Override
    public void sendConfirmRegister(String email, String code) {
        String htmlContent = htmlFileService.confirmationHtmlContent(email, code);
    }
}
