package com.search.teacher.Techlearner.config.handler;

import com.google.gson.Gson;
import com.search.teacher.Techlearner.model.response.JResponse;
import com.search.teacher.Techlearner.utils.GsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        GsonUtils.responseError(401, "Unauthorized", response);
    }
}
