package com.search.teacher.Techlearner.service.html;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class HtmlFileService {

    @Autowired
    private SpringTemplateEngine templateEngine;

    public String confirmationHtmlContent(String email, String code) {
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("code", code);
        return templateEngine.process("confirmation-code.html", context);
    }

    public String confirmationForgotPasswordHtmlContent(String email, String code) {
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("code", code);
        return templateEngine.process("confirmation-code.html", context);
    }
}
