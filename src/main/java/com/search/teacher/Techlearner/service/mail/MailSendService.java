package com.search.teacher.Techlearner.service.mail;

public interface MailSendService {

    void sendConfirmRegister(String email, String code);
    void sendConfirmForgot(String email, String code);
}
