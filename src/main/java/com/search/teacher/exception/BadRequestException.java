package com.search.teacher.exception;

import com.search.teacher.model.response.JResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private JResponse response;
    public BadRequestException(JResponse response) {
        this.response = response;
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public JResponse getResponse() {
        return response;
    }

    public void setResponse(JResponse response) {
        this.response = response;
    }
}
