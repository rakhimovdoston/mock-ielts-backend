package com.search.teacher.Techlearner.service.mail;

import com.search.teacher.Techlearner.config.rabbit.EmailPayload;

public interface MailSendService {

    void sendConfirmRegister(EmailPayload emailPayload);
    void sendConfirmForgot(EmailPayload emailPayload);
}
