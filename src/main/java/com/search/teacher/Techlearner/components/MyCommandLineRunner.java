package com.search.teacher.Techlearner.components;

import com.search.teacher.Techlearner.service.mail.MailSendService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyCommandLineRunner implements CommandLineRunner {
    private final MailSendService mailSendService;

    public MyCommandLineRunner(MailSendService mailSendService) {
        this.mailSendService = mailSendService;
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
