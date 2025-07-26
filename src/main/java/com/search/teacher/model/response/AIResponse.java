package com.search.teacher.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AIResponse<T> {
    private T data;
    private String message;
    private String status;
    private int code;
    private T error;

    public AIResponse(T data, String message, String status, int code, T error) {
        this.data = data;
        this.message = message;
        this.status = status;
        this.code = code;
        this.error = error;
    }

    public boolean isSuccess() {
        return code == 200 && status.equals("success") && data != null;
    }
}
