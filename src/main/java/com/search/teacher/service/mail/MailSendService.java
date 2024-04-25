package com.search.teacher.service.mail;

import com.search.teacher.config.rabbit.EmailPayload;

public interface MailSendService {

    void sendConfirmRegister(EmailPayload emailPayload);
    void sendConfirmForgot(EmailPayload emailPayload);
}
